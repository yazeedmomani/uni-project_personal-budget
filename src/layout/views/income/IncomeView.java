package layout.views.income;

import db.Database;
import db.dao.IncomeDAO;
import db.models.IncomeRecord;
import java.time.*;
import java.util.*;
import javafx.scene.control.ScrollPane;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.Summary;
import layout.components.income.IncomeBarChart;
import layout.components.income.IncomeLineChart;
import layout.components.income.IncomeTable;
import layout.views.TemplateView;

import java.text.NumberFormat;

public class IncomeView extends TemplateView<IncomeRecord, IncomeDAO> {
    private static DashboardCard summary1, summary2, barChart, lineChart, table;

    public IncomeView(){
        super(Database.getIncomeDAO());
    }

    @Override
    protected void initializeDashboardCards() {
        String incomeThisMonth = getIncomeThisMonth();
        String incomeLastMonth = getIncomeLastMonth();

        summary1 = new DashboardCard("Income This Month", new Summary(incomeThisMonth).getNode());
        summary2 = new DashboardCard("Income Last Month", new Summary(incomeLastMonth).getNode());
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

    private String formatJOD(double amount){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return "JOD " + nf.format(Math.round(amount));
    }
}
