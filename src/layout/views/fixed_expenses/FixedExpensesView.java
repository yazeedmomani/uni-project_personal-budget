package layout.views.fixed_expenses;

import db.Database;
import db.dao.SubscriptionsDAO;
import db.models.SubscriptionsRecord;
import layout.components.Summary;
import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.fixed_expenses.FixedExpensesBarChart;
import layout.components.fixed_expenses.FixedExpensesTable;
import layout.views.templates.TemplateView;

import java.time.LocalDate;
import java.time.YearMonth;

public class FixedExpensesView extends TemplateView<SubscriptionsRecord, SubscriptionsDAO> {
    private static DashboardCard summaryCard, barChartCard, tableCard;
    private static Summary summary;
    private static FixedExpensesBarChart barChart;
    private static FixedExpensesTable table;

    public FixedExpensesView(){
        super(Database.getSubscriptionsDAO());
    }

    @Override
    protected void initializeDashboardCards() {
        String totalMonthlyBill = getTotalMonthlyBills();

        summary = new Summary(totalMonthlyBill);
        barChart = new FixedExpensesBarChart(data);
        table = new FixedExpensesTable(data);

        summaryCard = new DashboardCard("Total Monthly Bills", summary.getSummary());
        barChartCard = new DashboardCard("Monthly Bills Distribution", barChart.getChart());
        tableCard = new DashboardCard("Monthly Bills Breakdown", table.getTable());

        dashboard = new Dashboard();

        dashboard.add(summaryCard, 0, 0, 2, 1);
        dashboard.add(barChartCard, 0, 1, 2, 1);
        dashboard.add(tableCard, 0, 2, 2, 1);
    }

    private String getTotalMonthlyBills() {
        double sum = data == null ? 0.0 : data.stream()
                .mapToDouble(SubscriptionsRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }
}
