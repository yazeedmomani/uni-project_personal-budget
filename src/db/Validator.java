package db;

import javafx.scene.control.TextInputControl;
import layout.components.Form;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validator {
    private Form form;

    public boolean assertNotEmpty(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs) {
            if (form.getString(input).isEmpty()) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage("error","Please fill in the required fields");

        return hasError;
    }

    public boolean assertDateFormat(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                LocalDate.parse(form.getString(input), Database.getDateFormat());
            } catch (DateTimeParseException e) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage("error","Invalid date format. Use YYYY-MM-DD");

        return hasError;
    }

    public boolean assertNumber(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                form.getDouble(input);
            } catch (NumberFormatException e) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage("error","Use numbers only");

        return hasError;
    }

    public boolean assertPositiveNumber(TextInputControl... inputs){
        if(assertNumber(inputs)) return true;

        boolean hasError = false;

        for(TextInputControl input : inputs){
            double number = form.getDouble(input);
            if(number < 0){
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage("error","Values must be zero or greater");

        return hasError;
    }

    public boolean assertInteger(TextInputControl... inputs){
        if(assertNumber(inputs)) return true;

        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                form.getInt(input);
            } catch (NumberFormatException e) {
                form.alert(input);
                hasError = true;
            }
        }
        if(hasError) form.setAlertMessage("error","Please enter a whole number");

        return hasError;
    }

    public boolean assertConfirmation(TextInputControl... inputs){
        boolean hasError = false;
        int emptyInputs = 0;

        // ASSERT ALL EMPTY OR NON EMPTY
        for(TextInputControl input : inputs)
            if (form.getString(input).isEmpty())
                emptyInputs++;
        if(emptyInputs < inputs.length && emptyInputs != 0) hasError = true;

        if(hasError) for(TextInputControl input : inputs) form.alert(input);
        if(hasError) form.setAlertMessage("error","Please fill in both fields");
        if(hasError) return hasError;

        // ASSERT EQUALITY
        String firstInput = form.getString(inputs[0]);

        for(TextInputControl input : inputs)
            if (!form.getString(input).equals(firstInput))
                hasError = true;

        if(hasError) for(TextInputControl input : inputs) form.alert(input);
        if(hasError) form.setAlertMessage("error","Values do not match");
        if(hasError) return hasError;

        return hasError;
    }

    public Validator(Form form) {this.form = form;}
}
