package layout.components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Form {
    private BorderPane root;
    private HBox header, footer;
    private VBox body;
    private Button createButton, updateButton, readButton, deleteButton;
    private TextField idInput;

    public TextArea addAreaField(String title, String placeholder){
        TextArea input = createAreaInput(placeholder);
        addField(title, input);
        return input;
    }

    public TextField addField(String title, String placeholder){
        TextField input = createInput(placeholder);
        addField(title, input);
        return input;
    }

    public BorderPane getRoot() {return root;}

    public Form(){
        initializeHeader();
        initializeBody();
        initializeFooter();
        initializeRoot();
    }

    private void initializeRoot(){
        root = new BorderPane();

        root.setTop(header);
        root.setCenter(body);
        root.setBottom(footer);

        root.setMaxHeight(Double.MAX_VALUE);
        root.getStyleClass().add("form");

        VBox.setVgrow(root, Priority.ALWAYS);
    }

    private void initializeHeader(){
        idInput = createInput("ID");
        readButton = createButton("Retrieve Record");
        createButton = createButton("New Record");

        HBox group = new HBox(idInput, readButton);
        group.setSpacing(8);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        header = new HBox(group, space, createButton);

        header.setFillHeight(true);
        header.getStyleClass().add("form_header");
    }

    private void initializeBody(){
        body = new VBox();
        body.setSpacing(12);
        body.getStyleClass().add("form_body");
    }

    private void initializeFooter(){
        updateButton = createButton("Update Record");
        deleteButton = createButton("Delete Record");
        footer = new HBox(updateButton, deleteButton);
        footer.setSpacing(10);
        footer.getStyleClass().add("form_footer");
    }

    private void addField(String title, Node input){
        Label label = createLabel(title);
        VBox field = createField(label, input);
        body.getChildren().add(field);
    }

    private VBox createField(Label label, Node input){
        VBox field = new VBox(label, input);
        field.setSpacing(4);
        field.getStyleClass().add("form_field");
        return field;
    }

    private Label createLabel(String text){
        Label label = new Label(text);
        label.getStyleClass().add("form_label");
        return label;
    }

    private TextArea createAreaInput(String promptText){
        TextArea input = new TextArea();
        input.setPromptText(promptText);
        input.setPrefRowCount(5);
        input.getStyleClass().add("form_input");
        return input;
    }

    private TextField createInput(String promptText){
        TextField input = new TextField();
        input.setPromptText(promptText);
        input.getStyleClass().add("form_input");
        return input;
    }

    private Button createButton(String text){
        Button button = new Button(text);
        button.getStyleClass().add("form_button");
        return button;
    }
}
