package layout.views;

import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import app.App;

public class Login {
    private static Button button;
    private static TextField usernameInput;
    private static PasswordField passwordInput;
    private static VBox centerBox;
    private static FlowPane root;

    public static FlowPane getRoot(){
        createCenterBox();

        root = new FlowPane(centerBox);
        root.getStyleClass().add("login");

        return root;
    }

    private static void createCenterBox(){
        usernameInput = createInput("Username");
        passwordInput = createPasswordInput("Password");
        button = createButton("Log in");

        centerBox = new VBox(usernameInput, passwordInput, button);
        centerBox.getStyleClass().add("login_centerBox");
    }

    private static PasswordField createPasswordInput(String promptText){
        PasswordField input = new PasswordField();
        input.setPromptText(promptText);
        input.getStyleClass().add("login_input");
        return input;
    }

    private static TextField createInput(String promptText){
        TextField input = new TextField();
        input.setPromptText(promptText);
        input.getStyleClass().add("login_input");
        return input;
    }

    private static Button createButton(String text){
        Button button = new Button(text);
        button.getStyleClass().add("login_button");
        button.setOnAction(Login::handleEvent);
        return button;
    }

    private static void handleEvent(ActionEvent e){
        Object source = e.getSource();

        if(source == button) App.login();
    }
}
