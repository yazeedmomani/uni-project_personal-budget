package layout.views.income;

import db.Database;
import db.dao.IncomeDAO;
import db.models.IncomeRecord;
import java.time.*;
import java.util.*;

import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.Summary;
import layout.components.income.IncomeBarChart;
import layout.components.income.IncomeLineChart;
import layout.components.income.IncomeTable;
import layout.views.templates.TemplateView;

import java.text.NumberFormat;

public class IncomeView extends TemplateView<IncomeRecord, IncomeDAO> {
    private static DashboardCard leftSummaryCard, rightSummaryCard, barChartCard, lineChartCard, tableCard;
    private static Summary leftSummary, rightSummary;
    private static IncomeBarChart barChart;
    private static IncomeLineChart lineChart;
    private static IncomeTable table;

    public IncomeView(){
        super(Database.getIncomeDAO());
    }

    @Override
    protected void initializeDashboardCards() {
        String incomeThisMonth = getIncomeThisMonth();
        String incomeLastMonth = getIncomeLastMonth();

        leftSummary = new Summary(incomeThisMonth);
        rightSummary = new Summary(incomeLastMonth);
        barChart = new IncomeBarChart(data);
        lineChart = new IncomeLineChart(data);
        table = new IncomeTable(data);

        leftSummaryCard = new DashboardCard("Income This Month", leftSummary.getSummary());
        rightSummaryCard = new DashboardCard("Income Last Month", rightSummary.getSummary());
        barChartCard = new DashboardCard("Income by Source (Last 6 Months)", barChart.getChart());
        lineChartCard = new DashboardCard("Total Income per Month (Last 6 Months)", lineChart.getChart());
        tableCard = new DashboardCard("Income Details", table.getTable());

        dashboard = new Dashboard();

        dashboard.add(leftSummaryCard, 0, 0);
        dashboard.add(rightSummaryCard, 1, 0);
        dashboard.add(barChartCard, 0, 1);
        dashboard.add(lineChartCard, 1, 1);
        dashboard.add(tableCard, 0, 2, 2, 1);
    }

    private String getIncomeThisMonth(){
        YearMonth current = YearMonth.from(LocalDate.now());
        double sum = data == null ? 0.0 : data.stream()
                .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(current))
                .mapToDouble(IncomeRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }

    private String getIncomeLastMonth(){
        YearMonth last = YearMonth.from(LocalDate.now()).minusMonths(1);
        double sum = data == null ? 0.0 : data.stream()
                .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(last))
                .mapToDouble(IncomeRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }
}
