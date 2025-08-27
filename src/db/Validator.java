package db;

import javafx.scene.control.TextInputControl;
import layout.components.Form;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validator {
    private Form form;

    public boolean assertNotEmpty(TextInputControl... inputs){
        boolean hasEmptyInput = false;

        for(TextInputControl input : inputs) {
            if (form.getString(input).isEmpty()) {
                form.setInvalid(true, input);
                hasEmptyInput = true;
            }
        }
        if(hasEmptyInput) form.setMessage("error","Please fill in the required fields");

        return hasEmptyInput;
    }

    public boolean assertDateFormat(TextInputControl input){
        try {
            LocalDate.parse(form.getString(input), Database.getDateFormat());
            return false;
        } catch (DateTimeParseException e) {
            form.setMessage("error","Invalid date format. Use YYYY-MM-DD");
            form.setInvalid(true, input);
            return true;
        }
    }

    public boolean assertNumber(TextInputControl input){
        try {
            form.getDouble(input);
            return false;
        } catch (NumberFormatException e) {
            form.setMessage("error","Use numbers only");
            form.setInvalid(true, input);
            return true;
        }
    }

    public boolean assertPositiveNumber(TextInputControl input){
        if(assertNumber(input)) return false;

        double number = form.getDouble(input);

        if(number < 0){
            form.setMessage("error","Values must be zero or greater");
            form.setInvalid(true, input);
            return true;
        }
        return false;
    }

    public boolean assertInteger(TextInputControl input){
        if(assertNumber(input)) return false;

        try {
            form.getInt(input);
            return false;
        } catch (NumberFormatException e) {
            form.setMessage("error","Please enter a whole number");
            form.setInvalid(true, input);
            return true;
        }
    }

    public Validator(Form form) {this.form = form;}
}
