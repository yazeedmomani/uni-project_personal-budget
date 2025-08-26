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
    private Label errorLabel, successLabel;
    private final String INVALID_INPUT_CLASS = "form_invalidInput";

    public Button getCreateButton() {return createButton;}
    public Button getReadButton() {return readButton;}
    public Button getUpdateButton() {return updateButton;}
    public Button getDeleteButton() {return deleteButton;}

    public BorderPane getRoot() {return root;}

    public Label getErrorLabel() {return errorLabel;}
    public String getInvalidClass() {return INVALID_INPUT_CLASS;}

    public void showErrorLabel() {body.getChildren().addFirst(errorLabel);}
    public void hideErrorLabel() {body.getChildren().remove(errorLabel);}

    public Label getSuccessLabel() {return successLabel;}

    public void showSuccessLabel() {body.getChildren().addFirst(successLabel);}
    public void hideSuccessLabel() {body.getChildren().remove(successLabel);}

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
        initializeLabels();
        initializeHeader();
        initializeBody();
        initializeFooter();
        initializeRoot();
    }

    private void initializeLabels(){
        errorLabel = new Label();
        errorLabel.getStyleClass().add("form_errorLabel");
        successLabel = new Label();
        successLabel.getStyleClass().add("form_successLabel");
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
