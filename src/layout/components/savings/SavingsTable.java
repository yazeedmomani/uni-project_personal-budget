package layout.components.savings;

import db.models.SavingsRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import layout.components.templates.TemplateTable;

import java.util.List;

public class SavingsTable extends TemplateTable<SavingsRecord> {
    public SavingsTable(List<SavingsRecord> data) {
        super(data);
    }

    @Override
    protected List<TableColumn<SavingsRecord, ?>> buildMiddleColumns() {
        TableColumn<SavingsRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getDate() != null ? cell.getValue().getDate().toString() : ""
                )
        );

        TableColumn<SavingsRecord, String> changeCol = new TableColumn<>("Change");
        changeCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getChange()))
        );

        TableColumn<SavingsRecord, String> balanceCol = new TableColumn<>("Balance (JOD)");
        balanceCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getBalance()))
        );

        return List.of(dateCol, changeCol, balanceCol);
    }
    @Override
    protected Number getValueForRowColoring(SavingsRecord record) {
        return record != null ? record.getChange() : null;
    }

    @Override
    protected String getNegativeRowColor() {
        return "#FFCCBC";
    }
}
