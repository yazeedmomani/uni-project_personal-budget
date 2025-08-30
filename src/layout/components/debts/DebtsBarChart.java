package layout.components.debts;

import db.models.DebtsRecord;
import javafx.scene.chart.XYChart;
import layout.components.templates.TemplateBarChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DebtsBarChart extends TemplateBarChart<DebtsRecord> {

    public DebtsBarChart(List<DebtsRecord> data, String color) {
        super(data, 6,"Party", "Amount (JOD)", color, 175, false);
    }

    public DebtsBarChart(List<DebtsRecord> data, String color, int barGap) {
        super(data, 6,"Party", "Amount (JOD)", color, barGap, false);
    }

    @Override
    protected List<XYChart.Series<String, Number>> buildSeries() {
        Map<String, Double> sums = data.stream()
                .collect(Collectors.groupingBy(DebtsRecord::getParty, Collectors.summingDouble(DebtsRecord::getAmount)));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> {
            if (total != 0) {
                series.getData().add(new XYChart.Data<>(source, total));
            }
        });

        series.setName("Debts");
        return List.of(series);
    }
}
