package layout.components.investments;

import db.models.InvestmentsRecord;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import layout.components.templates.TemplateTable;

import java.util.List;

public class InvestmentsTable extends TemplateTable<InvestmentsRecord> {
    public InvestmentsTable(List<InvestmentsRecord> data) {
        super(data);
    }

    @Override
    protected List<TableColumn<InvestmentsRecord, ?>> buildMiddleColumns() {
        TableColumn<InvestmentsRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getDate() != null ? cell.getValue().getDate().toString() : ""
                )
        );

        TableColumn<InvestmentsRecord, String> changeCol = new TableColumn<>("Change (JOD)");
        changeCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getChange()))
        );

        TableColumn<InvestmentsRecord, String> balanceCol = new TableColumn<>("Balance (JOD)");
        balanceCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.format("% .2f", cell.getValue().getBalance()))
        );

        return List.of(dateCol, changeCol, balanceCol);
    }
}
