package layout.components.income;

import db.models.IncomeRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import layout.components.TemplateTable;

import java.util.List;

public class IncomeTable extends TemplateTable<IncomeRecord> {
    public IncomeTable(List<IncomeRecord> data) {
        super(data);
    }

    @Override
    protected List<TableColumn<IncomeRecord, ?>> buildMiddleColumns() {
        TableColumn<IncomeRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getDate() != null ? cell.getValue().getDate().toString() : ""
                )
        );

        TableColumn<IncomeRecord, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getSource() != null ? cell.getValue().getSource() : ""
                )
        );

        TableColumn<IncomeRecord, String> amountCol = new TableColumn<>("Amount (JOD)");
        amountCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getAmount()))
        );

        return List.of(dateCol, sourceCol, amountCol);
    }
}
