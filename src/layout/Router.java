package layout;

import app.App;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import layout.views.Login;
import layout.views.Settings;
import layout.views.income.*;
import layout.views.savings.*;

public class Router {
    private static boolean viewSettings;
    private static BorderPane mainRoot;

    public static void route(){
        mainRoot = LayoutController.getMainRoot();

        viewSettings = LayoutController.getIsSettings();

        mainRoot.setCenter(null);

        if(viewSettings){
            setWindow("Budget - Settings", Settings.getRoot());
            return;
        }

        if(LayoutController.getCurrentView() == null || LayoutController.getCurrentMode() == null){
            setWindow("Budget - Login", Login.getRoot());
            return;
        }

        IncomeEdit incomeEdit = new IncomeEdit();

        switch (LayoutController.getCurrentView()){
            case LayoutController.View.INCOME:
                if(LayoutController.getCurrentMode().equals(LayoutController.Mode.VIEW))
                    setWindow("Budget - Income (View)", IncomeView.getRoot());
                if(LayoutController.getCurrentMode().equals(LayoutController.Mode.EDIT))
                    setWindow("Budget - Income (Edit)", incomeEdit.getRoot());
                break;

            case LayoutController.View.SAVINGS:
                if(LayoutController.getCurrentMode().equals(LayoutController.Mode.VIEW))
                    setWindow("Budget - Savings (View)", SavingsView.getRoot());
                if(LayoutController.getCurrentMode().equals(LayoutController.Mode.EDIT))
                    setWindow("Budget - Savings (Edit)", SavingsEdit.getRoot());
                break;
        }
    }

    private static void setWindow(String title, Node view){
        App.setWindowTitle(title);
        mainRoot.setCenter(view);
    }
}
