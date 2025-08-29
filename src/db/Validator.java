package db;

import javafx.scene.control.TextInputControl;
import layout.components.form.Form;
import layout.components.form.FormAlertType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validator {
    private Form form;

    public static String getString(TextInputControl input){return input.getText().trim();}
    public static int getInt(TextInputControl input){
        String text = getString(input);
        return text.isEmpty() || text.equals("-") ? 0 : Integer.parseInt(getString(input));
    }
    public static double getDouble(TextInputControl input){
        String text = getString(input);
        return text.isEmpty() || text.equals("-") ? 0.0 : Double.parseDouble(getString(input));
    }
    public static LocalDate getLocalDate(TextInputControl input){return LocalDate.parse(getString(input), Database.getDateFormat());}

    public boolean assertNotEmpty(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs) {
            if (Validator.getString(input).isEmpty()) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Please fill in the required fields");

        return hasError;
    }

    public boolean assertDateFormat(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                LocalDate.parse(Validator.getString(input), Database.getDateFormat());
            } catch (DateTimeParseException e) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Invalid date format. Use YYYY-MM-DD");

        return hasError;
    }

    public boolean assertNumber(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                Validator.getDouble(input);
            } catch (NumberFormatException e) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Use numbers only");

        return hasError;
    }

    public boolean assertPositiveNumber(TextInputControl... inputs){
        if(assertNumber(inputs)) return true;

        boolean hasError = false;

        for(TextInputControl input : inputs){
            double number = Validator.getDouble(input);
            if(number < 0){
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Values must be zero or greater");

        return hasError;
    }

    public boolean assertInteger(TextInputControl... inputs){
        if(assertNumber(inputs)) return true;

        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                Validator.getInt(input);
            } catch (NumberFormatException e) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Please enter a whole number");

        return hasError;
    }

    public boolean assertConfirmation(TextInputControl... inputs){
        boolean hasError = false;
        int emptyInputs = 0;

        // ASSERT ALL EMPTY OR NON EMPTY
        for(TextInputControl input : inputs)
            if (Validator.getString(input).isEmpty())
                emptyInputs++;
        if(emptyInputs < inputs.length && emptyInputs != 0) hasError = true;

        if(hasError) for(TextInputControl input : inputs) form.alert(input);
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Please fill in both fields");
        if(hasError) return hasError;

        // ASSERT EQUALITY
        String firstInput = Validator.getString(inputs[0]);

        for(TextInputControl input : inputs)
            if (!Validator.getString(input).equals(firstInput))
                hasError = true;

        if(hasError) for(TextInputControl input : inputs) form.alert(input);
        if(hasError) form.setAlertMessage(FormAlertType.ERROR,"Values do not match");
        if(hasError) return hasError;

        return hasError;
    }

    public Validator(Form form) {this.form = form;}
}
