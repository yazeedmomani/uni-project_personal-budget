package layout.views.templates;

import db.Validator;
import db.dao.templates.TemplateSavingsDAO;
import db.models.templates.TemplateSavingsRecord;
import javafx.scene.control.TextField;
import layout.components.form.FormAlertType;

import java.time.LocalDate;

public abstract class TemplateSavingsEdit<Record extends TemplateSavingsRecord, DAO extends TemplateSavingsDAO<Record>> extends TemplateEdit<Record, DAO> {
    private TextField dateField, changeField, balanceField;
    private double lastBalance, change;

    public TemplateSavingsEdit(DAO dao){
        super(dao);
    }

    protected void initializeCustomFields(){
        form.getHeaderLeftComponents().getChildren().remove(idField);
        readButton.setText("Retrieve Last Record");

        dateField = form.addField("Date", "YYYY-MM-DD");
        changeField = form.addField("Change", "0.00");
        balanceField = form.addField("Balance", "Balance");

        balanceField.setDisable(true);

        dateField.setOnKeyPressed(super::eventHandler);
        changeField.setOnKeyPressed(super::eventHandler);

        changeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(mode == TemplateEdit.Mode.UPDATE)
                balanceField.setText(String.valueOf(lastBalance + (Validator.getDouble(changeField) - change)));
            if(mode == TemplateEdit.Mode.CREATE)
                balanceField.setText(String.valueOf(lastBalance + Validator.getDouble(changeField)));
        });
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

        record = createRecord(date, change, notes);
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
                form.setAlertMessage(FormAlertType.ERROR,"Record does not exist");
                return;
            }
            enterUpdateMode(record);
        }
        catch (Exception exp){
            form.setAlertMessage(FormAlertType.ERROR,"Failed to retrieve record");
        }
    }

    @Override
    protected void enterUpdateMode(Record record){
        mode = TemplateEdit.Mode.UPDATE;

        updateButton.setText("Update Record");
        deleteButton.setText("Delete Record");

        lastBalance = record.getBalance();
        change = record.getChange();

        idField.setText(String.valueOf(record.getId()));
        String n = record.getNotes();
        notesField.setText(n == null ? "" : n);

        setFieldTextToRetrievedValue();

        form.showFooter();
    }

    @Override
    protected void enterCreateMode(){
        mode = TemplateEdit.Mode.CREATE;
        form.reset();

        try{
            lastBalance = dao.getLastBalance();
        }
        catch (Exception e){
            form.setAlertMessage(FormAlertType.ERROR, "Failed to fetch last balance");
        }

        updateButton.setText("Insert Record");
        deleteButton.setText("Cancel");

        balanceField.setText(String.valueOf(lastBalance));

        form.hideHeader();
        form.showFooter();
    }

    protected abstract Record createRecord(LocalDate date, double change, String notes);
}
