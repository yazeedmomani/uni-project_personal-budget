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
        IncomeView incomeView = new IncomeView();
        SavingsEdit savingsEdit = new SavingsEdit();

        switch (LayoutController.getCurrentView()){
            case LayoutController.View.INCOME:
                setDashboardWindow("Income", incomeView.getRoot(), incomeEdit.getRoot());
                break;

            case LayoutController.View.SAVINGS:
                setDashboardWindow("Savings", SavingsView.getRoot(), savingsEdit.getRoot());
                break;
        }
    }

    private static void setDashboardWindow(String title, Node view, Node mode){
        String adjustedTitle = "Budget - " + title;

        if(LayoutController.getCurrentMode().equals(LayoutController.Mode.VIEW))
            setWindow(adjustedTitle + " (View)", view);

        if(LayoutController.getCurrentMode().equals(LayoutController.Mode.EDIT))
            setWindow(adjustedTitle + " (Edit)", mode);
    }

    private static void setWindow(String title, Node view){
        App.setWindowTitle(title);
        mainRoot.setCenter(view);
    }
}
