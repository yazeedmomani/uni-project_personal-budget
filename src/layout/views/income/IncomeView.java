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
import layout.components.income.IncomeLineChart;

public class IncomeView {
    private static Dashboard dashboard;
    private static DashboardCard summary1, summary2, barChart, lineChart, table;
    private static List<IncomeRecord> data;

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

        return new DashboardCard(title, IncomeLineChart.init(data));
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
