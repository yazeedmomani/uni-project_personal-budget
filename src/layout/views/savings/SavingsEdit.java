package layout.views.savings;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class SavingsEdit {
    private static FlowPane root;
    private static Label temp;

    public static FlowPane getRoot(){
        temp = new Label("Savings Edit");
        root = new FlowPane(temp);
        return root;
    }
}
