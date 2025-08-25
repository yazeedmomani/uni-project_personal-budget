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
        DashboardCard card = new DashboardCard(IncomeTable.init(data));
        card.setTitle("Income Details");

        return card;
    }

    private static DashboardCard createLineChart(){
        DashboardCard card =  new DashboardCard(IncomeLineChart.init(data));
        card.setTitle("Total Income per Month (Last 6 Months)");

        return card;
    }

    private static DashboardCard createBarChart(){
        DashboardCard card = new DashboardCard(IncomeBarChart.init(data));
        card.setTitle("Income by Source (Last 6 Months)");

        return card;
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
