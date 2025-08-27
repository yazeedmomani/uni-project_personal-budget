package layout.components;

import db.Database;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Form {
    private BorderPane root;
    private HBox header, footer;
    private VBox body;
    private Button createButton, updateButton, readButton, deleteButton;
    private TextField idInput;
    private Label alertMessage;
    private List<TextInputControl> inputs = new ArrayList<>();

    // GETTERS
    public TextField getIdInput() {return idInput;}
    public Button getCreateButton() {return createButton;}
    public Button getReadButton() {return readButton;}
    public Button getUpdateButton() {return updateButton;}
    public Button getDeleteButton() {return deleteButton;}

    public HBox getHeader() {return header;}
    public VBox getBody() {return body;}
    public HBox getFooter() {return footer;}

    public BorderPane getRoot() {return root;}

    // GET FROM INPUT
    public String getString(TextInputControl input){return input.getText().trim();}
    public int getInt(TextInputControl input){return Integer.parseInt(getString(input));}
    public double getDouble(TextInputControl input){return Double.parseDouble(getString(input));}
    public LocalDate getLocalDate(TextInputControl input){return LocalDate.parse(getString(input), Database.getDateFormat());}

    // RESET FORM / CLEAR INPUTS
    public void clear(TextInputControl... inputs){for(TextInputControl input : inputs) input.clear();}

    public void reset(){
        TextInputControl[] inputs = this.inputs.toArray(new TextInputControl[0]);
        clear(inputs);
        clearAlerts();
    }

    // ALERTS
    public void setAlertMessage(String type, String text) {
        alertMessage.setText(text);
        if(type.equals("error")) alertMessage.setId("form_errorMessage");
        if(type.equals("success")) alertMessage.setId("form_successMessage");
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

    // TODO REMOVE
    public void initializeFormSettings(){
        root.setTop(null);

        footer.getChildren().remove(deleteButton);

        updateButton.setText("Save");
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

    // CREATE FIELDS PRIVATE
    private void addField(String title, TextInputControl input){
        inputs.add(input);

        Label label = FormComponents.createLabel(title);
        VBox field = FormComponents.createField(label, input);

        body.getChildren().add(field);
    }
}
