package layout.views;

import db.Database;
import db.models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.Form;

public class Settings {
    private static Dashboard dashbaord;
    private static DashboardCard card;
    private static Form form;
    private static TextField nameField, usernameField;
    private static PasswordField passwordField, passwordConfirmField;
    private static User user = Database.getCurrentUser();

    public static ScrollPane getRoot(){
        initializeForm();
        initializeFields();
        form.getUpdateButton().setOnAction(Settings::handleSave);

        card = new DashboardCard(form.getRoot());
        card.setTitle("Settings");
        card.setTItleStyle("-fx-padding: 0 10 12 10");

        dashbaord = new Dashboard();
        dashbaord.initializeFormSettings();
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }

    private static void handleSave(ActionEvent e){
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordConfirmField.getText();

        boolean nameAndUsernameAreEmpty = name.equals("") && username.equals("");
        boolean nameIsEmpty = name.equals("");
        boolean usernameIsEmpty = username.equals("");
        boolean onlyPasswordFilled = !password.equals("") && passwordConfirm.equals("");
        boolean onlyPasswordConfirmFilled = password.equals("") && !passwordConfirm.equals("");
        boolean passwordsNotEqual = !password.equals(passwordConfirm);

        Label errorLabel = form.getErrorLabel();

        if(nameAndUsernameAreEmpty){
            errorLabel.setText("Enter name and username");
            form.showErrorLabel();
            nameField.getStyleClass().add(form.getInvalidClass());
            usernameField.getStyleClass().add(form.getInvalidClass());
        }
        else if (nameIsEmpty){

        }
        else if (usernameIsEmpty){

        }
        else if(onlyPasswordFilled){
            // TODO
        }
        else if(onlyPasswordConfirmFilled){
            // TODO
        }
        else if(passwordsNotEqual) {
            // TODO
        }
        else{
            // TODO
        }
    }

    private static void initializeForm(){
        form = new Form();
        form.initializeForSettings();

        nameField = form.addField("Name", "Name");
        usernameField = form.addField("Username", "Username");
        passwordField = form.addPasswordField("Password", "Password");
        passwordConfirmField = form.addPasswordField("Confirm Password", "Password");
    }

    private static void initializeFields(){
        nameField.setText(user.getName());
        usernameField.setText(user.getUsername());
    }
}
