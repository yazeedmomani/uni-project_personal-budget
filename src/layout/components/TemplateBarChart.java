package layout.components;

import db.models.TemplateRecord;
import javafx.application.Platform;
import javafx.scene.chart.*;

import java.util.List;

/**
 * Generic BarChart template.
 *
 * Things you can change from subclasses:
 *  - Data & all calculations → implement buildSeries(data).
 *  - Bar/legend color → pass via constructor (primaryColor).
 *  - X/Y axis labels → pass via constructor.
 *  - Series labels (one or more) → set names on the returned Series in buildSeries().
 */
public abstract class TemplateBarChart<Record extends TemplateRecord> {
    private BarChart<String, Number> chart;
    private final String primaryColor;        // e.g. "#388E3C" – supply via constructor
    private final String xAxisLabel;          // e.g. "Source"
    private final String yAxisLabel;          // e.g. "Total (JOD)"
    protected final int limitMonths;
    protected final List<Record> data;

    protected TemplateBarChart(List<Record> data, int limitMonths, String xAxisLabel, String yAxisLabel, String primaryColor) {
        this.primaryColor = primaryColor;
        this.xAxisLabel = xAxisLabel == null ? "" : xAxisLabel;
        this.yAxisLabel = yAxisLabel == null ? "" : yAxisLabel;
        this.limitMonths = limitMonths;
        this.data = data;

        initializeChart();
    }

    public BarChart<String, Number> getChart(){
        return chart;
    }

    /**
     * Create and style the JavaFX BarChart for the provided data.
     */
    private void initializeChart() {
        // Axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xAxisLabel);
        xAxis.setStyle("-fx-text-fill: #4d4d4d;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        yAxis.setStyle("-fx-text-fill: #4d4d4d;");

        chart = new javafx.scene.chart.BarChart<>(xAxis, yAxis);
        chart.setAnimated(true);
        chart.setCategoryGap(70);
        chart.setBarGap(6);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setPrefHeight(320);

        // Data series are fully defined by subclasses
        List<XYChart.Series<String, Number>> seriesList = buildSeries();
        chart.getData().addAll(seriesList);

        // Styling after nodes are realized
        Platform.runLater(() -> {
            chart.applyCss();
            chart.layout();

            // Make plot background transparent
            javafx.scene.Node plot = chart.lookup(".chart-plot-background");
            if (plot instanceof javafx.scene.layout.Region region) {
                region.setStyle("-fx-background-color: transparent;");
            }

            // Color bars + add value labels
            for (XYChart.Series<String, Number> s : seriesList) {
                for (XYChart.Data<String, Number> d : s.getData()) {
                    if (d.getNode() != null) {
                        d.getNode().setStyle("-fx-bar-fill: " + primaryColor + ";");
                        // Simple value label above each bar
                        javafx.scene.control.Label label = new javafx.scene.control.Label(String.valueOf(d.getYValue()));
                        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 11px;");
                        javafx.scene.layout.StackPane.setAlignment(label, javafx.geometry.Pos.TOP_CENTER);
                        if (d.getNode() instanceof javafx.scene.layout.StackPane sp) {
                            sp.getChildren().add(label);
                        }
                    }
                }
            }

            // Legend symbol color matches bars
            for (javafx.scene.Node node : chart.lookupAll(".chart-legend-item-symbol")) {
                node.setStyle("-fx-background-color: " + primaryColor + "; -fx-padding: 6px; -fx-background-radius: 30;");
            }

            // Transparent legend background
            javafx.scene.Node legend = chart.lookup(".chart-legend");
            if (legend instanceof javafx.scene.layout.Region legendRegion) {
                legendRegion.setStyle("-fx-background-color: transparent;");
            }
        });
    }

    /**
     * Subclasses must return one or more series, with names set if you want legend entries.
     * Do ALL calculations here (grouping, sums, filtering, etc.).
     */
    protected abstract List<XYChart.Series<String, Number>> buildSeries();
}
