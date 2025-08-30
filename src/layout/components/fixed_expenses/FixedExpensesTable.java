package layout.components.fixed_expenses;

import db.models.SubscriptionsRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import layout.components.templates.TemplateTable;

import java.util.List;

public class FixedExpensesTable extends TemplateTable<SubscriptionsRecord> {
    public FixedExpensesTable(List<SubscriptionsRecord> data) {
        super(data);
    }

    @Override
    protected List<TableColumn<SubscriptionsRecord, ?>> buildMiddleColumns() {
        TableColumn<SubscriptionsRecord, String> subscriptionCol = new TableColumn<>("Bill");
        subscriptionCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getSubscription() != null ? cell.getValue().getSubscription() : ""
                )
        );

        TableColumn<SubscriptionsRecord, String> amountCol = new TableColumn<>("Amount (JOD)");
        amountCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getAmount()))
        );

        TableColumn<SubscriptionsRecord, String> expectedDayCol = new TableColumn<>("Due Day");
        expectedDayCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("%d", cell.getValue().getExpectedDay()))
        );

        return List.of(subscriptionCol, amountCol, expectedDayCol);
    }
}
