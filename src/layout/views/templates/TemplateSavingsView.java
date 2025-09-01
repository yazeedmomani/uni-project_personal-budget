package layout.views.templates;

import db.dao.templates.TemplateSavingsDAO;
import db.models.templates.TemplateSavingsRecord;
import layout.components.Summary;
import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.savings.SavingsLineChart;
import layout.components.savings.SavingsMonthlyLineChart;
import layout.components.savings.SavingsTable;
import layout.components.templates.TemplateLineChart;
import layout.components.templates.TemplateTable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;

public abstract class TemplateSavingsView<Record extends TemplateSavingsRecord, DAO extends TemplateSavingsDAO<Record>, LineChart extends TemplateLineChart, MonthlyLineChart extends TemplateLineChart, Table extends TemplateTable> extends TemplateView<Record, DAO> {
    private DashboardCard leftSummaryCard, rightSummaryCard, barChartCard, lineChartCard, tableCard;
    private Summary leftSummary, rightSummary;
    private LineChart lineChart;
    private MonthlyLineChart monthlyLineChart;
    private Table table;

    public TemplateSavingsView(DAO dao){
        super(dao);
    }

    protected abstract String getTableTitle();

    @Override
    protected void initializeDashboardCards() {
        String growthThisMonth = getGrowthThisMonth();
        String lastBalance = getLastBalance();

        leftSummary = new Summary(lastBalance);
        rightSummary = new Summary(growthThisMonth);
        lineChart = createLineChart();
        monthlyLineChart = createMonthlyLineChart();
        table = createTable();

        leftSummaryCard = new DashboardCard("Balance", leftSummary.getSummary());
        rightSummaryCard = new DashboardCard("Monthly Growth", rightSummary.getSummary());
        barChartCard = new DashboardCard("Balance Trend (This Month)", lineChart.getChart());
        lineChartCard = new DashboardCard("Monthly Growth (Last 6 Months)", monthlyLineChart.getChart());
        tableCard = new DashboardCard(getTableTitle(), table.getTable());

        dashboard = new Dashboard();

        dashboard.add(leftSummaryCard, 0, 0);
        dashboard.add(rightSummaryCard, 1, 0);
        dashboard.add(barChartCard, 0, 1);
        dashboard.add(lineChartCard, 1, 1);
        dashboard.add(tableCard, 0, 2, 2, 1);
    }

    private String getLastBalance(){
        double lastBalance;
        try{
            lastBalance = dao.getLastBalance();
            return formatJOD(lastBalance);
        }
        catch(Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        return "No Data";
    }

    private String getGrowthThisMonth(){
        try {
            if (data == null || data.isEmpty()) {
                return "No Data";
            }

            YearMonth current = YearMonth.from(LocalDate.now());
            YearMonth last = YearMonth.from(LocalDate.now()).minusMonths(1);
            double lastBalance = dao.getLastBalance();

            // 2) Get the last balance (by last id) within the reference month and previous month
            double lastBalanceThisMonth = data.stream()
                    .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(current))
                    .max(Comparator.comparingInt(Record::getId))
                    .map(Record::getBalance)
                    .orElse(lastBalance);

            double lastBalancePrevMonth = data.stream()
                    .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(last))
                    .max(Comparator.comparingInt(Record::getId))
                    .map(Record::getBalance)
                    .orElse(0.0);

            double growth = lastBalanceThisMonth - lastBalancePrevMonth;
            return formatJOD(growth);
        } catch (Exception e) {
            System.out.println("View Error: " + e.getMessage());
            return "No Data";
        }
    }

    protected abstract LineChart createLineChart();
    protected abstract MonthlyLineChart createMonthlyLineChart();
    protected abstract Table createTable();
}
