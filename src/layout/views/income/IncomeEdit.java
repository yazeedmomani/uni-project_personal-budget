package layout.views.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.income.IncomeBarChart;

import java.util.Collections;
import java.util.List;

public class IncomeEdit {
    private static Dashboard dashbaord;
    private static DashboardCard card;
    // UI fields
    private static HBox header, footer;
    private static GridPane body1;
    private static VBox dateField, sourceField, amountField, notesField;

    public static ScrollPane getRoot(){
        // Header: id input, retrieve, new record
        TextField idInput = new TextField();
        idInput.setPromptText("ID");
        Button retrieveBtn = new Button("Retrieve");
        Button newRecordBtn = new Button("New Record");
        HBox idBox = new HBox(idInput, retrieveBtn);
        idBox.setSpacing(8);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header = new HBox();
        header.getChildren().addAll(idBox, spacer, newRecordBtn);
        header.setSpacing(8);
        HBox.setHgrow(newRecordBtn, Priority.ALWAYS);
        header.setFillHeight(true);
        header.setStyle("-fx-padding: 10 10 10 10;");

        // Body: fields
        // Date field
        Label dateLabel = new Label("Date");
        TextField dateInput = new TextField();
        dateInput.setPromptText("YYYY-MM-DD");
        dateField = new VBox(dateLabel, dateInput);
        dateField.setSpacing(4);

        // Source field
        Label sourceLabel = new Label("Source");
        TextField sourceInput = new TextField();
        sourceInput.setPromptText("Income Source");
        sourceField = new VBox(sourceLabel, sourceInput);
        sourceField.setSpacing(4);

        // Amount field
        Label amountLabel = new Label("Amount");
        TextField amountInput = new TextField();
        amountInput.setPromptText("0.00");
        amountField = new VBox(amountLabel, amountInput);
        amountField.setSpacing(4);

        // Notes field
        Label notesLabel = new Label("Notes");
        javafx.scene.control.TextArea notesInput = new javafx.scene.control.TextArea();
        notesInput.setPromptText("Notes (optional)");
        notesInput.setPrefRowCount(5);
        notesField = new VBox(notesLabel, notesInput);
        notesField.setSpacing(4);

        // Group fields
        VBox leftFields = new VBox(dateField, sourceField, amountField, notesField);
        leftFields.setSpacing(12);
        leftFields.setStyle("-fx-padding: 20 10 20 10;");


        // Footer: update, delete
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        footer = new HBox(updateBtn, deleteBtn);
        footer.setSpacing(10);
        footer.setStyle("-fx-padding: 10 10 10 10;");

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(leftFields);
        root.setBottom(footer);
        root.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(root, Priority.ALWAYS);

        card = new DashboardCard(root);
        dashbaord = new Dashboard();
        dashbaord.initializeFormSettings();
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }
}
