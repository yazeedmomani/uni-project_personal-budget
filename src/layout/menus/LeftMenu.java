package layout.menus;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import layout.LayoutController;

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

        incomeBtn.setOnAction(e -> {
            LayoutController.setCurrentView("income");
        });

        savingsBtn.setOnAction(e -> {
            LayoutController.setCurrentView("savings");
        });

        return root;
    }

    public static void selectView(String view){
        if(view == null) return;

        clearSelection();
        if(view.equals("income")) incomeBtn.setId("leftMenu_selected");
        if(view.equals("savings")) savingsBtn.setId("leftMenu_selected");
    }

    private static void clearSelection(){
        savingsBtn.setId(null);
        incomeBtn.setId(null);
    }
}
