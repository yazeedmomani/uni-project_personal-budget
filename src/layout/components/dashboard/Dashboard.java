package layout.components.dashboard;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class Dashboard {
    private ScrollPane root;
    private GridPane grid;
    private ColumnConstraints column1, column2;
    private RowConstraints row1, row2, row3;

    public ScrollPane getRoot(){
        return root;
    }

    public void add(DashboardCard card, int column, int row){
        grid.add(card.getRoot(), column, row);
    }

    public void add(DashboardCard card, int column, int row, int columnSpan, int rowSpan){
        grid.add(card.getRoot(), column, row, columnSpan, rowSpan);
    }

    public Dashboard(){
        initializeGrid();
        initializeRoot();
    }

    private void initializeRoot(){
        root = new ScrollPane(grid);
        root.setFitToWidth(true);
        root.setFitToHeight(false);
    }

    private void initializeGrid(){
        column1 = createColumn();
        column2 = createColumn();
        row1 = createRow();
        row2 = createRow();
        row3 = createRow();

        grid = new GridPane();

        grid.getColumnConstraints().setAll(column1, column2);
        grid.getRowConstraints().setAll(row1, row2, row3);

        grid.getStyleClass().add("dashboard");
        grid.setVgap(24);
        grid.setHgap(24);
    }

    private ColumnConstraints createColumn(){
        ColumnConstraints column = new ColumnConstraints();

        column.setPercentWidth(50);
        column.setHgrow(Priority.ALWAYS);
        column.setFillWidth(true);

        return column;
    }

    private RowConstraints createRow(){
        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);

        return row;
    }
}

