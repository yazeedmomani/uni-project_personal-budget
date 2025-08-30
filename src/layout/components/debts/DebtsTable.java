package layout.components.debts;

import db.models.DebtsRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import layout.components.templates.TemplateTable;

import java.util.List;

public class DebtsTable extends TemplateTable<DebtsRecord> {
    public DebtsTable(List<DebtsRecord> data) {
        super(data);
    }

    @Override
    protected List<TableColumn<DebtsRecord, ?>> buildMiddleColumns() {
        TableColumn<DebtsRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getDate() != null ? cell.getValue().getDate().toString() : ""
                )
        );

        TableColumn<DebtsRecord, String> subscriptionCol = new TableColumn<>("Party");
        subscriptionCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getParty() != null ? cell.getValue().getParty() : ""
                )
        );

        TableColumn<DebtsRecord, String> amountCol = new TableColumn<>("Amount (JOD)");
        amountCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getAmount()))
        );

        return List.of(dateCol, subscriptionCol, amountCol);
    }
}
