package layout.components.templates;

import db.models.templates.TemplateRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public abstract class TemplateTable<R extends TemplateRecord> {
    private Pagination table;
    private final List<R> data;
    private static final int ROWS_PER_PAGE = 12;

    public TemplateTable(List<R> data) {
        this.data = data;
        this.table = initializeTable();
    }

    public Pagination getTable() {
        return table;
    }

    private Pagination initializeTable() {
        TableView<R> table = buildTableSkeleton();
        table.setMinHeight(393);
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);

        int pageCount = Math.max(1, (int) Math.ceil(data.size() / (double) ROWS_PER_PAGE));
        Pagination pagination = new Pagination(pageCount, 0);
        pagination.getStyleClass().add("pagination");
        pagination.setId("pagination");
        pagination.setPrefHeight(420);
        VBox.setVgrow(pagination, Priority.ALWAYS);
        pagination.setMaxWidth(Double.MAX_VALUE);

        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ROWS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());
            if (fromIndex > toIndex) {
                fromIndex = 0;
                toIndex = Math.min(ROWS_PER_PAGE, data.size());
            }
            ObservableList<R> page = FXCollections.observableArrayList(
                    data.subList(fromIndex, toIndex)
            );
            table.setItems(page);
            return table;
        });

        this.table = pagination;
        return pagination;
    }

    private TableView<R> buildTableSkeleton() {
        TableView<R> tv = new TableView<>();
        tv.getStyleClass().add("table");
        tv.setId("table");
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<R, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                String.valueOf(cell.getValue().getId())
        ));
        idCol.getStyleClass().add("col-id");

        TableColumn<R, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getNotes() != null ? cell.getValue().getNotes() : ""
        ));
        notesCol.getStyleClass().add("col-notes");

        tv.getColumns().add(idCol);
        List<TableColumn<R, ?>> middle = buildMiddleColumns();
        if (middle != null && !middle.isEmpty()) {
            tv.getColumns().addAll(middle);
        }
        tv.getColumns().add(notesCol);

        tv.setMaxWidth(Double.MAX_VALUE);
        tv.setPrefHeight(388);

        // Color rows whose target value is negative; color & value provider are defined by subclasses
        tv.setRowFactory(tableView -> new TableRow<R>() {
            @Override
            protected void updateItem(R item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                    return;
                }
                Number v = getValueForRowColoring(item);
                if (v != null && v.doubleValue() < 0) {
                    String color = getNegativeRowColor();
                    if (color != null && !color.isEmpty()) {
                        setStyle("-fx-background-color: " + color + ";");
                    } else {
                        setStyle("");
                    }
                } else {
                    setStyle("");
                }
            }
        });

        return tv;
    }

    protected abstract List<TableColumn<R, ?>> buildMiddleColumns();

    /**
     * Return the numeric value used to decide row coloring. If this value is < 0, the row will be colored.
     */
    protected abstract Number getValueForRowColoring(R record);

    /**
     * Return a JavaFX CSS color value (e.g., "#FFCDD2" or "rgba(255,0,0,0.2)") for rows where the value < 0.
     */
    protected abstract String getNegativeRowColor();
}
