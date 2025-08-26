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

    public Button getCreateButton() {return createButton;}
    public Button getReadButton() {return readButton;}
    public Button getUpdateButton() {return updateButton;}
    public Button getDeleteButton() {return deleteButton;}

    public BorderPane getRoot() {return root;}

    public PasswordField addPasswordField(String title, String placeholder){
        PasswordField input = FormComponents.createPasswordInput(placeholder);
        addField(title, input);
        return input;
    }

    public TextArea addAreaField(String title, String placeholder){
        TextArea input = FormComponents.createAreaInput(placeholder);
        addField(title, input);
        return input;
    }

    public TextField addField(String title, String placeholder){
        TextField input = FormComponents.createInput(placeholder);
        addField(title, input);
        return input;
    }

    public void initializeForSettings(){
        root.setTop(null);
        footer.getChildren().remove(deleteButton);
        updateButton.setText("Save");
    }

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
        idInput = FormComponents.createInput("ID");
        readButton = FormComponents.createButton("Retrieve Record");
        createButton = FormComponents.createButton("New Record");

        HBox group = new HBox(idInput, readButton);
        group.setSpacing(12);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        header = new HBox(group, space, createButton);

        header.setFillHeight(true);
        header.getStyleClass().add("form_header");
    }

    private void initializeBody(){
        body = new VBox();
        body.setSpacing(24);
        body.getStyleClass().add("form_body");
    }

    private void initializeFooter(){
        updateButton = FormComponents.createButton("Update Record");
        deleteButton = FormComponents.createButton("Delete Record");
        deleteButton.setId("form_deleteButton");

        footer = new HBox(updateButton, deleteButton);
        footer.setSpacing(10);
        footer.getStyleClass().add("form_footer");
    }

    private void addField(String title, Node input){
        Label label = FormComponents.createLabel(title);
        VBox field = FormComponents.createField(label, input);
        body.getChildren().add(field);
    }
}
