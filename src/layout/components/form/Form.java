package layout.components.form;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class Form {
    private BorderPane root;
    private HBox header, footer, headerLeftComponents;
    private VBox body;
    private Button createButton, updateButton, readButton, deleteButton;
    private TextField idInput;
    private Label alertMessage;
    private List<TextInputControl> inputs = new ArrayList<>();

    public static enum AlertType {ERROR, SUCCESS};

    // GETTERS
    public TextField getIdInput() {return idInput;}
    public Button getCreateButton() {return createButton;}
    public Button getReadButton() {return readButton;}
    public Button getUpdateButton() {return updateButton;}
    public Button getDeleteButton() {return deleteButton;}

    public HBox getHeader() {return header;}
    public VBox getBody() {return body;}
    public HBox getFooter() {return footer;}

    public HBox getHeaderLeftComponents() {return headerLeftComponents;}

    public BorderPane getRoot() {return root;}

    // RESET FORM / CLEAR INPUTS
    public void clear(TextInputControl... inputs){for(TextInputControl input : inputs) input.clear();}

    public void reset(){
        TextInputControl[] inputs = this.inputs.toArray(new TextInputControl[0]);
        clear(inputs);
        clearAlerts();
    }

    // ALERTS
    public void setAlertMessage(AlertType type, String text) {
        alertMessage.setText(text);
        if(type.equals(AlertType.ERROR)) alertMessage.setId("form_errorMessage");
        if(type.equals(AlertType.SUCCESS)) alertMessage.setId("form_successMessage");
        body.getChildren().addFirst(alertMessage);
    }

    public void alert(TextInputControl... inputs) {
        for(Node input : inputs) input.getStyleClass().add("form_invalidInput");
    }

    public void clearAlerts() {
        body.getChildren().remove(alertMessage);
        for(Node input : inputs) input.getStyleClass().remove("form_invalidInput");
    }

    // FOOTER/HEADER VISIBILITY
    public void hideFooter(){root.setBottom(null);}
    public void showFooter(){root.setBottom(footer);}

    public void hideHeader(){root.setTop(null);}
    public void showHeader(){root.setTop(header);}

    // CREATE FIELDS PUBLIC
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

    // CONSTRUCTOR
    public Form(){
        alertMessage = new Label();
        alertMessage.getStyleClass().add("form_alertMessage");

        initializeHeader();
        initializeBody();
        initializeFooter();

        initializeRoot();
    }

    // INITIALIZE LAYOUTS
    private void initializeRoot(){
        root = new BorderPane();

        root.setTop(header);
        root.setCenter(body);

        root.setMaxHeight(Double.MAX_VALUE);
        root.getStyleClass().add("form");

        VBox.setVgrow(root, Priority.ALWAYS);
    }

    private void initializeHeader(){
        idInput = FormComponents.createInput("ID");
        readButton = FormComponents.createButton("Retrieve Record");
        createButton = FormComponents.createButton("New Record");

        inputs.add(idInput);

        headerLeftComponents = new HBox(idInput, readButton);
        headerLeftComponents.setSpacing(12);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        header = new HBox(headerLeftComponents, space, createButton);

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

    // CREATE FIELDS PRIVATE
    private void addField(String title, TextInputControl input){
        inputs.add(input);

        Label label = FormComponents.createLabel(title);
        VBox field = FormComponents.createField(label, input);

        body.getChildren().add(field);
    }
}
