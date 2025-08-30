package layout.components.investments;

import db.models.InvestmentsRecord;
import javafx.scene.chart.XYChart;
import layout.components.templates.TemplateLineChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvestmentsMonthlyLineChart extends TemplateLineChart<InvestmentsRecord> {
    public InvestmentsMonthlyLineChart(List<InvestmentsRecord> data) {
        super(data, 6, true, "Month", "Total (JOD)", "#04ABC1", "#4ED0E1");
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

        YearMonth endYm = nowYm;

        // Filter to requested month window
        List<InvestmentsRecord> window = data.stream()
                .filter(r -> {
                    YearMonth ym = YearMonth.from(r.getDate());
                    return !ym.isBefore(startYm) && !ym.isAfter(endYm);
                })
                .collect(Collectors.toList());

        // For each calendar month, keep the latest entry by id (so we reflect end-of-month balance)
        Map<YearMonth, InvestmentsRecord> latestPerMonth = window.stream()
                .collect(Collectors.toMap(
                        r -> YearMonth.from(r.getDate()),
                        r -> r,
                        (r1, r2) -> r1.getId() > r2.getId() ? r1 : r2
                ));

        // Sort months ascending for the x-axis
        List<YearMonth> months = latestPerMonth.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

        XYChart.Series<String, Number> balanceSeries = new XYChart.Series<>();
        balanceSeries.setName("Monthly Balance");

        for (YearMonth ym : months) {
            InvestmentsRecord rec = latestPerMonth.get(ym);
            double balance = rec.getBalance();
            balanceSeries.getData().add(new XYChart.Data<>(ym.format(fmt), balance));
        }

        return List.of(balanceSeries);
    }
}
