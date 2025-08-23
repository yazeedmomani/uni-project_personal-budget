package layout.views.savings;

import javafx.scene.layout.FlowPane;
import javafx.scene.control.*;

public class SavingsInsert {
    private static FlowPane root = new FlowPane();
    private static Label temp = new Label("Savings Insert");

    public static FlowPane getRoot(){
        root.getChildren().add(temp);
        return root;
    }
}
