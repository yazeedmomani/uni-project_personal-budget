package layout.views.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class IncomeView {
    private static GridPane root;
    private static Label summaryLabel1, summaryLabel2;
    private static Label summaryContent1, summaryContent2;
    private static VBox dashboardCard1, dashboardCard2;
    private static List<IncomeRecord> data;

    private static BarChart<String, Number> buildIncomeBySourceChart() {
        // Mock data
        try{
            data = Database.getIncomeDAO().getAll();
        }
        catch (Exception e){
            System.out.println("IncomeView Error: " + e.getMessage());
        }
        if (data == null) data = Collections.emptyList();

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

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(true);
        chart.setCategoryGap(20);
        chart.setBarGap(6);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> series.getData().add(new XYChart.Data<>(source, total)));

        chart.getData().add(series);
        chart.setMaxWidth(Double.MAX_VALUE);
        return chart;
    }

    public static GridPane getRoot(){
        summaryLabel1 = new Label("Income This Month");
        summaryLabel1.getStyleClass().add("summaryLabel");
        summaryContent1 = new Label("JOD 240.00");
        summaryContent1.getStyleClass().add("summaryContent");

        summaryLabel2 = new Label("Income Last Month");
        summaryLabel2.getStyleClass().add("summaryLabel");
        summaryContent2 = new Label("JOD 256.00");
        summaryContent2.getStyleClass().add("summaryContent");

        dashboardCard1 = new VBox(summaryLabel1, summaryContent1);
        dashboardCard1.getStyleClass().add("dashboardCard");
        dashboardCard2 = new VBox(summaryLabel2, summaryContent2);
        dashboardCard2.getStyleClass().add("dashboardCard");

        Label barTitle = new Label("Income by Source (Last 6 Months)");
        barTitle.getStyleClass().add("summaryLabel");
        BarChart<String, Number> incomeChart = buildIncomeBySourceChart();
        VBox barChartCard = new VBox(barTitle, incomeChart);
        barChartCard.getStyleClass().add("dashboardCard");
        barChartCard.setMaxWidth(Double.MAX_VALUE);

        root = new GridPane();
        // Add two columns, 50% width each, fill width
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        c1.setHgrow(Priority.ALWAYS);
        c1.setFillWidth(true);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        c2.setHgrow(Priority.ALWAYS);
        c2.setFillWidth(true);
        root.getColumnConstraints().setAll(c1, c2);

        // Ensure cards expand horizontally
        dashboardCard1.setMaxWidth(Double.MAX_VALUE);
        dashboardCard2.setMaxWidth(Double.MAX_VALUE);

        // Each card in its own column
        root.add(dashboardCard1, 0, 0);
        root.add(dashboardCard2, 1, 0);
        root.add(barChartCard, 0, 1);
        root.getStyleClass().add("incomeView");
        root.setVgap(24);
        root.setHgap(24);

        GridPane.setHgrow(dashboardCard1, Priority.ALWAYS);
        GridPane.setHgrow(dashboardCard2, Priority.ALWAYS);
        GridPane.setHgrow(barChartCard, Priority.ALWAYS);

        return root;
    }
}
