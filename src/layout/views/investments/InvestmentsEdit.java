package layout.views.investments;

import db.Database;
import db.dao.InvestmentsDAO;
import db.models.InvestmentsRecord;
import layout.views.templates.TemplateSavingsEdit;

import java.time.LocalDate;

public class InvestmentsEdit extends TemplateSavingsEdit<InvestmentsRecord, InvestmentsDAO> {
    public InvestmentsEdit(){
        super(Database.getInvestmentsDAO());
    }

    @Override
    protected InvestmentsRecord createRecord(LocalDate date, double change, String notes){
        return new InvestmentsRecord(date, change, notes);
    }
}
