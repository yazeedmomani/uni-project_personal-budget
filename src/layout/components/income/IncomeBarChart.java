package layout.components.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeBarChart {
    private static BarChart<String, Number> chart;
    private static List<IncomeRecord> data;

    public static BarChart<String, Number> init(List<IncomeRecord> data){
        IncomeBarChart.data = data;
        return initializeChart();
    }

    private static BarChart<String, Number> initializeChart() {
        YearMonth start = YearMonth.from(LocalDate.now()).minusMonths(5); // inclusive start
        LocalDate startDate = start.atDay(1);
        LocalDate endDate = LocalDate.now(); // inclusive end today

        Map<String, Double> sums = data.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(IncomeRecord::getSource, Collectors.summingDouble(IncomeRecord::getAmount)));

        // Axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Source");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total (JOD)");
        // Set axis label colors
        xAxis.setStyle("-fx-text-fill: #4d4d4d;");
        yAxis.setStyle("-fx-text-fill: #4d4d4d;");

        chart = new BarChart<>(xAxis, yAxis);
        // Keep legend enabled by default
        chart.setAnimated(true);
        chart.setCategoryGap(70);
        chart.setBarGap(6);
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> series.getData().add(new XYChart.Data<>(source, total)));

        chart.getData().add(series);
        series.setName("Source");
        Platform.runLater(() -> {
            chart.applyCss();
            chart.layout();
            // Match legend symbol color to bars
            chart.lookupAll(".chart-legend-item-symbol").forEach(node ->
                    node.setStyle("-fx-background-color: #388E3C; -fx-background-radius: 0; -fx-padding: 6px;"));

            // Transparent plot background (inside area behind bars)
            javafx.scene.Node plot = chart.lookup(".chart-plot-background");
            if (plot != null) {
                ((javafx.scene.layout.Region) plot).setStyle("-fx-background-color: transparent;");
            }

            // Color bars and add value labels (ensure bar nodes exist)
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    d.getNode().setStyle("-fx-bar-fill: #388E3C;");
                    javafx.scene.control.Label label = new javafx.scene.control.Label(String.valueOf(d.getYValue()));
                    label.setStyle("-fx-text-fill: #fff; -fx-font-size: 11px;");
                    javafx.scene.layout.StackPane.setAlignment(label, javafx.geometry.Pos.TOP_CENTER);
                    ((javafx.scene.layout.StackPane) d.getNode()).getChildren().add(label);
                }
            }

            // Legend symbol colors mapped to series order
            for (javafx.scene.Node node : chart.lookupAll(".chart-legend-item-symbol")) {
                node.setStyle("-fx-background-color: #388E3C; -fx-padding: 6px; -fx-background-radius: 30;");
            }

            // Transparent legend background
            javafx.scene.Node legend = chart.lookup(".chart-legend");
            if (legend != null) {
                ((javafx.scene.layout.Region) legend).setStyle("-fx-background-color: transparent;");
            }
        });
        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setPrefHeight(320);

        return chart;
    }
}
