package layout.views.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.event.ActionEvent;
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

    private static boolean isInvalidId(){
        String id = idField.getText().trim();
        System.out.println(id);

        clearFields();
        form.hideSuccessLabel();
        form.hideErrorLabel();
        idField.getStyleClass().remove(form.getInvalidClass());

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
        idField.getStyleClass().add(form.getInvalidClass());
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

    private static void resetFormMessages(){
        form.hideSuccessLabel();
        form.hideErrorLabel();
        idField.getStyleClass().remove(form.getInvalidClass());
        dateField.getStyleClass().remove(form.getInvalidClass());
        sourceField.getStyleClass().remove(form.getInvalidClass());
        amountField.getStyleClass().remove(form.getInvalidClass());
        notesField.getStyleClass().remove(form.getInvalidClass());
    }

    private static void clearFields(){
        dateField.clear();
        sourceField.clear();
        amountField.clear();
        notesField.clear();
    }
}
