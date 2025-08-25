package layout.components.income;


import db.Database;
import db.models.IncomeRecord;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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

        chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(true);
        chart.setCategoryGap(70);
        chart.setBarGap(6);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> series.getData().add(new XYChart.Data<>(source, total)));

        chart.getData().add(series);
        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setPrefHeight(320);

        return chart;
    }
}
