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
    private static Dashboard dashbaord;
    private static DashboardCard card;
    private static Form form;
    private static Validator validator;
    private static TextField idField, dateField, sourceField, amountField;
    private static TextArea notesField;
    private static Button createButton, readButton, updateButton, deleteButton;

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

        readButton.setOnAction(IncomeEdit::handleReadButton);
        createButton.setOnAction(IncomeEdit::handleCreateButton);

        card = new DashboardCard(form.getRoot());
        dashbaord = new Dashboard();
        dashbaord.initializeFormSettings();
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }

    private static void handleCreateButton(ActionEvent e){
        enterCreateMode();
    }

    private static void handleCancelButton(ActionEvent e){
        exitCreateMode();
    }

    private static void handleInsertButton(ActionEvent e){
        if(isInvalid()) return;

        LocalDate date = form.getLocalDate(dateField);
        String source = form.getString(sourceField);
        double amount = form.getDouble(amountField);
        String notes = form.getString(notesField);

        IncomeRecord record = new IncomeRecord(date, source, amount, notes);

        try{
            Database.getIncomeDAO().create(record);

            exitCreateMode();
            form.setMessage("success","Record inserted successfully");
        }
        catch (Exception exp){
            form.setMessage("error","Failed to insert record");
        }
    }

    private static void handleReadButton(ActionEvent e){
        form.clear(dateField, sourceField, amountField, notesField);

        if(isInvalidId()) return;

        int id = form.getInt(idField);

        try{
            IncomeRecord record = Database.getIncomeDAO().get(id);
            if(record == null){
                form.setMessage("error","Record does not exist");
                exitUpdateMode();
                return;
            }
            enterUpdateMode(record);
        }
        catch (Exception exp){
            form.setMessage("error","Failed to retrieve record");
        }
    }

    private static void handleDeleteButton(ActionEvent e, IncomeRecord record){
        form.removeMessage();
        form.setInvalid(false, idField, dateField, sourceField, amountField, notesField);

        try{
            Database.getIncomeDAO().delete(record);

            exitUpdateMode();
            form.setMessage("success","Record deleted successfully");
        }
        catch (Exception exp){
            form.setMessage("error","Failed to delete record");
        }
    }

    private static void handleUpdateButton(ActionEvent e, IncomeRecord record){
        if(isInvalid()) return;

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
            form.setMessage("success","Record updated successfully");
        }
        catch (Exception exp){
            form.setMessage("error","Failed to update record");
        }
    }

    private static boolean isInvalid(){
        form.removeMessage();
        form.setInvalid(false, idField, dateField, sourceField, amountField, notesField);

        if (validator.checkNotEmpty(dateField, sourceField, amountField)) return true;
        if (validator.checkDateFormat(dateField)) return true;
        if (validator.checkPositiveNumber(amountField)) return true;

        return false;
    }

    private static boolean isInvalidId(){
        form.removeMessage();
        form.setInvalid(false, idField);

        if(validator.checkNotEmpty(idField)) return true;
        if(validator.checkInteger(idField)) return true;

        return false;
    }

    private static void enterCreateMode(){
        form.reset(idField, dateField, sourceField, amountField, notesField);
        form.hideHeader();
        updateButton.setText("Insert Record");
        updateButton.setOnAction(IncomeEdit::handleInsertButton);
        deleteButton.setText("Cancel");
        deleteButton.setOnAction(IncomeEdit::handleCancelButton);
        form.showFooter();
    }

    private static void exitCreateMode(){
        form.reset(idField, dateField, sourceField, amountField, notesField);
        form.hideFooter();
        updateButton.setText("Update Record");
        updateButton.setOnAction(null);
        deleteButton.setText("Delete Record");
        deleteButton.setOnAction(null);
        form.showHeader();
    }

    private static void enterUpdateMode(IncomeRecord record){
        deleteButton.setOnAction(deleteEvent -> handleDeleteButton(deleteEvent, record));
        updateButton.setOnAction(updateEvent -> handleUpdateButton(updateEvent, record));
        dateField.setText(record.getDate().toString());
        sourceField.setText(record.getSource());
        amountField.setText(String.valueOf(record.getAmount()));
        notesField.setText(record.getNotes());
        form.showFooter();
    }

    private static void exitUpdateMode(){
        form.hideFooter();
        form.reset(idField, dateField, sourceField, amountField, notesField);
    }
}
