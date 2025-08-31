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

            // 1) Pick a reference month: use the real current month if there is data for it;
            //    otherwise fall back to the latest month that actually exists in the data.
            YearMonth calendarCurrent = YearMonth.from(LocalDate.now());
            boolean hasCalendarCurrent = data.stream()
                    .filter(r -> r != null && r.getDate() != null)
                    .anyMatch(r -> YearMonth.from(r.getDate()).equals(calendarCurrent));

            YearMonth refMonth = hasCalendarCurrent
                    ? calendarCurrent
                    : data.stream()
                        .filter(r -> r != null && r.getDate() != null)
                        .max(Comparator.comparing(Record::getDate))
                        .map(r -> YearMonth.from(r.getDate()))
                        .orElse(calendarCurrent);

            YearMonth prevMonth = refMonth.minusMonths(1);

            // 2) Get the last balance (by last id) within the reference month and previous month
            double lastBalanceThisMonth = data.stream()
                    .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(refMonth))
                    .max(Comparator.comparingInt(Record::getId))
                    .map(Record::getBalance)
                    .orElse(0.0);

            double lastBalancePrevMonth = data.stream()
                    .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(prevMonth))
                    .max(Comparator.comparingInt(Record::getId))
                    .map(Record::getBalance)
                    .orElse(0.0);

            System.out.println("refMonth=" + refMonth + " prevMonth=" + prevMonth);
            System.out.println("this= " + lastBalanceThisMonth + " prev= " + lastBalancePrevMonth);

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
