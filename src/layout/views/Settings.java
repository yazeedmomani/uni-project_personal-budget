package layout.views;

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

    public static ScrollPane getRoot(){
        form = new Form();
        form.initializeForSettings();

        nameField = form.addField("Name", "Name");
        usernameField = form.addField("Username", "Username");
        passwordField = form.addPasswordField("Password", "Password");
        passwordConfirmField = form.addPasswordField("Confirm Password", "Password");

        card = new DashboardCard(form.getRoot());
        card.setTitle("Settings");
        card.setTItleStyle("-fx-padding: 0 10 12 10");
        
        dashbaord = new Dashboard();
        dashbaord.initializeFormSettings();
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }
}
