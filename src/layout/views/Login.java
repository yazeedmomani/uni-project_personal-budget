package layout.views;

import db.Database;
import db.Validator;
import db.models.User;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import app.App;

public class Login {
    private static VBox centerBox;
    private static FlowPane root;

    private static Button submitBtn;
    private static TextField usernameInput;
    private static PasswordField passwordInput;

    private static Label alertMessage;
    private static final String INVALID_INPUT_CLASS = "login_invalidInput";


    public static FlowPane getRoot(){
        alertMessage = new Label();
        alertMessage.getStyleClass().add("login_errorLabel");

        usernameInput = new TextField();
        passwordInput = new PasswordField();
        submitBtn = new Button("Log in");

        initializeInput(usernameInput, "Username");
        initializeInput(passwordInput, "Password");
        submitBtn.getStyleClass().add("login_button");
        submitBtn.setOnAction(Login::eventHandler);

        centerBox = new VBox(usernameInput, passwordInput, submitBtn);
        centerBox.getStyleClass().add("login_centerBox");

        root = new FlowPane(centerBox);
        root.getStyleClass().add("login");
        root.setOnMouseClicked(Login::eventHandler);

        return root;
    }

    // LAYOUT HELPER
    private static void initializeInput(TextInputControl input, String promptText){
        input.setPromptText(promptText);
        input.getStyleClass().add("login_input");
        input.setOnKeyPressed(Login::eventHandler);
    }

    // EVENT HANDLERS
    private static void eventHandler(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER) submit();
    }

    private static void eventHandler(Event e){
        Object source = e.getSource();

        if(source == root || source == centerBox) root.requestFocus();
        if(source == submitBtn) submit();
    }

    private static void submit(){
        clearAlerts();
        if(assertNotEmpty()) return;

        try{
            User user = Database.getUser(usernameInput.getText(), passwordInput.getText());
            if(user == null){
                setAlertMessage("Invalid username or password");
                return;
            }
            App.login(user);
        }
        catch(Exception exception){
            System.out.println("Error: " + exception.getMessage());
        }
    }

    // ASSERTION
    private static boolean assertNotEmpty(){
        String username = Validator.getString(usernameInput);
        String password = Validator.getString(passwordInput);

        if(username.isEmpty() || password.isEmpty()){
            setAlertMessage("Enter username and password");
            if(username.isEmpty()) alert(usernameInput);
            if(password.isEmpty()) alert(passwordInput);
            return true;
        }

        return false;
    }

    // ALERTS
    private static void setAlertMessage(String message){
        alertMessage.setText(message);
        centerBox.getChildren().addFirst(alertMessage);
    }

    private static void alert(TextInputControl input){
        input.getStyleClass().add(INVALID_INPUT_CLASS);
    }

    private static void clearAlerts(){
        centerBox.getChildren().remove(alertMessage);
        usernameInput.getStyleClass().remove(INVALID_INPUT_CLASS);
        passwordInput.getStyleClass().remove(INVALID_INPUT_CLASS);
    }
}
