package layout.views;

import db.Validator;
import db.dao.TemplateDAO;
import db.models.TemplateRecord;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.Form;

public abstract class TemplateEdit<Record extends TemplateRecord, DAO extends TemplateDAO<Record>> {
    protected Dashboard dashboard;
    protected DashboardCard card;
    protected Form form;
    protected Validator validator;

    protected TextField idField;
    protected TextArea notesField;
    protected Button createButton, readButton, updateButton, deleteButton;

    protected boolean isCreateMode;
    protected boolean isUpdateMode;
    protected Record record;
    protected DAO dao;

    // CONSTRUCTOR
    public TemplateEdit(DAO dao){
        this.dao = dao;

        form = new Form();
        validator = new Validator(form);

        idField = form.getIdInput();
        idField.setOnKeyPressed(this::eventHandler);

        initializeCustomFields();

        notesField = form.addAreaField("Notes", "Notes (optional)");
        notesField.setOnKeyPressed(this::eventHandler);

        createButton = form.getCreateButton();
        readButton = form.getReadButton();
        updateButton = form.getUpdateButton();
        deleteButton = form.getDeleteButton();

        createButton.setOnAction(this::eventHandler);
        readButton.setOnAction(this::eventHandler);
        updateButton.setOnAction(this::eventHandler);
        deleteButton.setOnAction(this::eventHandler);

        card = new DashboardCard(form.getRoot());
        dashboard = new Dashboard();
        dashboard.getRoot().setFitToHeight(true);
        dashboard.add(card, 0, 0, 2, 3);
    }

    // GET ROOT
    public ScrollPane getRoot(){
        return dashboard.getRoot();
    }

    // ENTER/EXIT MODES
    private void enterCreateMode(){
        isCreateMode = true;
        form.reset();

        updateButton.setText("Insert Record");
        deleteButton.setText("Cancel");

        form.hideHeader();
        form.showFooter();
    }

    private void exitCreateMode(){
        isCreateMode = false;
        form.reset();

        form.showHeader();
        form.hideFooter();
    }

    private void enterUpdateMode(Record record){
        isUpdateMode = true;

        updateButton.setText("Update Record");
        deleteButton.setText("Delete Record");

        idField.setText(String.valueOf(record.getId()));
        String n = record.getNotes();
        notesField.setText(n == null ? "" : n);

        setFieldTextToRetrievedValue();

        form.showFooter();
    }

    private void exitUpdateMode(){
        isUpdateMode = false;
        form.reset();
        form.hideFooter();
    }

    // EVENT HANDLERS
    protected void eventHandler(KeyEvent e){
        Object source = e.getSource();

        if(e.getCode() == KeyCode.ENTER){
            if(source.equals(idField))
                retrieve();

            if((checkSourceEqualAnyFields(source) || source.equals(notesField)) && isUpdateMode)
                update();

            if((checkSourceEqualAnyFields(source) || source.equals(notesField)) && isCreateMode)
                insert();
        }
    }

    protected void eventHandler(ActionEvent e){
        Object target = e.getTarget();

        // RETRIEVE RECORD
        if(target.equals(readButton)){
            retrieve();
        }

        // UPDATE RECORD
        if(target.equals(updateButton) && isUpdateMode){
            update();
        }

        // DELETE RECORD
        if(target.equals(deleteButton) && isUpdateMode){
            form.clearAlerts();

            try{
                dao.delete(record);

                exitUpdateMode();
                form.setAlertMessage("success","Record deleted successfully");
            }
            catch (Exception exp){
                form.setAlertMessage("error","Failed to delete record");
            }
        }

        // NEW RECORD
        if(target.equals(createButton)){
            enterCreateMode();
        }

        // INSERT RECORD
        if(target.equals(updateButton) && isCreateMode){
            insert();
        }

        // CANCEL
        if(target.equals(deleteButton) && isCreateMode){
            exitCreateMode();
        }
    }

    // EVENT HANDLERS HELPERS
    private void retrieve(){
        if(validator.assertNotEmpty(idField)) return;
        if(validator.assertInteger(idField)) return;
        if(validator.assertPositiveNumber(idField)) return;

        int id = Validator.getInt(idField);

        form.reset();

        try{
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

    private void update(){
        form.clearAlerts();

        if(assertAndGetFromFieldsAndSetCurrentRecord()) return;

        String notes = Validator.getString(notesField);
        record.setNotes(notes);

        try{
            dao.update(record);

            exitUpdateMode();
            form.setAlertMessage("success","Record updated successfully");
        }
        catch (Exception exp){
            form.setAlertMessage("error","Failed to update record");
        }
    }

    private void insert(){
        form.clearAlerts();

        String notes = Validator.getString(notesField);

        if(assertAndGetFromFieldsAndCreateNewRecord(notes)) return;

        try{
            dao.create(record);

            exitCreateMode();
            form.setAlertMessage("success","Record inserted successfully");
        }
        catch (Exception exp){
            form.setAlertMessage("error","Failed to insert record");
        }
    }

    // METHODS TO BE IMPLEMENTED
    protected abstract void initializeCustomFields();
    protected abstract void setFieldTextToRetrievedValue();
    protected abstract boolean checkSourceEqualAnyFields(Object source);
    protected abstract boolean assertAndGetFromFieldsAndSetCurrentRecord();
    protected abstract boolean assertAndGetFromFieldsAndCreateNewRecord(String notes);
}
