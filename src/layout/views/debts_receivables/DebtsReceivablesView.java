package layout.views.debts_receivables;

import layout.views.templates.TemplateDebtsView;

public class DebtsReceivablesView extends TemplateDebtsView {
    protected String getType(){return "Credited";}
    protected String getBarChartColor() {return "#1664C0";}
    protected String getNegativeRowsColor() {return "#BBDEFB";}
    protected String getSummaryTitle() {return "Total Debts to Collect";}
    protected String getBarChartTitle() {return "Receivables Distribution";}
    protected String getTableTitle() {return "Receivables Transactions";}
}
