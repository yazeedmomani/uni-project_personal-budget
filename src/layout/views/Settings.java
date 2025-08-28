package layout.views;

import db.Database;
import db.Validator;
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
    private static Validator validator;

    private static TextField nameField, usernameField;
    private static PasswordField passwordField, passwordConfirmField;
    private static Button updateButton;

    private static User user;

    public static ScrollPane getRoot(){
        user = Database.getCurrentUser();

        form = new Form();
        validator = new Validator(form);

        form.getFooter().getChildren().remove(form.getDeleteButton());
        form.hideHeader();
        form.showFooter();

        updateButton = form.getUpdateButton();
        updateButton.setText("Save");
        updateButton.setOnAction(Settings::eventHandler);

        nameField = form.addField("Name", "Name");
        usernameField = form.addField("Username", "Username");
        passwordField = form.addPasswordField("Password", "Password");
        passwordConfirmField = form.addPasswordField("Confirm Password", "Password");

        nameField.setText(user.getName());
        usernameField.setText(user.getUsername());

        card = new DashboardCard(form.getRoot());
        card.setTitle("Settings");
        card.setTItleStyle("-fx-padding: 0 10 12 10");
        dashbaord = new Dashboard();
        dashbaord.getRoot().setFitToHeight(true);
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }

    private static void eventHandler(ActionEvent e){
        form.clearAlerts();

        if(validator.assertNotEmpty(usernameField, nameField)) return;
        if(validator.assertConfirmation(passwordField, passwordConfirmField)) return;

        String name = Validator.getString(nameField);
        String username = Validator.getString(usernameField);
        String password = Validator.getString(passwordField);

        if(password.isEmpty()) {password = user.getPassword();}

        User updatedUser = new User(user.getId(), name, username, password);

        try{
            Database.updateUser(updatedUser);
            Database.setCurrentUser(updatedUser);
            TopMenu.reloadWelcomeLabel();
            form.setAlertMessage("success","Changes saved successfully");
        }
        catch (Exception exp){
            System.out.println("Settings Error: " + exp.getMessage());
            form.setAlertMessage("error","Couldn't save changes");
        }
    }
}
