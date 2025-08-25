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

public class IncomeView {
    private static GridPane root;
    private static VBox summaryCard1, summaryCard2, barChart, lineChart;
    private static List<IncomeRecord> data;
    private static Pagination pagination;
    private static TableView<IncomeRecord> table;
    private static final int ROWS_PER_PAGE = 10;

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
        tv.setPrefHeight(360);
        return tv;
    }

    private static VBox buildPaginatedIncomeTable() {
        // Ensure data is loaded
        if (data == null) {
            try {
                data = Database.getIncomeDAO().getAll();
            } catch (Exception e) {
                System.out.println("IncomeView Error: " + e.getMessage());
            }
            if (data == null) data = Collections.emptyList();
        }

        table = buildTableSkeleton();
        table.setMinHeight(360);
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);

        int pageCount = Math.max(1, (int) Math.ceil(data.size() / (double) ROWS_PER_PAGE));
        pagination = new Pagination(pageCount, 0);
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

        VBox container = new VBox(pagination);
        VBox.setVgrow(container, Priority.ALWAYS);
        container.getStyleClass().add("dashboardCard");
        container.setMaxWidth(Double.MAX_VALUE);
        return container;
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
        summaryCard1 = createSummaryCard();
        summaryCard2 = createSummaryCard();
        barChart = createBarChart();
        lineChart = createLineChart();




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

        RowConstraints r0 = new RowConstraints();
        RowConstraints r1 = new RowConstraints();
        RowConstraints r2 = new RowConstraints();
        r2.setVgrow(Priority.ALWAYS);
        root.getRowConstraints().setAll(r0, r1, r2);

        VBox tableCard = buildPaginatedIncomeTable();
        tableCard.setMinHeight(360);
        tableCard.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Each card in its own column
        root.add(summaryCard1, 0, 0);
        root.add(summaryCard2, 1, 0);
        root.add(barChart, 0, 1);
        root.add(lineChart, 1, 1);
        // Place the table under the two charts, spanning both columns
        root.add(tableCard, 0, 2, 2, 1);
        GridPane.setHgrow(tableCard, Priority.ALWAYS);
        root.getStyleClass().add("incomeView");
        root.setVgap(24);
        root.setHgap(24);

        GridPane.setHgrow(summaryCard1, Priority.ALWAYS);
        GridPane.setHgrow(summaryCard2, Priority.ALWAYS);
        GridPane.setHgrow(barChart, Priority.ALWAYS);
        GridPane.setHgrow(lineChart, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        // Let content exceed viewport height so charts keep their preferred height
        scrollPane.setFitToHeight(false);
        return scrollPane;
    }

    private static VBox createLineChart(){
        Label title = new Label("Total Income per Month (Last 6 Months)");
        title.getStyleClass().add("summaryLabel");
        LineChart<String, Number> chart = buildIncomePerMonthLineChart();
        VBox root = new VBox(title, chart);
        root.getStyleClass().add("dashboardCard");
        root.setMaxWidth(Double.MAX_VALUE);
        return root;
    }

    private static VBox createBarChart(){
        Label title = new Label("Income by Source (Last 6 Months)");
        title.getStyleClass().add("summaryLabel");

        BarChart<String, Number> chart = buildIncomeBySourceChart();

        VBox root = new VBox(title, chart);
        root.getStyleClass().add("dashboardCard");
        root.setMaxWidth(Double.MAX_VALUE);
        return root;
    }

    private static VBox createSummaryCard(){
        Label label = new Label("Income This Month");
        label.getStyleClass().add("summaryLabel");
        Label value = new Label("JOD 240.00");
        value.getStyleClass().add("summaryContent");
        VBox root = new VBox(label, value);
        root.getStyleClass().add("dashboardCard");
        root.setMaxWidth(Double.MAX_VALUE);
        return root;
    }
}
