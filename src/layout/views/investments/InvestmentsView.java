package layout.views.investments;

import db.Database;
import db.dao.InvestmentsDAO;
import db.models.InvestmentsRecord;
import layout.components.investments.InvestmentsLineChart;
import layout.components.investments.InvestmentsMonthlyLineChart;
import layout.components.investments.InvestmentsTable;
import layout.views.templates.TemplateSavingsView;

public class InvestmentsView extends TemplateSavingsView<InvestmentsRecord, InvestmentsDAO, InvestmentsLineChart, InvestmentsMonthlyLineChart, InvestmentsTable> {
    public InvestmentsView(){
        super(Database.getInvestmentsDAO());
    }

    @Override
    protected InvestmentsLineChart createLineChart(){
        return new InvestmentsLineChart(data);
    }

    @Override
    protected InvestmentsMonthlyLineChart createMonthlyLineChart(){
        return new InvestmentsMonthlyLineChart(data);
    }

    @Override
    protected InvestmentsTable createTable(){
        return new InvestmentsTable(data);
    }
}
