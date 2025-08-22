import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LeftMenu {
    private static VBox root;
    private static Button incomeBtn, savingsBtn;

    public static VBox getRoot(){
        incomeBtn = new Button("Income");
        savingsBtn = new Button("Savings");

        incomeBtn.getStyleClass().add("leftMenu_btn");
        savingsBtn.getStyleClass().add("leftMenu_btn");

        root = new VBox(incomeBtn, savingsBtn);
        root.getStyleClass().add("leftMenu");

        return root;
    }
}
