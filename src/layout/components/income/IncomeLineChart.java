package layout.components.income;

import db.models.IncomeRecord;
import javafx.scene.chart.XYChart;
import layout.components.TemplateLineChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeLineChart extends TemplateLineChart<IncomeRecord> {

    public IncomeLineChart(List<IncomeRecord> data) {
        super(data, true, "Month", "Total (JOD)", "#388E3C", "#C8E6C9");
    }

    @Override
    protected List<XYChart.Series<String, Number>> buildSeries() {
        // Define month window: last 6 months including current
        YearMonth startYm = YearMonth.from(LocalDate.now()).minusMonths(5);
        YearMonth endYm = YearMonth.from(LocalDate.now());
        LocalDate startDate = startYm.atDay(1);
        LocalDate endDate = endYm.atEndOfMonth();

        // Group by YearMonth and sum amounts
        Map<YearMonth, Double> monthlyTotals = data.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(r -> YearMonth.from(r.getDate()),
                        Collectors.summingDouble(IncomeRecord::getAmount)));

        // Build ordered categories and values for the 6 months, filling missing ones with 0
        List<YearMonth> months = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            months.add(startYm.plusMonths(i));
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
