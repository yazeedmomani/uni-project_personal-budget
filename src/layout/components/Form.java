package layout.components;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Form {
    private BorderPane root;
    private HBox header, footer;
    private VBox body;

    public void addAreaField(String labelText, String promptText){
        Label label = new Label(labelText);
        label.getStyleClass().add("form_label");
        TextArea input = new TextArea();
        input.setPromptText(promptText);
        input.setPrefRowCount(5);
        input.getStyleClass().add("form_input");
        VBox field = new VBox(label, input);
        field.getStyleClass().add("form_field");
        field.setSpacing(4);
        body.getChildren().add(field);
    }

    public void addField(String labelText, String promptText){
        Label label = new Label(labelText);
        TextField input = new TextField();
        input.setPromptText(promptText);
        VBox field = new VBox(label, input);
        field.setSpacing(4);
        label.getStyleClass().add("form_label");
        input.getStyleClass().add("form_input");
        field.getStyleClass().add("form_field");
        body.getChildren().add(field);
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
        VBox.setVgrow(root, Priority.ALWAYS);
    }

    private void initializeHeader(){
        TextField idInput = new TextField();
        idInput.setPromptText("ID");

        Button retrieveBtn = new Button("Retrieve");
        Button newRecordBtn = new Button("New Record");

        HBox group = new HBox(idInput, retrieveBtn);
        group.setSpacing(8);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header = new HBox(group, spacer, newRecordBtn);

        header.setFillHeight(true);
        header.getStyleClass().add("form_header");
        header.setStyle("-fx-padding: 10 10 10 10;");
    }

    private void initializeBody(){
        body = new VBox();
        body.setSpacing(12);
        body.getStyleClass().add("form_body");
        body.setStyle("-fx-padding: 20 10 20 10;");
    }

    private void initializeFooter(){
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        footer = new HBox(updateBtn, deleteBtn);
        footer.setSpacing(10);
        footer.getStyleClass().add("form_footer");
        footer.setStyle("-fx-padding: 10 10 10 10;");
    }
}
