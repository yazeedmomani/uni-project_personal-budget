package layout.views.income;

import db.Database;
import db.Validator;
import db.models.IncomeRecord;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.Form;
import layout.components.income.IncomeBarChart;

import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class IncomeEdit {
    private static Dashboard dashboard;
    private static DashboardCard card;
    private static Form form;
    private static Validator validator;

    private static TextField idField, dateField, sourceField, amountField;
    private static TextArea notesField;
    private static Button createButton, readButton, updateButton, deleteButton;

    private static boolean isCreateMode;
    private static IncomeRecord record;

    public static ScrollPane getRoot(){
        form = new Form();
        validator = new Validator(form);

        idField = form.getIdInput();
        dateField = form.addField("Date", "YYYY-MM-DD");
        sourceField = form.addField("Source", "Income Source");
        amountField = form.addField("Amount", "0.00");
        notesField = form.addAreaField("Notes", "Notes (optional)");

        createButton = form.getCreateButton();
        readButton = form.getReadButton();
        updateButton = form.getUpdateButton();
        deleteButton = form.getDeleteButton();

        createButton.setOnAction(IncomeEdit::clickHandler);
        readButton.setOnAction(IncomeEdit::clickHandler);
        updateButton.setOnAction(IncomeEdit::clickHandler);
        deleteButton.setOnAction(IncomeEdit::clickHandler);

        card = new DashboardCard(form.getRoot());
        dashboard = new Dashboard();
        dashboard.getRoot().setFitToHeight(true);
        dashboard.add(card, 0, 0, 2, 3);
        return dashboard.getRoot();
    }

    private static void enterCreateMode(){
        form.reset();

        updateButton.setText("Insert Record");
        deleteButton.setText("Cancel");

        form.hideHeader();
        form.showFooter();
    }

    private static void exitCreateMode(){
        form.reset();

        form.showHeader();
        form.hideFooter();
    }

    private static void enterUpdateMode(IncomeRecord record){
        updateButton.setText("Update Record");
        deleteButton.setText("Delete Record");

        dateField.setText(record.getDate().toString());
        sourceField.setText(record.getSource());
        amountField.setText(String.valueOf(record.getAmount()));
        notesField.setText(record.getNotes());

        form.showFooter();
    }

    private static void exitUpdateMode(){
        form.reset();

        form.hideFooter();
    }

    private static void clickHandler(ActionEvent e){
        Object target = e.getTarget();

        // RETRIEVE RECORD
        if(target.equals(readButton)){
            isCreateMode = false;

            form.clear(dateField, sourceField, amountField, notesField);
            form.clearAlerts();

            if(validator.assertNotEmpty(idField)) return;
            if(validator.assertInteger(idField)) return;
            if(validator.assertPositiveNumber(idField)) return;

            int id = form.getInt(idField);

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

        // UPDATE RECORD
        if(target.equals(updateButton) && !isCreateMode){
            form.clearAlerts();

            if (validator.assertNotEmpty(dateField, sourceField, amountField)) return;
            if (validator.assertDateFormat(dateField)) return;
            if (validator.assertPositiveNumber(amountField)) return;

            LocalDate date = form.getLocalDate(dateField);
            String source = form.getString(sourceField);
            double amount = form.getDouble(amountField);
            String notes = form.getString(notesField);

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

        // DELETE RECORD
        if(target.equals(deleteButton) && !isCreateMode){
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
            isCreateMode = true;
            enterCreateMode();
        }

        // INSERT RECORD
        if(target.equals(updateButton) && isCreateMode){
            form.clearAlerts();

            if (validator.assertNotEmpty(dateField, sourceField, amountField)) return;
            if (validator.assertDateFormat(dateField)) return;
            if (validator.assertPositiveNumber(amountField)) return;

            LocalDate date = form.getLocalDate(dateField);
            String source = form.getString(sourceField);
            double amount = form.getDouble(amountField);
            String notes = form.getString(notesField);

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

        // CANCEL
        if(target.equals(deleteButton) && isCreateMode){
            exitCreateMode();
        }
    }
}
