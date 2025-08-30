package layout.views.fixed_expenses;

import db.Database;
import db.Validator;
import db.dao.SubscriptionsDAO;
import db.models.SubscriptionsRecord;
import javafx.scene.control.TextField;
import layout.views.templates.TemplateEdit;

import java.time.LocalDate;

public class FixedExpensesEdit extends TemplateEdit<SubscriptionsRecord, SubscriptionsDAO> {
    private TextField billField, amountField, dueDayField;

    public FixedExpensesEdit(){
        super(Database.getSubscriptionsDAO());
    }

    protected void initializeCustomFields(){
        billField = form.addField("Bill", "e.g., Gym, iCloud, Spotify");
        amountField = form.addField("Amount", "0.00");
        dueDayField = form.addField("Due Day", "Day of month (1–31)");

        billField.setOnKeyPressed(super::eventHandler);
        amountField.setOnKeyPressed(super::eventHandler);
        dueDayField.setOnKeyPressed(super::eventHandler);
    }

    protected void setFieldTextToRetrievedValue(){
        billField.setText(record.getSubscription());
        amountField.setText(String.valueOf(record.getAmount()));
        dueDayField.setText(String.valueOf(record.getExpectedDay()));
    }

    protected boolean checkSourceEqualAnyFields(Object source){
        return source.equals(billField) || source.equals(amountField) || source.equals(dueDayField);
    }

    protected boolean assertAndGetFromFieldsAndSetCurrentRecord(){
        if (validator.assertNotEmpty(billField, amountField, dueDayField)) return true;
        if (validator.assertPositiveNumber(amountField)) return true;
        if (validator.assertDayOfMonth(dueDayField)) return true;

        String bill = Validator.getString(billField);
        double amount = Validator.getDouble(amountField);
        int dueDay = Validator.getInt(dueDayField);

        record.setSubscription(bill);
        record.setAmount(amount);
        record.setExpectedDay(dueDay);
        return false;
    }

    protected boolean assertAndGetFromFieldsAndCreateNewRecord(String notes){
        if (validator.assertNotEmpty(billField, amountField, dueDayField)) return true;
        if (validator.assertPositiveNumber(amountField)) return true;
        if (validator.assertDayOfMonth(dueDayField)) return true;

        String bill = Validator.getString(billField);
        double amount = Validator.getDouble(amountField);
        int dueDay = Validator.getInt(dueDayField);

        record = new SubscriptionsRecord(bill, amount, dueDay, notes);
        return false;
    }
}
