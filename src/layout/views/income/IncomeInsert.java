package layout.views.income;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class IncomeInsert {
    private static FlowPane root = new FlowPane();
    private static Label temp = new Label("Income Insert");

    public static FlowPane getRoot(){
        root.getChildren().add(temp);
        return root;
    }
}
