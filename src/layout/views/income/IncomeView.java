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
import layout.components.income.IncomeTable;

public class IncomeView {
    private static Dashboard dashboard;
    private static DashboardCard summary1, summary2, barChart, lineChart, table;
    private static List<IncomeRecord> data;

    public static ScrollPane getRoot(){
        initializeData();
        initializeDashboard();

        return dashboard.getRoot();
    }

    private static void initializeDashboard(){
        summary1 = createSummaryCard();
        summary2 = createSummaryCard();
        barChart = new DashboardCard("Income by Source (Last 6 Months)", IncomeBarChart.init(data));
        lineChart = new DashboardCard("Total Income per Month (Last 6 Months)", IncomeLineChart.init(data));
        table = new DashboardCard("Income Details", IncomeTable.init(data));

        dashboard = new Dashboard();

        dashboard.add(summary1, 0, 0);
        dashboard.add(summary2, 1, 0);
        dashboard.add(barChart, 0, 1);
        dashboard.add(lineChart, 1, 1);
        dashboard.add(table, 0, 2, 2, 1);
    }

    private static DashboardCard createSummaryCard(){
        Label value = new Label("JOD 240.00");
        value.getStyleClass().add("summaryContent");

        DashboardCard card =  new DashboardCard(value);
        card.setTitle("Income This Month");

        return card;
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
