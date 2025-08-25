package layout.components;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class Dashboard {
    private ScrollPane root;
    private GridPane grid;

    public ScrollPane getRoot(){
        return root;
    }

    public void add(Node component, int column, int row){
        grid.add(component, column, row);
        GridPane.setHgrow(component, Priority.ALWAYS);
    }

    public void add(Node component, int column, int row, int columnSpan, int rowSpan){
        grid.add(component, column, row, columnSpan, rowSpan);
        GridPane.setHgrow(component, Priority.ALWAYS);
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
        ColumnConstraints column1 = createColumn();
        ColumnConstraints column2 = createColumn();
        RowConstraints row1 = createRow();
        RowConstraints row2 = createRow();
        RowConstraints row3 = createRow();

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

