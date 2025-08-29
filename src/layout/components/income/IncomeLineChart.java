package layout.components.income;

import db.models.IncomeRecord;
import javafx.scene.chart.XYChart;
import layout.components.templates.TemplateLineChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeLineChart extends TemplateLineChart<IncomeRecord> {

    public IncomeLineChart(List<IncomeRecord> data) {
        super(data, 6, true, "Month", "Total (JOD)", "#388E3C", "#C8E6C9");
    }

    public IncomeLineChart(List<IncomeRecord> data,
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
            // include all months present in data up to current month
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

        Map<YearMonth, Double> monthlyTotals = data.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(r -> YearMonth.from(r.getDate()),
                        Collectors.summingDouble(IncomeRecord::getAmount)));

        List<YearMonth> months = new ArrayList<>();
        for (YearMonth ym = startYm; !ym.isAfter(endYm); ym = ym.plusMonths(1)) {
            months.add(ym);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yyyy");
        XYChart.Series<String, Number> totalsSeries = new XYChart.Series<>();
        totalsSeries.setName("Monthly Total");
        for (YearMonth ym : months) {
            double total = monthlyTotals.getOrDefault(ym, 0.0);
            String label = ym.format(fmt);
            totalsSeries.getData().add(new XYChart.Data<>(label, total));
        }

        return List.of(totalsSeries);
    }
}
