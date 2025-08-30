package layout.components.debts;

import db.models.DebtsRecord;
import db.models.IncomeRecord;
import db.models.SubscriptionsRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import layout.components.templates.TemplateTable;

import java.util.List;

public class DebtsTable extends TemplateTable<DebtsRecord> {
    private String negativeRowColor;

    public DebtsTable(List<DebtsRecord> data, String negativeRowColor) {
        super(data);
        this.negativeRowColor = negativeRowColor;
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

    @Override
    protected Number getValueForRowColoring(DebtsRecord record) {
        return record != null ? record.getAmount() : null;
    }

    @Override
    protected String getNegativeRowColor() {
        return negativeRowColor;
    }
}
