package layout.views.savings;

import db.Database;
import db.Validator;
import db.dao.SavingsDAO;

import db.models.SavingsRecord;
import javafx.scene.control.*;

import layout.views.templates.TemplateEdit;

import java.time.LocalDate;

public class SavingsEdit extends TemplateEdit<SavingsRecord, SavingsDAO> {
    private TextField dateField, changeField, balanceField;

    public SavingsEdit(){
        super(Database.getSavingsDAO());
    }

    protected void initializeCustomFields(){
        form.getHeaderLeftComponents().getChildren().remove(idField);
        readButton.setText("Retrieve Last Record");

        dateField = form.addField("Date", "YYYY-MM-DD");
        changeField = form.addField("Change", "0.00");
        balanceField = form.addField("Balance", "Balance");

        dateField.setOnKeyPressed(super::eventHandler);
        changeField.setOnKeyPressed(super::eventHandler);
        balanceField.setOnKeyPressed(super::eventHandler);
    }

    protected void setFieldTextToRetrievedValue(){
        dateField.setText(record.getDate().toString());
        changeField.setText(String.valueOf(record.getChange()));
        balanceField.setText(String.valueOf(record.getBalance()));
    }

    protected boolean checkSourceEqualAnyFields(Object source){
        return source.equals(dateField) || source.equals(changeField) || source.equals(balanceField);
    }

    protected boolean assertAndGetFromFieldsAndSetCurrentRecord(){
        if (validator.assertNotEmpty(dateField, changeField)) return true;
        if (validator.assertDateFormat(dateField)) return true;
        if (validator.assertNumber(changeField)) return true;

        LocalDate date = Validator.getLocalDate(dateField);
        double change = Validator.getDouble(changeField);

        record.setDate(date);
        record.setChange(change);
        return false;
    }

    protected boolean assertAndGetFromFieldsAndCreateNewRecord(String notes){
        if (validator.assertNotEmpty(dateField, changeField)) return true;
        if (validator.assertDateFormat(dateField)) return true;
        if (validator.assertNumber(changeField)) return true;

        LocalDate date = Validator.getLocalDate(dateField);
        double change = Validator.getDouble(changeField);

        record = new SavingsRecord(date, change, notes);
        return false;
    }

    @Override
    protected void retrieve(){
        form.reset();

        try{
            int id = dao.getLastID();
            record = dao.get(id);
            if(record == null){
                exitUpdateMode();
                form.setAlertMessage("error","Record does not exist");
                return;
            }
            enterUpdateMode(record);
        }
        catch (Exception exp){
            form.setAlertMessage("error","Failed to retrieve record");
        }
    }
}
