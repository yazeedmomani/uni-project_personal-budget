package layout;

import app.App;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import layout.views.Login;
import layout.views.income.IncomeEdit;
import layout.views.income.IncomeView;
import layout.views.savings.SavingsEdit;
import layout.views.savings.SavingsView;

public class Router {
    private static String currentView;
    private static String currentMode;

    public static void route(){
        //BorderPane root = LayoutController.getMainRoot();
        currentView = LayoutController.getCurrentView() == null ? "" : LayoutController.getCurrentView();
        currentMode = LayoutController.getCurrentMode() == null ? "" : LayoutController.getCurrentMode();

        LayoutController.getMainRoot().setCenter(null);

        String route = currentView + "/" + currentMode;
        switch (route){
            case "/":
                App.setWindowTitle("Budget - Login");
                LayoutController.getMainRoot().setCenter(Login.getRoot());
                break;
            case "income/view":
                App.setWindowTitle("Budget - Income (View)");
                LayoutController.getMainRoot().setCenter(IncomeView.getRoot());
                break;
            case "income/edit":
                App.setWindowTitle("Budget - Income (Edit)");
                LayoutController.getMainRoot().setCenter(IncomeEdit.getRoot());
                break;
            case "savings/view":
                App.setWindowTitle("Budget - Savings (View)");
                LayoutController.getMainRoot().setCenter(SavingsView.getRoot());
                break;
            case "savings/edit":
                App.setWindowTitle("Budget - Savings (Edit)");
                LayoutController.getMainRoot().setCenter(SavingsEdit.getRoot());
                break;
        }
    }
}
