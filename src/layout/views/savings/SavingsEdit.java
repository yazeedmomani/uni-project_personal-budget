package layout.views.savings;

import db.Database;
import db.Validator;
import db.models.IncomeRecord;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import layout.components.dashboard.Dashboard;
import layout.components.dashboard.DashboardCard;
import layout.components.form.Form;

import java.time.LocalDate;

public class SavingsEdit {
    private static Dashboard dashboard;
    private static DashboardCard card;
    private static Form form;
    private static Validator validator;

    private static TextField idField, dateField, sourceField, amountField;
    private static TextArea notesField;
    private static Button createButton, readButton, updateButton, deleteButton;

    private static boolean isCreateMode;
    private static boolean isUpdateMode;
    private static IncomeRecord record;

    public static ScrollPane getRoot(){
        form = new Form();
        validator = new Validator(form);

        idField = form.getIdInput();
        dateField = form.addField("Date", "YYYY-MM-DD");
        sourceField = form.addField("Source", "Income Source");
        amountField = form.addField("Amount", "0.00");
        notesField = form.addAreaField("Notes", "Notes (optional)");

        idField.setOnKeyPressed(SavingsEdit::eventHandler);
        dateField.setOnKeyPressed(SavingsEdit::eventHandler);
        sourceField.setOnKeyPressed(SavingsEdit::eventHandler);
        amountField.setOnKeyPressed(SavingsEdit::eventHandler);
        notesField.setOnKeyPressed(SavingsEdit::eventHandler);

        createButton = form.getCreateButton();
        readButton = form.getReadButton();
        updateButton = form.getUpdateButton();
        deleteButton = form.getDeleteButton();

        createButton.setOnAction(SavingsEdit::eventHandler);
        readButton.setOnAction(SavingsEdit::eventHandler);
        updateButton.setOnAction(SavingsEdit::eventHandler);
        deleteButton.setOnAction(SavingsEdit::eventHandler);

        card = new DashboardCard(form.getRoot());
        dashboard = new Dashboard();
        dashboard.getRoot().setFitToHeight(true);
        dashboard.add(card, 0, 0, 2, 3);
        return dashboard.getRoot();
    }

    // ENTER/EXIT MODES
    private static void enterCreateMode(){
        isCreateMode = true;
        form.reset();

        updateButton.setText("Insert Record");
        deleteButton.setText("Cancel");

        form.hideHeader();
        form.showFooter();
    }

    private static void exitCreateMode(){
        isCreateMode = false;
        form.reset();

        form.showHeader();
        form.hideFooter();
    }

    private static void enterUpdateMode(IncomeRecord record){
        isUpdateMode = true;

        updateButton.setText("Update Record");
        deleteButton.setText("Delete Record");

        dateField.setText(record.getDate().toString());
        sourceField.setText(record.getSource());
        amountField.setText(String.valueOf(record.getAmount()));
        notesField.setText(record.getNotes());

        form.showFooter();
    }

    private static void exitUpdateMode(){
        isUpdateMode = false;
        form.reset();
        form.hideFooter();
    }

    // EVENT HANDLERS
    private static void eventHandler(KeyEvent e){
        Object source = e.getSource();

        if(e.getCode() == KeyCode.ENTER){
            if(source.equals(idField))
                retrieve();

            if((source.equals(dateField) || source.equals(sourceField)
                    || source.equals(amountField) || source.equals(notesField)) && isUpdateMode)
                update();

            if((source.equals(dateField) || source.equals(sourceField)
                    || source.equals(amountField) || source.equals(notesField)) && isCreateMode)
                insert();
        }
    }

    private static void eventHandler(ActionEvent e){
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
                Database.getIncomeDAO().delete(record);

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
    private static void retrieve(){
        form.clear(dateField, sourceField, amountField, notesField);
        form.clearAlerts();

        if(validator.assertNotEmpty(idField)) return;
        if(validator.assertInteger(idField)) return;
        if(validator.assertPositiveNumber(idField)) return;

        int id = Validator.getInt(idField);

        try{
            record = Database.getIncomeDAO().get(id);
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

    private static void update(){
        form.clearAlerts();

        if (validator.assertNotEmpty(dateField, sourceField, amountField)) return;
        if (validator.assertDateFormat(dateField)) return;
        if (validator.assertPositiveNumber(amountField)) return;

        LocalDate date = Validator.getLocalDate(dateField);
        String source = Validator.getString(sourceField);
        double amount = Validator.getDouble(amountField);
        String notes = Validator.getString(notesField);

        record.setDate(date);
        record.setSource(source);
        record.setAmount(amount);
        record.setNotes(notes);

        try{
            Database.getIncomeDAO().update(record);

            exitUpdateMode();
            form.setAlertMessage("success","Record updated successfully");
        }
        catch (Exception exp){
            form.setAlertMessage("error","Failed to update record");
        }
    }

    private static void insert(){
        form.clearAlerts();

        if (validator.assertNotEmpty(dateField, sourceField, amountField)) return;
        if (validator.assertDateFormat(dateField)) return;
        if (validator.assertPositiveNumber(amountField)) return;

        LocalDate date = Validator.getLocalDate(dateField);
        String source = Validator.getString(sourceField);
        double amount = Validator.getDouble(amountField);
        String notes = Validator.getString(notesField);

        record = new IncomeRecord(date, source, amount, notes);

        try{
            Database.getIncomeDAO().create(record);

            exitCreateMode();
            form.setAlertMessage("success","Record inserted successfully");
        }
        catch (Exception exp){
            form.setAlertMessage("error","Failed to insert record");
        }
    }
}
