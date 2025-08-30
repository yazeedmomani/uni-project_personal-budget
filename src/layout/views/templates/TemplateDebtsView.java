package layout.views.templates;

import db.Database;
import db.dao.DebtsDAO;
import db.models.DebtsRecord;
import layout.components.Summary;
import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.debts.DebtsBarChart;
import layout.components.debts.DebtsTable;

import java.util.Collections;

public abstract class TemplateDebtsView extends TemplateView<DebtsRecord, DebtsDAO> {
    private static DashboardCard summaryCard, barChartCard, tableCard;
    private static Summary summary;
    private static DebtsBarChart barChart;
    private static DebtsTable table;

    protected TemplateDebtsView(){
        super(Database.getDebtsDAO());
    }

    @Override
    protected void initializeDashboardCards() {
        String totalReceivables = getTotalReceivables();

        summary = new Summary(totalReceivables);
        barChart = new DebtsBarChart(data, getBarChartColor());
        table = new DebtsTable(data, getNegativeRowsColor());

        summaryCard = new DashboardCard(getSummaryTitle(), summary.getSummary());
        barChartCard = new DashboardCard(getBarChartTitle(), barChart.getChart());
        tableCard = new DashboardCard(getTableTitle(), table.getTable());

        dashboard = new Dashboard();

        dashboard.add(summaryCard, 0, 0, 2, 1);
        dashboard.add(barChartCard, 0, 1, 2, 1);
        dashboard.add(tableCard, 0, 2, 2, 1);
    }

    private String getTotalReceivables() {
        double sum = data == null ? 0.0 : data.stream()
                .filter(r -> r != null && r.getType() != null && r.getType().equals(getType()))
                .mapToDouble(DebtsRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }

    @Override
    protected void initializeData(){
        try{
            data = dao.getAll(getType());
        }
        catch (Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        if (data == null) data = Collections.emptyList();
    }

    protected abstract String getType();
    protected abstract String getBarChartColor();
    protected abstract String getNegativeRowsColor();
    protected abstract String getSummaryTitle();
    protected abstract String getBarChartTitle();
    protected abstract String getTableTitle();
}
