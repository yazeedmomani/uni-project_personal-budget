package layout.menus;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import layout.LayoutController;
import layout.LayoutView;

public class LeftMenu {
    private static VBox root;
    private static Button incomeBtn, savingsBtn;

    public static void selectView(LayoutView view){
        if(view == null) return;

        clearSelection();
        if(view.equals(LayoutView.INCOME)) incomeBtn.setId("leftMenu_selected");
        if(view.equals(LayoutView.SAVINGS)) savingsBtn.setId("leftMenu_selected");
    }

    public static VBox getRoot(){
        incomeBtn = createButton("Income");
        savingsBtn = createButton("Savings");

        root = new VBox(incomeBtn, savingsBtn);
        root.getStyleClass().add("leftMenu");

        return root;
    }

    private static Button createButton(String text){
        Button button = new Button(text);
        button.getStyleClass().add("leftMenu_btn");
        button.setOnAction(LeftMenu::handleEvent);
        return button;
    }

    private static void handleEvent(ActionEvent e){
        Object source = e.getSource();

        if(source == incomeBtn) LayoutController.setCurrentView(LayoutView.INCOME);
        if(source == savingsBtn) LayoutController.setCurrentView(LayoutView.SAVINGS);
    }

    private static void clearSelection(){
        savingsBtn.setId(null);
        incomeBtn.setId(null);
    }
}
