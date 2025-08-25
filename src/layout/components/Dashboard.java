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
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();

        column1.setPercentWidth(50);
        column1.setHgrow(Priority.ALWAYS);
        column1.setFillWidth(true);

        column2.setPercentWidth(50);
        column2.setHgrow(Priority.ALWAYS);
        column2.setFillWidth(true);

        row3.setVgrow(Priority.ALWAYS);

        grid = new GridPane();

        grid.getColumnConstraints().setAll(column1, column2);
        grid.getRowConstraints().setAll(row1, row2, row3);

        grid.getStyleClass().add("incomeView");
        grid.setVgap(24);
        grid.setHgap(24);
    }
}

