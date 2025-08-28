package layout.views.income;

import db.Database;
import db.Validator;
import db.dao.IncomeDAO;
import db.models.IncomeRecord;

import javafx.scene.control.*;

import layout.views.TemplateEdit;

import java.time.LocalDate;

public class IncomeEdit extends TemplateEdit<IncomeRecord, IncomeDAO> {
    private TextField dateField, sourceField, amountField;

    public IncomeEdit(){
        super(Database.getIncomeDAO());
    }

    protected void initializeCustomFields(){
        dateField = form.addField("Date", "YYYY-MM-DD");
        sourceField = form.addField("Source", "Income Source");
        amountField = form.addField("Amount", "0.00");

        dateField.setOnKeyPressed(super::eventHandler);
        sourceField.setOnKeyPressed(super::eventHandler);
        amountField.setOnKeyPressed(super::eventHandler);
    }

    protected void setFieldTextToRetrievedValue(){
        dateField.setText(record.getDate().toString());
        sourceField.setText(record.getSource());
        amountField.setText(String.valueOf(record.getAmount()));
    }

    protected boolean checkSourceEqualAnyFields(Object source){
        return source.equals(dateField) || source.equals(sourceField) || source.equals(amountField);
    }

    protected boolean assertAndGetFromFieldsAndSetCurrentRecord(){
        if (validator.assertNotEmpty(dateField, sourceField, amountField)) return true;
        if (validator.assertDateFormat(dateField)) return true;
        if (validator.assertPositiveNumber(amountField)) return true;

        LocalDate date = Validator.getLocalDate(dateField);
        String source = Validator.getString(sourceField);
        double amount = Validator.getDouble(amountField);

        record.setDate(date);
        record.setSource(source);
        record.setAmount(amount);
        return false;
    }

    protected boolean assertAndGetFromFieldsAndCreateNewRecord(String notes){
        if (validator.assertNotEmpty(dateField, sourceField, amountField)) return true;
        if (validator.assertDateFormat(dateField)) return true;
        if (validator.assertPositiveNumber(amountField)) return true;

        LocalDate date = Validator.getLocalDate(dateField);
        String source = Validator.getString(sourceField);
        double amount = Validator.getDouble(amountField);

        record = new IncomeRecord(date, source, amount, notes);
        return false;
    }
}
