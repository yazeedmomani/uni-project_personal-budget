package layout.views;

import db.Database;
import db.models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.Form;
import layout.menus.TopMenu;

public class Settings {
    private static Dashboard dashbaord;
    private static DashboardCard card;
    private static Form form;
    private static TextField nameField, usernameField;
    private static PasswordField passwordField, passwordConfirmField;
    private static User user;

    public static ScrollPane getRoot(){
        user = Database.getCurrentUser();
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

        if(isInvalid()) return;
        if(password.equals("")) {password = user.getPassword();}

        User updatedUser = new User(user.getId(), name, username, password);

        try{
            Database.updateUser(updatedUser);
            Database.setCurrentUser(updatedUser);
            TopMenu.reloadWelcomeLabel();
            form.setMessage("success","Changes saved successfully");
        }
        catch (Exception exp){
            System.out.println("Settings Error: " + exp.getMessage());
            form.setMessage("error","Couldn't save changes");
        }
    }

    private static boolean isInvalid(){
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String passwordConfirm = passwordConfirmField.getText().trim();

        clearFormMessages();

        boolean nameAndUsernameAreEmpty = name.equals("") && username.equals("");
        boolean nameIsEmpty = name.equals("");
        boolean usernameIsEmpty = username.equals("");
        boolean onlyPasswordFilled = !password.equals("") && passwordConfirm.equals("");
        boolean onlyPasswordConfirmFilled = password.equals("") && !passwordConfirm.equals("");
        boolean passwordsNotEqual = !password.equals(passwordConfirm);

        if(nameAndUsernameAreEmpty){
            form.setMessage("error","Enter name and username");
            form.setInvalid(true, nameField, usernameField);
        }
        else if (nameIsEmpty){
            form.setMessage("error","Enter name");
            form.setInvalid(true, nameField);
        }
        else if (usernameIsEmpty){
            form.setMessage("error","Enter username");
            form.setInvalid(true, usernameField);
        }
        else if(onlyPasswordFilled){
            form.setMessage("error","Confirm password");
            form.setInvalid(true, passwordConfirmField);
        }
        else if(onlyPasswordConfirmFilled){
            form.setMessage("error","Enter password");
            form.setInvalid(true, passwordField);
        }
        else if(passwordsNotEqual) {
            form.setMessage("error","Passwords do not match");
            form.setInvalid(true, passwordField, passwordConfirmField);
        }
        else{
            return false;
        }
        return true;
    }

    private static void clearFormMessages(){
        form.removeMessage();
        form.setInvalid(false, nameField, usernameField, passwordField, passwordConfirmField);
    }

    private static void initializeForm(){
        form = new Form();
        form.initializeFormSettings();
        form.showFooter();

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
