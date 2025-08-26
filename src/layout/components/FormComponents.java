package layout.components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class FormComponents {
    public static VBox createField(Label label, Node input){
        VBox field = new VBox(label, input);
        field.setSpacing(8);
        field.getStyleClass().add("form_field");
        return field;
    }

    public static Label createLabel(String text){
        Label label = new Label(text);
        label.getStyleClass().add("form_label");
        return label;
    }

    public static Button createButton(String text){
        Button button = new Button(text);
        button.getStyleClass().add("form_button");
        return button;
    }

    public static PasswordField createPasswordInput(String promptText){
        PasswordField input = new PasswordField();
        input.setPromptText(promptText);
        input.getStyleClass().add("form_input");
        return input;
    }

    public static TextArea createAreaInput(String promptText){
        TextArea input = new TextArea();
        input.setPromptText(promptText);
        input.setPrefRowCount(5);
        input.getStyleClass().add("form_input");
        return input;
    }

    public static TextField createInput(String promptText){
        TextField input = new TextField();
        input.setPromptText(promptText);
        input.getStyleClass().add("form_input");
        return input;
    }
}
