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
                form.setInvalid(true, input);
                hasError = true;
            }
        }
        if(hasError) form.setMessage("error","Please fill in the required fields");

        return hasError;
    }

    public boolean assertDateFormat(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                LocalDate.parse(form.getString(input), Database.getDateFormat());
            } catch (DateTimeParseException e) {
                form.setInvalid(true, input);
                hasError = true;
            }
        }
        if(hasError) form.setMessage("error","Invalid date format. Use YYYY-MM-DD");

        return hasError;
    }

    public boolean assertNumber(TextInputControl... inputs){
        boolean hasError = false;

        for(TextInputControl input : inputs){
            try {
                form.getDouble(input);
            } catch (NumberFormatException e) {
                form.setInvalid(true, input);
                hasError = true;
            }
        }
        if(hasError) form.setMessage("error","Use numbers only");

        return hasError;
    }

    public boolean assertPositiveNumber(TextInputControl... inputs){
        if(assertNumber(inputs)) return true;

        boolean hasError = false;

        for(TextInputControl input : inputs){
            double number = form.getDouble(input);
            if(number < 0){
                form.setInvalid(true, input);
                hasError = true;
            }
        }
        if(hasError) form.setMessage("error","Values must be zero or greater");

        return hasError;
    }

    public boolean assertInteger(TextInputControl... inputs){
        if(assertNumber(inputs)) return true;

        boolean hasError = false;
        for(TextInputControl input : inputs){
            try {
                form.getInt(input);
            } catch (NumberFormatException e) {
                form.setInvalid(true, input);
                hasError = true;
            }
        }
        if(hasError) form.setMessage("error","Please enter a whole number");

        return hasError;
    }

    public Validator(Form form) {this.form = form;}
}
