package layout.views.income;

import db.Database;
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
    private static TextField idField, dateField, sourceField, amountField;
    private static TextArea notesField;
    private static Button createButton, readButton, updateButton, deleteButton;

    public static ScrollPane getRoot(){
        form = new Form();

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
        reset();
        form.hideHeader();
        updateButton.setText("Insert Record");
        updateButton.setOnAction(IncomeEdit::handleInsertButton);
        deleteButton.setText("Cancel");
        deleteButton.setOnAction(IncomeEdit::handleCancelButton);
        form.showFooter();
    }

    private static void handleCancelButton(ActionEvent e){
        reset();
        form.hideFooter();
        updateButton.setText("Update Record");
        updateButton.setOnAction(null);
        deleteButton.setText("Delete Record");
        deleteButton.setOnAction(null);
        form.showHeader();
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

            reset();
            form.hideFooter();
            updateButton.setText("Update Record");
            updateButton.setOnAction(null);
            deleteButton.setText("Delete Record");
            deleteButton.setOnAction(null);
            form.showHeader();

            form.setMessage("success","Record inserted successfully");
        }
        catch (Exception exp){
            form.setMessage("error","Failed to insert record");
        }
    }

    private static void handleReadButton(ActionEvent e){
        if(isInvalidId()) return;

        int id = form.getInt(idField);

        try{
            IncomeRecord record = Database.getIncomeDAO().get(id);
            if(record == null){
                form.setMessage("error","Record does not exist");
                form.hideFooter();
                return;
            }
            deleteButton.setOnAction(deleteEvent -> handleDeleteButton(deleteEvent, record));
            updateButton.setOnAction(updateEvent -> handleUpdateButton(updateEvent, record));
            form.showFooter();
            dateField.setText(record.getDate().toString());
            sourceField.setText(record.getSource());
            amountField.setText(String.valueOf(record.getAmount()));
            notesField.setText(record.getNotes());
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
            form.hideFooter();
            reset();
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

            form.hideFooter();
            reset();
            form.setMessage("success","Record updated successfully");
        }
        catch (Exception exp){
            form.setMessage("error","Failed to update record");
        }
    }

    private static boolean isInvalid(){
        String date = form.getString(dateField);
        String source = form.getString(sourceField);
        String amount = form.getString(amountField);

        form.removeMessage();
        form.setInvalid(false, idField, dateField, sourceField, amountField, notesField);

        boolean dateIsEmpty = date.equals("");
        boolean dateWrongFormat;
        try {
            LocalDate.parse(date, Database.getDateFormat());
            dateWrongFormat = false;
        } catch (DateTimeParseException e) {
            dateWrongFormat = true;
        }
        boolean sourceIsEmpty = source.equals("");
        boolean amountIsEmpty = amount.equals("");
        boolean amountNotNumber = false;
        Double amountValue = null;
        try {
            amountValue = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            amountNotNumber = true;
        }
        boolean amountIsNegative = (amountValue != null && amountValue < 0);

        if(dateIsEmpty || sourceIsEmpty || amountIsEmpty){
            form.setMessage("error","Please fill in the required fields");
            if(dateIsEmpty) form.setInvalid(true, dateField);
            if(sourceIsEmpty) form.setInvalid(true, sourceField);
            if(amountIsEmpty) form.setInvalid(true, amountField);
        }
        else if(dateWrongFormat){
            form.setMessage("error","Invalid date format. Use YYYY-MM-DD");
            form.setInvalid(true, dateField);
        }
        else if(amountNotNumber){
            form.setMessage("error","Invalid amount. Use numbers only");
            form.setInvalid(true, amountField);
        }
        else if(amountIsNegative){
            form.setMessage("error","Amount must be zero or greater");
            form.setInvalid(true, amountField);
        }
        else{
            return false;
        }

        return true;
    }

    private static boolean isInvalidId(){
        String id = form.getString(idField);

        form.removeMessage();
        form.clear(dateField, sourceField, amountField, notesField);
        form.setInvalid(false, idField);

        boolean fieldIsEmpty = id.equals("");
        boolean inputNotNumber;
        try {
            Integer.parseInt(id);
            inputNotNumber = false;
        } catch (NumberFormatException e) {
            inputNotNumber = true;
        }

        if(fieldIsEmpty){
            form.setMessage("error", "Enter ID");
        }
        else if(inputNotNumber){
            form.setMessage("error","ID must be a number");
        }
        else{
            return false;
        }

        form.setInvalid(true, idField);
        return true;
    }

    private static void reset(){
        form.clear(idField, dateField, sourceField, amountField, notesField);
        form.removeMessage();
        form.setInvalid(false, idField, dateField, sourceField, amountField, notesField);
    }
}
