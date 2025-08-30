package layout.components.fixed_expenses;

import db.models.SubscriptionsRecord;
import javafx.scene.chart.XYChart;
import layout.components.templates.TemplateBarChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FixedExpensesBarChart extends TemplateBarChart<SubscriptionsRecord> {

    public FixedExpensesBarChart(List<SubscriptionsRecord> data) {
        super(data, 6,"Bill", "Total (JOD)", "#E64A19", 175, false);
    }

    @Override
    protected List<XYChart.Series<String, Number>> buildSeries() {
        Map<String, Double> sums = data.stream()
                .collect(Collectors.groupingBy(SubscriptionsRecord::getSubscription, Collectors.summingDouble(SubscriptionsRecord::getAmount)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> series.getData().add(new XYChart.Data<>(source, total)));

        series.setName("Bill");
        return List.of(series);
    }
}
