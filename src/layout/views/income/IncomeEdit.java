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

        LocalDate date = LocalDate.parse(dateField.getText().trim(), Database.getDateFormat());
        String source = sourceField.getText().trim();
        double amount = Double.parseDouble(amountField.getText().trim());
        String notes = notesField.getText().trim();

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

            Label successLabel = form.getSuccessLabel();
            successLabel.setText("Record inserted successfully");
            form.showSuccessLabel();
        }
        catch (Exception exp){
            Label errorLabel = form.getErrorLabel();
            errorLabel.setText("Failed to insert record");
            form.showErrorLabel();
        }
    }

    private static void handleReadButton(ActionEvent e){
        if(isInvalidId()) return;

        int id = Integer.parseInt(idField.getText());

        try{
            IncomeRecord record = Database.getIncomeDAO().get(id);
            if(record == null){
                Label errorLabel = form.getErrorLabel();
                errorLabel.setText("Record does not exist");
                form.showErrorLabel();
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
            Label errorLabel = form.getErrorLabel();
            errorLabel.setText("Failed to retrieve record");
            form.showErrorLabel();
        }
    }

    private static void handleDeleteButton(ActionEvent e, IncomeRecord record){
        resetFormMessages();

        try{
            Database.getIncomeDAO().delete(record);
            form.hideFooter();
            reset();
            Label successLabel = form.getSuccessLabel();
            successLabel.setText("Record deleted successfully");
            form.showSuccessLabel();
        }
        catch (Exception exp){
            Label errorLabel = form.getErrorLabel();
            errorLabel.setText("Failed to delete record");
            form.showErrorLabel();
        }
    }

    private static void handleUpdateButton(ActionEvent e, IncomeRecord record){
        if(isInvalid()) return;

        LocalDate date = LocalDate.parse(dateField.getText().trim(), Database.getDateFormat());
        String source = sourceField.getText().trim();
        double amount = Double.parseDouble(amountField.getText().trim());
        String notes = notesField.getText().trim();

        record.setDate(date);
        record.setSource(source);
        record.setAmount(amount);
        record.setNotes(notes);

        try{
            Database.getIncomeDAO().update(record);

            form.hideFooter();
            reset();
            Label successLabel = form.getSuccessLabel();
            successLabel.setText("Record updated successfully");
            form.showSuccessLabel();
        }
        catch (Exception exp){
            Label errorLabel = form.getErrorLabel();
            errorLabel.setText("Failed to update record");
            form.showErrorLabel();
        }
    }

    private static boolean isInvalid(){
        String date = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String amount = amountField.getText().trim();

        resetFormMessages();

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
        boolean amountNotNumber;
        try {
            Double.parseDouble(amount);
            amountNotNumber = false;
        } catch (NumberFormatException e) {
            amountNotNumber = true;
        }
        boolean amountIsNegative = Double.parseDouble(amount) < 0;

        Label errorLabel = form.getErrorLabel();

        if(dateIsEmpty || sourceIsEmpty || amountIsEmpty){
            errorLabel.setText("Please fill in the required fields");
            if(dateIsEmpty) setInvalid(true, dateField);
            if(sourceIsEmpty) setInvalid(true, sourceField);
            if(amountIsEmpty) setInvalid(true, amountField);
        }
        else if(dateWrongFormat){
            errorLabel.setText("Invalid date format. Use YYYY-MM-DD");
            setInvalid(true, dateField);
        }
        else if(amountNotNumber){
            errorLabel.setText("Invalid amount. Use numbers only");
            setInvalid(true, amountField);
        }
        else if(amountIsNegative){
            errorLabel.setText("Amount must be zero or greater");
            setInvalid(true, amountField);
        }
        else{
            return false;
        }

        form.showErrorLabel();
        return true;
    }

    private static boolean isInvalidId(){
        String id = idField.getText().trim();

        clearFields(dateField, sourceField, amountField, notesField);
        form.hideSuccessLabel();
        form.hideErrorLabel();
        setInvalid(false, idField);

        boolean fieldIsEmpty = id.equals("");
        boolean inputNotNumber;
        try {
            Integer.parseInt(id);
            inputNotNumber = false;
        } catch (NumberFormatException e) {
            inputNotNumber = true;
        }

        Label errorLabel = form.getErrorLabel();

        if(fieldIsEmpty){
            errorLabel.setText("Enter ID");
        }
        else if(inputNotNumber){
            errorLabel.setText("ID must be a number");
        }
        else{
            return false;
        }

        form.showErrorLabel();
        setInvalid(true, idField);
        return true;
    }

    private static void reset(){
        idField.clear();
        dateField.clear();
        sourceField.clear();
        amountField.clear();
        notesField.clear();

        resetFormMessages();
    }

    private static void setInvalid(boolean isInvalid, TextInputControl... inputs){
        if(isInvalid) for(Node input : inputs) input.getStyleClass().add(form.getInvalidClass());
        if(!isInvalid) for(Node input : inputs) input.getStyleClass().remove(form.getInvalidClass());
    }

    private static void resetFormMessages(){
        form.hideSuccessLabel();
        form.hideErrorLabel();
        setInvalid(false, idField, dateField, sourceField, amountField, notesField);
    }

    private static void clearFields(TextInputControl... inputs){
        for(TextInputControl input : inputs) input.clear();
    }
}
