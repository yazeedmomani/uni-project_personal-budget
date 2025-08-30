package layout.views.debts_receivables;

import db.Database;
import db.Validator;
import db.dao.DebtsDAO;
import db.models.DebtsRecord;
import javafx.scene.control.TextField;
import layout.views.templates.TemplateEdit;

import java.time.LocalDate;

public class DebtsReceivablesEdit extends TemplateEdit<DebtsRecord, DebtsDAO> {
    private TextField dateField, partyField, amountField;
    private final String DEBT_TYPE = "Credited";

    public DebtsReceivablesEdit(){
        super(Database.getDebtsDAO());
    }

    protected void initializeCustomFields(){
        dateField = form.addField("Date", "YYYY-MM-DD");
        partyField = form.addField("Source", "Income Source");
        amountField = form.addField("Amount", "0.00");

        dateField.setOnKeyPressed(super::eventHandler);
        partyField.setOnKeyPressed(super::eventHandler);
        amountField.setOnKeyPressed(super::eventHandler);
    }

    protected void setFieldTextToRetrievedValue(){
        dateField.setText(record.getDate().toString());
        partyField.setText(record.getParty());
        amountField.setText(String.valueOf(record.getAmount()));
    }

    protected boolean checkSourceEqualAnyFields(Object source){
        return source.equals(dateField) || source.equals(partyField) || source.equals(amountField);
    }

    protected boolean assertAndGetFromFieldsAndSetCurrentRecord(){
        if (validator.assertNotEmpty(dateField, partyField, amountField)) return true;
        if (validator.assertDateFormat(dateField)) return true;
        if (validator.assertPositiveNumber(amountField)) return true;

        LocalDate date = Validator.getLocalDate(dateField);
        String party = Validator.getString(partyField);
        double amount = Validator.getDouble(amountField);

        record.setDate(date);
        record.setParty(party);
        record.setAmount(amount);
        return false;
    }

    protected boolean assertAndGetFromFieldsAndCreateNewRecord(String notes){
        if (validator.assertNotEmpty(dateField, partyField, amountField)) return true;
        if (validator.assertDateFormat(dateField)) return true;
        if (validator.assertPositiveNumber(amountField)) return true;

        LocalDate date = Validator.getLocalDate(dateField);
        String party = Validator.getString(partyField);
        double amount = Validator.getDouble(amountField);

        record = new DebtsRecord(date, party, amount, notes, DEBT_TYPE);
        return false;
    }
}
