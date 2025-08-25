package layout.views.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.ScrollPane;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.income.IncomeBarChart;

public class IncomeView {
    private static Dashboard dashboard;
    private static DashboardCard summary1, summary2, barChart, lineChart, table;
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
        chart.setCategoryGap(70);
        chart.setBarGap(6);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        sums.forEach((source, total) -> series.getData().add(new XYChart.Data<>(source, total)));

        chart.getData().add(series);
        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setPrefHeight(320);
        return chart;
    }

    private static TableView<IncomeRecord> buildTableSkeleton() {
        TableView<IncomeRecord> tv = new TableView<>();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<IncomeRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> {
            LocalDate d = cell.getValue().getDate();
            return new ReadOnlyStringWrapper(d != null ? d.toString() : "");
        });

        TableColumn<IncomeRecord, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getSource() != null ? cell.getValue().getSource() : ""
        ));

        TableColumn<IncomeRecord, String> amountCol = new TableColumn<>("Amount (JOD)");
        amountCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                String.format("% .2f", cell.getValue().getAmount())
        ));

        TableColumn<IncomeRecord, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getNotes() != null ? cell.getValue().getNotes() : ""
        ));

        tv.getColumns().setAll(dateCol, sourceCol, amountCol, notesCol);
        tv.setMaxWidth(Double.MAX_VALUE);
        tv.setPrefHeight(388);
        return tv;
    }

    private static Pagination buildPaginatedIncomeTable() {
        final int ROWS_PER_PAGE = 15;

        // Ensure data is loaded
        if (data == null) {
            try {
                data = Database.getIncomeDAO().getAll();
            } catch (Exception e) {
                System.out.println("IncomeView Error: " + e.getMessage());
            }
            if (data == null) data = Collections.emptyList();
        }

        TableView<IncomeRecord> table = buildTableSkeleton();
        table.setMinHeight(388);
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);

        int pageCount = Math.max(1, (int) Math.ceil(data.size() / (double) ROWS_PER_PAGE));
        Pagination pagination = new Pagination(pageCount, 0);
        pagination.setPrefHeight(420);
        VBox.setVgrow(pagination, Priority.ALWAYS);
        pagination.setMaxWidth(Double.MAX_VALUE);

        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
            if (fromIndex > toIndex) {
                fromIndex = 0;
                toIndex = Math.min(ROWS_PER_PAGE, data.size());
            }
            ObservableList<IncomeRecord> page = FXCollections.observableArrayList(
                    data.subList(fromIndex, toIndex)
            );
            table.setItems(page);
            return table;
        });

        return pagination;
    }

    private static LineChart<String, Number> buildIncomePerMonthLineChart() {
        // Ensure data is loaded (reuse same data source as bar chart)
        if (data == null) {
            try {
                data = Database.getIncomeDAO().getAll();
            } catch (Exception e) {
                System.out.println("IncomeView Error: " + e.getMessage());
            }
            if (data == null) data = Collections.emptyList();
        }

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

    public static ScrollPane getRoot(){
        initializeData();

        summary1 = createSummaryCard();
        summary2 = createSummaryCard();
        barChart = createBarChart();
        lineChart = createLineChart();
        table = createTable();

        dashboard = new Dashboard();

        dashboard.add(summary1, 0, 0);
        dashboard.add(summary2, 1, 0);
        dashboard.add(barChart, 0, 1);
        dashboard.add(lineChart, 1, 1);
        dashboard.add(table, 0, 2, 2, 1);

        return dashboard.getRoot();
    }

    private static DashboardCard createTable(){
        Pagination table = buildPaginatedIncomeTable();

        return new DashboardCard(table);
    }

    private static DashboardCard createLineChart(){
        Label title = new Label("Total Income per Month (Last 6 Months)");
        title.getStyleClass().add("summaryLabel");
        LineChart<String, Number> chart = buildIncomePerMonthLineChart();

        return new DashboardCard(title, chart);
    }

    private static DashboardCard createBarChart(){
        Label title = new Label("Income by Source (Last 6 Months)");
        title.getStyleClass().add("summaryLabel");

        return new DashboardCard(title, IncomeBarChart.init(data));
    }

    private static DashboardCard createSummaryCard(){
        Label label = new Label("Income This Month");
        label.getStyleClass().add("summaryLabel");
        Label value = new Label("JOD 240.00");
        value.getStyleClass().add("summaryContent");

        return new DashboardCard(label, value);
    }

    private static void initializeData(){
        try{
            data = Database.getIncomeDAO().getAll();
        }
        catch (Exception e){
            System.out.println("IncomeView Error: " + e.getMessage());
        }
        if (data == null) data = Collections.emptyList();
    }
}
