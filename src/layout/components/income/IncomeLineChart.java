package layout.components.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeLineChart {
    private static LineChart<String, Number> chart;
    private static List<IncomeRecord> data;

    public static LineChart<String, Number> init(List<IncomeRecord> data){
        IncomeLineChart.data = data;
        return initializeChart();
    }

    private static LineChart<String, Number> initializeChart() {
        // Define month window: last 6 months including current
        YearMonth startYm = YearMonth.from(LocalDate.now()).minusMonths(5);
        YearMonth endYm = YearMonth.from(LocalDate.now());
        LocalDate startDate = startYm.atDay(1);
        LocalDate endDate = endYm.atEndOfMonth();

        // Group by YearMonth and sum amounts
        Map<YearMonth, Double> monthlyTotals = data.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(r -> YearMonth.from(r.getDate()), Collectors.summingDouble(IncomeRecord::getAmount)));

        // Build ordered categories and values for the 6 months, filling missing ones with 0
        List<YearMonth> months = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            months.add(startYm.plusMonths(i));
        }
        List<Double> values = months.stream()
                .map(ym -> monthlyTotals.getOrDefault(ym, 0.0))
                .collect(Collectors.toList());

        // Compute median of the 6 values
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        double median = (sorted.get(2) + sorted.get(3)) / 2.0; // even count (6)

        // Axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total (JOD)");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(true);
        lineChart.setCreateSymbols(true); // show points

        // Series for monthly totals
        XYChart.Series<String, Number> totalsSeries = new XYChart.Series<>();
        totalsSeries.setName("Monthly Total");

        // Series for median (flat line across all months)
        XYChart.Series<String, Number> medianSeries = new XYChart.Series<>();
        medianSeries.setName("Median: JOD " + String.format("%,.0f", median));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy");
        for (int i = 0; i < months.size(); i++) {
            String label = months.get(i).format(fmt);
            Double val = values.get(i);
            totalsSeries.getData().add(new XYChart.Data<>(label, val));
            medianSeries.getData().add(new XYChart.Data<>(label, median));
        }

        lineChart.getData().addAll(totalsSeries, medianSeries);
        lineChart.setMaxWidth(Double.MAX_VALUE);
        lineChart.setPrefHeight(320);
        return lineChart;
    }
}
