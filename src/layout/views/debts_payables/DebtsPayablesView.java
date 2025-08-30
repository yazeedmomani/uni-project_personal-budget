package layout.views.debts_payables;

import layout.views.templates.TemplateDebtsView;

public class DebtsPayablesView extends TemplateDebtsView {
    protected String getType(){return "Received";}
    protected String getBarChartColor() {return "#B71C1C";}
    protected String getNegativeRowsColor() {return "#F4C7C2";}
    protected String getSummaryTitle() {return "Total Debts to Pay";}
    protected String getBarChartTitle() {return "Payables Distribution";}
    protected String getTableTitle() {return "Payables Transactions";}
}
