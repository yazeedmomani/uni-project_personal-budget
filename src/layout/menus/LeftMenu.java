package layout.menus;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import layout.LayoutController;
import layout.LayoutView;

public class LeftMenu {
    private static VBox root;
    private static Button incomeBtn, receivablesBtn, savingsBtn, investmentsBtn, fixedExpensesBtn, payablesBtn;

    public static void selectView(LayoutView view){
        if(view == null) return;

        clearSelection();
        if(view.equals(LayoutView.INCOME)) incomeBtn.setId("leftMenu_selected");
        if(view.equals(LayoutView.RECEIVABLES)) receivablesBtn.setId("leftMenu_selected");
        if(view.equals(LayoutView.SAVINGS)) savingsBtn.setId("leftMenu_selected");
        if(view.equals(LayoutView.INVESTMENTS)) investmentsBtn.setId("leftMenu_selected");
        if(view.equals(LayoutView.FIXED_EXPENSES)) fixedExpensesBtn.setId("leftMenu_selected");
        if(view.equals(LayoutView.PAYABLES)) payablesBtn.setId("leftMenu_selected");
    }

    public static VBox getRoot(){
        incomeBtn = createButton("Income");
        receivablesBtn = createButton("Receivables");
        savingsBtn = createButton("Savings");
        investmentsBtn = createButton("Investments");
        fixedExpensesBtn = createButton("Monthly Bills");
        payablesBtn = createButton("Payables");

        root = new VBox(incomeBtn, receivablesBtn, savingsBtn, investmentsBtn, fixedExpensesBtn, payablesBtn);
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
        if(source == receivablesBtn) LayoutController.setCurrentView(LayoutView.RECEIVABLES);
        if(source == savingsBtn) LayoutController.setCurrentView(LayoutView.SAVINGS);
        if(source == investmentsBtn) LayoutController.setCurrentView(LayoutView.INVESTMENTS);
        if(source == fixedExpensesBtn) LayoutController.setCurrentView(LayoutView.FIXED_EXPENSES);
        if(source == payablesBtn) LayoutController.setCurrentView(LayoutView.PAYABLES);
    }

    private static void clearSelection(){
        incomeBtn.setId(null);
        receivablesBtn.setId(null);
        savingsBtn.setId(null);
        investmentsBtn.setId(null);
        fixedExpensesBtn.setId(null);
        payablesBtn.setId(null);
    }
}
