package layout.views.investments;

import db.Database;
import db.dao.SavingsDAO;
import db.models.SavingsRecord;
import layout.components.savings.SavingsLineChart;
import layout.components.savings.SavingsMonthlyLineChart;
import layout.components.savings.SavingsTable;
import layout.views.templates.TemplateSavingsView;

public class InvestmentsView extends TemplateSavingsView<SavingsRecord, SavingsDAO> {
    public InvestmentsView(){
        super(Database.getSavingsDAO());
    }

    @Override
    protected SavingsLineChart createLineChart(){
        return new SavingsLineChart(data);
    }

    @Override
    protected SavingsMonthlyLineChart createMonthlyLineChart(){
        return new SavingsMonthlyLineChart(data);
    }

    @Override
    protected SavingsTable createTable(){
        return new SavingsTable(data);
    }
}
