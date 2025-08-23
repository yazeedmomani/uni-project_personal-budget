package layout.views.income;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class IncomeUpdate {
    private static FlowPane root = new FlowPane();
    private static Label temp = new Label("Income Update");

    public static FlowPane getRoot(){
        root.getChildren().add(temp);
        return root;
    }
}
