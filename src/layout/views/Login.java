package layout.views;

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
        usernameInput = new TextField();
        passwordInput = new PasswordField();
        button = new Button("Log in");

        usernameInput.setPromptText("Username");
        usernameInput.setId("login_usernameInput");
        passwordInput.setPromptText("Password");
        passwordInput.setId("login_passwordInput");
        button.setId("login_button");

        centerBox = new VBox(usernameInput, passwordInput, button);
        centerBox.setId("login_centerBox");

        root = new FlowPane(centerBox);
        root.setId("login_root");

        button.setOnAction(e -> {
            App.login();
        });

        return root;
    }
}
