package layout.views.debts_receivables;

import db.Database;
import db.dao.DebtsDAO;
import db.models.DebtsRecord;
import layout.components.Summary;
import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.debts.DebtsBarChart;
import layout.components.debts.DebtsTable;
import layout.views.templates.TemplateView;

import java.util.Collections;

public class DebtsReceivablesView extends TemplateView<DebtsRecord, DebtsDAO> {
    private static DashboardCard summaryCard, barChartCard, tableCard;
    private static Summary summary;
    private static DebtsBarChart barChart;
    private static DebtsTable table;
    private final static String DEBT_TYPE = "Credited";

    public DebtsReceivablesView(){
        super(Database.getDebtsDAO());
    }

    @Override
    protected void initializeDashboardCards() {
        String totalReceivables = getTotalReceivables();

        summary = new Summary(totalReceivables);
        barChart = new DebtsBarChart(data, "#3849AB");
        table = new DebtsTable(data);

        summaryCard = new DashboardCard("Total Debts to Collect", summary.getSummary());
        barChartCard = new DashboardCard("Receivables Distribution", barChart.getChart());
        tableCard = new DashboardCard("Receivables Transactions", table.getTable());

        dashboard = new Dashboard();

        dashboard.add(summaryCard, 0, 0, 2, 1);
        dashboard.add(barChartCard, 0, 1, 2, 1);
        dashboard.add(tableCard, 0, 2, 2, 1);
    }

    private String getTotalReceivables() {
        double sum = data == null ? 0.0 : data.stream()
                .filter(r -> r != null && r.getType() != null && r.getType().equals(DEBT_TYPE))
                .mapToDouble(DebtsRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }

    @Override
    protected void initializeData(){
        try{
            data = dao.getAll(DEBT_TYPE);
        }
        catch (Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        if (data == null) data = Collections.emptyList();
    }
}
