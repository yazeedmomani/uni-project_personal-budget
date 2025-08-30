package layout.components.savings;

import db.models.IncomeRecord;
import db.models.SavingsRecord;
import javafx.scene.chart.XYChart;
import layout.components.templates.TemplateLineChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SavingsLineChart extends TemplateLineChart<SavingsRecord> {
    public SavingsLineChart(List<SavingsRecord> data) {
        super(data, 1, true, "Date", "Total (JOD)", "#FF8F00", "#FFC007");
    }

    public SavingsLineChart(List<SavingsRecord> data,
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
        YearMonth endYm = nowYm;

        if (lm == -1) {
            // include all dates present in data up to current month
            startYm = data.stream()
                    .map(r -> YearMonth.from(r.getDate()))
                    .min(YearMonth::compareTo)
                    .orElse(nowYm);
        } else {
            if (lm < 1) lm = 1; // safety clamp
            startYm = nowYm.minusMonths(lm - 1);
        }

        LocalDate startDate = startYm.atDay(1);
        LocalDate endDate = endYm.atEndOfMonth();

        // Filter to requested window
        List<SavingsRecord> window = data.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.toList());

        // For each calendar date, keep the latest entry by id (so we reflect end-of-day balance)
        Map<LocalDate, SavingsRecord> latestPerDate = window.stream()
                .collect(Collectors.toMap(
                        SavingsRecord::getDate,
                        r -> r,
                        (r1, r2) -> r1.getId() > r2.getId() ? r1 : r2
                ));

        // Sort dates ascending for the x-axis
        List<LocalDate> dates = latestPerDate.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        XYChart.Series<String, Number> balanceSeries = new XYChart.Series<>();
        balanceSeries.setName("Daily Balance");

        for (LocalDate d : dates) {
            SavingsRecord rec = latestPerDate.get(d);
            double balance = rec.getBalance();
            balanceSeries.getData().add(new XYChart.Data<>(d.format(fmt), balance));
        }

        return List.of(balanceSeries);
    }
}
