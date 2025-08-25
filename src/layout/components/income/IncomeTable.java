package layout.components.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class IncomeTable {
    private static Pagination table;
    private static List<IncomeRecord> data;
    private static final int ROWS_PER_PAGE = 15;

    public static Pagination init(List<IncomeRecord> data){
        IncomeTable.data = data;
        return initializeTable();
    }

    private static Pagination initializeTable() {
        TableView<IncomeRecord> table = buildTableSkeleton();
        table.setMinHeight(388);
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);

        int pageCount = Math.max(1, (int) Math.ceil(data.size() / (double) ROWS_PER_PAGE));
        Pagination pagination = new Pagination(pageCount, 0);
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
            ObservableList<IncomeRecord> page = FXCollections.observableArrayList(
                    data.subList(fromIndex, toIndex)
            );
            table.setItems(page);
            return table;
        });

        return pagination;
    }

    private static TableView<IncomeRecord> buildTableSkeleton() {
        TableView<IncomeRecord> tv = new TableView<>();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<IncomeRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell -> {
            LocalDate d = cell.getValue().getDate();
            return new ReadOnlyStringWrapper(d != null ? d.toString() : "");
        });

        TableColumn<IncomeRecord, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getSource() != null ? cell.getValue().getSource() : ""
        ));

        TableColumn<IncomeRecord, String> amountCol = new TableColumn<>("Amount (JOD)");
        amountCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                String.format("% .2f", cell.getValue().getAmount())
        ));

        TableColumn<IncomeRecord, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getNotes() != null ? cell.getValue().getNotes() : ""
        ));

        tv.getColumns().setAll(dateCol, sourceCol, amountCol, notesCol);
        tv.setMaxWidth(Double.MAX_VALUE);
        tv.setPrefHeight(388);
        return tv;
    }
}
