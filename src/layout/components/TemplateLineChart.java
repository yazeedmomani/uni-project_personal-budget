package layout.components;

import db.models.TemplateRecord;
import javafx.application.Platform;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TemplateLineChart<Record extends TemplateRecord> {
    protected final int limitMonths;
    private final boolean addMedian;
    private final String xAxisLabel;
    private final String yAxisLabel;
    private final String primaryColor;
    private final String secondaryColor;
    protected final List<Record> data;

    private LineChart<String, Number> chart;

    /**
     * Template constructor matching TemplateBarChart parameters with two additions:
     * - int limitMonths: limit to the last N months (including current). Use `-1` to include all months in the data.
     * - boolean addMedian: whether to include a median line computed from the first returned series
     * - String secondaryColor: color for the median line and its legend symbol
     */
    protected TemplateLineChart(List<Record> data,
                                int limitMonths,
                                boolean addMedian,
                                String xAxisLabel,
                                String yAxisLabel,
                                String primaryColor,
                                String secondaryColor) {
        this.data = data;
        this.limitMonths = limitMonths;
        this.addMedian = addMedian;
        this.xAxisLabel = xAxisLabel == null ? "" : xAxisLabel;
        this.yAxisLabel = yAxisLabel == null ? "" : yAxisLabel;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        initializeChart();
    }

    /**
     * Public accessor for the configured chart node.
     */
    public LineChart<String, Number> getChart() {
        return chart;
    }

    /**
     * Initialize and style the JavaFX LineChart. Data series are provided by subclasses via buildSeries().
     */
    private void initializeChart() {
        // Axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xAxisLabel);
        xAxis.setStyle("-fx-text-fill: #4d4d4d;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        yAxis.setStyle("-fx-text-fill: #4d4d4d;");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(true);
        chart.setCreateSymbols(true);
        chart.setAnimated(true);

        // Base series from subclass
        List<XYChart.Series<String, Number>> seriesList = buildSeries();
        if (seriesList != null && !seriesList.isEmpty()) {
            chart.getData().addAll(seriesList);

            // Optional median line computed from the first series' Y values
            if (addMedian) {
                XYChart.Series<String, Number> primarySeries = seriesList.get(0);
                List<Double> yVals = primarySeries.getData().stream()
                        .map(dp -> dp.getYValue() == null ? 0.0 : dp.getYValue().doubleValue())
                        .sorted()
                        .collect(Collectors.toList());

                double median;
                int n = yVals.size();
                if (n == 0) {
                    median = 0.0;
                } else if (n % 2 == 1) {
                    median = yVals.get(n / 2);
                } else {
                    median = (yVals.get(n / 2 - 1) + yVals.get(n / 2)) / 2.0;
                }

                XYChart.Series<String, Number> medianSeries = new XYChart.Series<>();
                medianSeries.setName("Median: " + String.format("%,.0f", median));
                for (XYChart.Data<String, Number> dp : primarySeries.getData()) {
                    medianSeries.getData().add(new XYChart.Data<>(dp.getXValue(), median));
                }
                chart.getData().add(medianSeries);
            }
        }

        // Styling after nodes are realized
        Platform.runLater(() -> {
            chart.applyCss();
            chart.layout();

            // Transparent plot background
            javafx.scene.Node plot = chart.lookup(".chart-plot-background");
            if (plot instanceof javafx.scene.layout.Region region) {
                region.setStyle("-fx-background-color: transparent;");
            }

            // Style primary (first) series line and symbols
            if (!chart.getData().isEmpty()) {
                XYChart.Series<String, Number> primarySeries = chart.getData().get(0);
                if (primarySeries.getNode() != null) {
                    primarySeries.getNode().setStyle("-fx-stroke: " + primaryColor + "; -fx-stroke-width: 2px;");
                }
                primarySeries.getData().forEach(d -> {
                    if (d.getNode() != null) d.getNode().setStyle("-fx-background-color: " + primaryColor + ", white;");
                });
            }

            // Style median series (last) if present
            if (addMedian && chart.getData().size() >= 2) {
                XYChart.Series<String, Number> medianSeries = chart.getData().get(chart.getData().size() - 1);
                if (medianSeries.getNode() != null) {
                    medianSeries.getNode().setStyle("-fx-stroke: " + secondaryColor + "; -fx-stroke-dash-array: 5 5; -fx-stroke-width: 2px;");
                }
                medianSeries.getData().forEach(d -> {
                    if (d.getNode() != null) {
                        d.getNode().setVisible(false);
                        d.getNode().setManaged(false);
                    }
                });
            }

            // Legend symbol colors mapped to series order (primary then median)
            int idx = 0;
            for (javafx.scene.Node node : chart.lookupAll(".chart-legend-item-symbol")) {
                if (idx == 0) node.setStyle("-fx-background-color: " + primaryColor + "; -fx-padding: 6px; -fx-background-radius: 30;");
                else if (idx == 1 && addMedian) node.setStyle("-fx-background-color: " + secondaryColor + "; -fx-padding: 6px; -fx-background-radius: 30;");
                idx++;
            }

            // Transparent legend background
            javafx.scene.Node legend = chart.lookup(".chart-legend");
            if (legend instanceof javafx.scene.layout.Region legendRegion) {
                legendRegion.setStyle("-fx-background-color: transparent;");
            }
        });

        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setPrefHeight(320);
    }

    /**
     * Subclasses must return one or more series, with names set if you want legend entries.
     * Do ALL calculations (grouping, sums, filtering, category windows, etc.) here.
     */
    protected abstract List<XYChart.Series<String, Number>> buildSeries();
}
