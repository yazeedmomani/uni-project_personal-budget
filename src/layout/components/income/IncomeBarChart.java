package layout.components.income;

import db.models.IncomeRecord;
import javafx.scene.chart.XYChart;
import layout.components.TemplateBarChart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeBarChart extends TemplateBarChart<IncomeRecord> {

    public IncomeBarChart(List<IncomeRecord> data) {
        super(data, 6,"Source", "Total (JOD)", "#388E3C");
    }

    @Override
    protected List<XYChart.Series<String, Number>> buildSeries() {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now(); // inclusive end today
        if (limitMonths == -1) {
            startDate = LocalDate.MIN; // include all dates
        } else {
            YearMonth start = YearMonth.from(LocalDate.now()).minusMonths(limitMonths - 1);
            startDate = start.atDay(1);
        }

        Map<String, Double> sums = data.stream()
                .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(IncomeRecord::getSource, Collectors.summingDouble(IncomeRecord::getAmount)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> series.getData().add(new XYChart.Data<>(source, total)));

        series.setName("Source");
        return List.of(series);
    }
}
