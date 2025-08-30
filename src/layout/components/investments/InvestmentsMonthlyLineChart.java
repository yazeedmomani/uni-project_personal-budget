package layout.components.investments;

import db.models.InvestmentsRecord;
import javafx.scene.chart.XYChart;
import layout.components.templates.TemplateLineChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InvestmentsMonthlyLineChart extends TemplateLineChart<InvestmentsRecord> {
    public InvestmentsMonthlyLineChart(List<InvestmentsRecord> data) {
        super(data, 6, true, "Month", "Growth (JOD)", "#04ABC1", "#4ED0E1", "Median Growth");
    }

    public InvestmentsMonthlyLineChart(List<InvestmentsRecord> data,
                                       int limitMonths,
                                       boolean addMedian,
                                       String xAxisLabel,
                                       String yAxisLabel,
                                       String primaryColor,
                                       String secondaryColor) {
        super(data, limitMonths, addMedian, xAxisLabel, yAxisLabel, primaryColor, secondaryColor);
    }

    @Override
    protected List<XYChart.Series<String, Number>> buildSeries() {
        int lm = limitMonths;
        YearMonth nowYm = YearMonth.from(LocalDate.now());
        YearMonth startYm;

        if (lm == -1) {
            // include all months present in data up to current month
            startYm = data.stream()
                    .map(r -> YearMonth.from(r.getDate()))
                    .min(YearMonth::compareTo)
                    .orElse(nowYm);
        } else {
            if (lm < 1) lm = 1; // safety clamp
            startYm = nowYm.minusMonths(lm - 1);
        }

        // We need the previous month of the first month to compute growth for the first plotted month
        YearMonth windowStart = startYm.minusMonths(1);
        YearMonth endYm = nowYm;

        // Filter to requested window (including one extra month before start for previous-month lookups)
        List<InvestmentsRecord> window = data.stream()
                .filter(r -> {
                    YearMonth ym = YearMonth.from(r.getDate());
                    return !ym.isBefore(windowStart) && !ym.isAfter(endYm);
                })
                .collect(Collectors.toList());

        // For each calendar month, keep the latest entry by id (end-of-month balance)
        Map<YearMonth, InvestmentsRecord> latestPerMonth = window.stream()
                .collect(Collectors.toMap(
                        r -> YearMonth.from(r.getDate()),
                        r -> r,
                        (r1, r2) -> r1.getId() > r2.getId() ? r1 : r2
                ));

        // Build a continuous list of months from startYm to endYm (inclusive)
        List<YearMonth> monthsToPlot = new ArrayList<>();
        for (YearMonth ym = startYm; !ym.isAfter(endYm); ym = ym.plusMonths(1)) {
            monthsToPlot.add(ym);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

        XYChart.Series<String, Number> growthSeries = new XYChart.Series<>();
        growthSeries.setName("Monthly Growth");

        for (YearMonth ym : monthsToPlot) {
            InvestmentsRecord cur = latestPerMonth.get(ym);
            InvestmentsRecord prev = latestPerMonth.get(ym.minusMonths(1));
            if (cur != null && prev != null) {
                double growth = cur.getBalance() - prev.getBalance();
                growthSeries.getData().add(new XYChart.Data<>(ym.format(fmt), growth));
            }
        }

        return List.of(growthSeries);
    }
}
