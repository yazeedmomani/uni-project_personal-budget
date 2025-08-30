package layout.views;

import db.Database;
import db.models.*;
import javafx.scene.control.ScrollPane;
import layout.components.Summary;
import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.debts.DebtsBarChart;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DashboardView {
    private Dashboard dashboard;
    private DashboardCard incomeSummaryCard, savingsSummaryCard, investmentsSummaryCard, monthlyBillsSummaryCard, payablesChartCard, receivablesChartCard;

    private Summary incomeSummary, savingsSummary, investmentsSummary, monthlyBillsSummary;
    private DebtsBarChart payablesChart, receivablesChart;

    private List<IncomeRecord> incomeData;
    private List<SavingsRecord> savingsData;
    private List<InvestmentsRecord> investmentsData;
    private List<DebtsRecord> receivablesData;
    private List<DebtsRecord> payablesData;
    private List<SubscriptionsRecord> subscriptionsData;

    public DashboardView(){
        initializeData();

        String incomeThisMonth = getIncomeThisMonth();
        String savingsBalance = getLastSavingsBalance();
        String investmentsBalance = getLastInvestmentsBalance();
        String totalMonthlyBills = getTotalMonthlyBills();

        incomeSummary = new Summary(incomeThisMonth);
        savingsSummary = new Summary(savingsBalance);
        investmentsSummary = new Summary(investmentsBalance);
        monthlyBillsSummary = new Summary(totalMonthlyBills);
        receivablesChart = new DebtsBarChart(receivablesData, "#02838F", 70);
        payablesChart = new DebtsBarChart(payablesData, "#9FA8DA", 70);


        incomeSummaryCard = new DashboardCard("Income This Month", incomeSummary.getSummary());
        savingsSummaryCard = new DashboardCard("Savings Balance", savingsSummary.getSummary());
        investmentsSummaryCard = new DashboardCard("Investments Balance", investmentsSummary.getSummary());
        monthlyBillsSummaryCard = new DashboardCard("Total Monthly Bills", monthlyBillsSummary.getSummary());
        receivablesChartCard = new DashboardCard("Receivables Distribution", receivablesChart.getChart());
        payablesChartCard = new DashboardCard("Payables Distribution", payablesChart.getChart());


        dashboard = new Dashboard();

        dashboard.add(incomeSummaryCard, 0, 0);
        dashboard.add(monthlyBillsSummaryCard, 1, 0);
        dashboard.add(savingsSummaryCard, 0, 1);
        dashboard.add(investmentsSummaryCard, 1, 1);
        dashboard.add(receivablesChartCard, 0, 2);
        dashboard.add(payablesChartCard, 1, 2);
    }

    public ScrollPane getRoot(){
        return dashboard.getRoot();
    }

    private String getTotalMonthlyBills() {
        double sum = subscriptionsData == null ? 0.0 : subscriptionsData.stream()
                .mapToDouble(SubscriptionsRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }

    private String getLastInvestmentsBalance(){
        double lastBalance;
        try{
            lastBalance = Database.getInvestmentsDAO().getLastBalance();
            return formatJOD(lastBalance);
        }
        catch(Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        return "No Data";
    }

    private String getLastSavingsBalance(){
        double lastBalance;
        try{
            lastBalance = Database.getSavingsDAO().getLastBalance();
            return formatJOD(lastBalance);
        }
        catch(Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        return "No Data";
    }

    private String getIncomeThisMonth(){
        YearMonth current = YearMonth.from(LocalDate.now());
        double sum = incomeData == null ? 0.0 : incomeData.stream()
                .filter(r -> r != null && r.getDate() != null && YearMonth.from(r.getDate()).equals(current))
                .mapToDouble(IncomeRecord::getAmount)
                .sum();
        return formatJOD(sum);
    }

    private void initializeData(){
        try{
            incomeData = Database.getIncomeDAO().getAll();
            savingsData = Database.getSavingsDAO().getAll();
            investmentsData = Database.getInvestmentsDAO().getAll();
            receivablesData = Database.getDebtsDAO().getAll("Credited");
            payablesData = Database.getDebtsDAO().getAll("Received");
            subscriptionsData = Database.getSubscriptionsDAO().getAll();
        }
        catch (Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        if (incomeData == null) incomeData = Collections.emptyList();
        if (savingsData == null) savingsData = Collections.emptyList();
        if (investmentsData == null) investmentsData = Collections.emptyList();
        if (receivablesData == null) receivablesData = Collections.emptyList();
        if (payablesData == null) payablesData = Collections.emptyList();
        if (subscriptionsData == null) subscriptionsData = Collections.emptyList();
    }

    private String formatJOD(double amount){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return "JOD " + nf.format(Math.round(amount));
    }
}
