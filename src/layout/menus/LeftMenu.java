package layout.menus;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LeftMenu {
    private static VBox root;
    private static Button incomeBtn, savingsBtn;

    public static VBox getRoot(){
        incomeBtn = new Button("Income");
        savingsBtn = new Button("Savings");

        incomeBtn.getStyleClass().add("leftMenu_btn");
        incomeBtn.setId("leftMenu_selected");
        savingsBtn.getStyleClass().add("leftMenu_btn");

        root = new VBox(incomeBtn, savingsBtn);
        root.getStyleClass().add("leftMenu");

        incomeBtn.setOnAction(e -> {
            //selectIncome();
        });

        savingsBtn.setOnAction(e -> {
            //selectSavings();
        });

        return root;
    }

    public static void selectView(String view){
        clearSelection();
        if(view.equals("income")) incomeBtn.setId("leftMenu_selected");
        if(view.equals("savings")) savingsBtn.setId("leftMenu_selected");
    }

    private static void clearSelection(){
        savingsBtn.setId(null);
        incomeBtn.setId(null);
    }
}
