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

        if(isInvalid()) return;


    }

    private static boolean isInvalid(){
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordConfirmField.getText();

        clearInvalid();

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
            errorLabel.setText("Enter name");
            form.showErrorLabel();
            nameField.getStyleClass().add(form.getInvalidClass());
        }
        else if (usernameIsEmpty){
            errorLabel.setText("Enter username");
            form.showErrorLabel();
            usernameField.getStyleClass().add(form.getInvalidClass());
        }
        else if(onlyPasswordFilled){
            errorLabel.setText("Confirm password");
            form.showErrorLabel();
            passwordConfirmField.getStyleClass().add(form.getInvalidClass());
        }
        else if(onlyPasswordConfirmFilled){
            errorLabel.setText("Enter password");
            form.showErrorLabel();
            passwordField.getStyleClass().add(form.getInvalidClass());
        }
        else if(passwordsNotEqual) {
            errorLabel.setText("Passwords do not match");
            form.showErrorLabel();
            passwordField.getStyleClass().add(form.getInvalidClass());
            passwordConfirmField.getStyleClass().add(form.getInvalidClass());
        }
        else{
            return false;
        }
        return true;
    }

    private static void clearInvalid(){
        form.hideErrorLabel();
        nameField.getStyleClass().remove(form.getInvalidClass());
        usernameField.getStyleClass().remove(form.getInvalidClass());
        passwordField.getStyleClass().remove(form.getInvalidClass());
        passwordConfirmField.getStyleClass().remove(form.getInvalidClass());
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
