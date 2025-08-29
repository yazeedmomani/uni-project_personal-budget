package layout.views.savings;

import db.Database;
import db.dao.SavingsDAO;
import db.models.SavingsRecord;
import layout.components.Summary;
import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.income.IncomeBarChart;
import layout.components.income.IncomeLineChart;
import layout.components.income.IncomeTable;
import layout.components.savings.SavingsLineChart;
import layout.components.savings.SavingsMonthlyLineChart;
import layout.views.templates.TemplateView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;

public class SavingsView extends TemplateView<SavingsRecord, SavingsDAO> {
    private static DashboardCard leftSummaryCard, rightSummaryCard, barChartCard, lineChartCard, tableCard;
    private static Summary leftSummary, rightSummary;
    private static SavingsLineChart lineChart;
    private static SavingsMonthlyLineChart monthlyLineChart;
    private static IncomeTable table;

    public SavingsView(){
        super(Database.getSavingsDAO());
    }

    @Override
    protected void initializeDashboardCards() {
        String growthThisMonth = getGrowthThisMonth();
        String lastBalance = getLastBalance();

        leftSummary = new Summary(lastBalance);
        rightSummary = new Summary(growthThisMonth);
        lineChart = new SavingsLineChart(data);
        monthlyLineChart = new SavingsMonthlyLineChart(data);
//        table = new IncomeTable(data);

        leftSummaryCard = new DashboardCard("Balance", leftSummary.getSummary());
        rightSummaryCard = new DashboardCard("Growth This Month", rightSummary.getSummary());
        barChartCard = new DashboardCard("Balance Over This Month", lineChart.getChart());
        lineChartCard = new DashboardCard("Balance per Month (Last 6 Months)", monthlyLineChart.getChart());
//        tableCard = new DashboardCard("Income Details", table.getTable());

        dashboard = new Dashboard();

        dashboard.add(leftSummaryCard, 0, 0);
        dashboard.add(rightSummaryCard, 1, 0);
        dashboard.add(barChartCard, 0, 1);
        dashboard.add(lineChartCard, 1, 1);
//        dashboard.add(tableCard, 0, 2, 2, 1);
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
        YearMonth current = YearMonth.from(LocalDate.now());
        YearMonth previous = current.minusMonths(1);

        double lastBalanceThisMonth = 0.0;
        double lastBalancePrevMonth = 0.0;

        if (data != null && !data.isEmpty()) {
            lastBalanceThisMonth = data.stream()
                    .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(current))
                    .max(Comparator.comparingInt(SavingsRecord::getId))
                    .map(SavingsRecord::getBalance)
                    .orElse(0.0);

            lastBalancePrevMonth = data.stream()
                    .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(previous))
                    .max(Comparator.comparingInt(SavingsRecord::getId))
                    .map(SavingsRecord::getBalance)
                    .orElse(0.0);
        }

        double growth = lastBalanceThisMonth - lastBalancePrevMonth;
        return formatJOD(growth);
    }
}
