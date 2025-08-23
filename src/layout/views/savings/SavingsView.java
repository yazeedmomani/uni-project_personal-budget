package layout.views.savings;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class SavingsView {
    private static FlowPane root;
    private static Label temp;

    public static FlowPane getRoot(){
        temp = new Label("Savings View");
        root = new FlowPane(temp);
        return root;
    }
}
