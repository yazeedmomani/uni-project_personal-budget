package layout.views.investments;

import db.Database;
import db.dao.SavingsDAO;
import db.models.SavingsRecord;
import layout.views.templates.TemplateSavingsEdit;

import java.time.LocalDate;

public class InvestmentsEdit extends TemplateSavingsEdit<SavingsRecord, SavingsDAO> {
    public InvestmentsEdit(){
        super(Database.getSavingsDAO());
    }

    @Override
    protected SavingsRecord createRecord(LocalDate date, double change, String notes){
        return new SavingsRecord(date, change, notes);
    }
}
