package layout.views.income;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class IncomeEdit {
    private static FlowPane root;
    private static Label temp;

    public static FlowPane getRoot(){
        temp = new Label("Income Edit");
        root = new FlowPane(temp);
        return root;
    }
}
