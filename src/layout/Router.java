package layout;

import app.App;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import layout.views.Login;
import layout.views.Settings;
import layout.views.income.IncomeEdit;
import layout.views.income.IncomeView;
import layout.views.savings.SavingsEdit;
import layout.views.savings.SavingsView;

public class Router {
    private static String currentView;
    private static String currentMode;
    private static boolean viewSettings;

    public static void route(){
        BorderPane root = LayoutController.getMainRoot();
        currentView = LayoutController.getCurrentView() == null ? "" : LayoutController.getCurrentView();
        currentMode = LayoutController.getCurrentMode() == null ? "" : LayoutController.getCurrentMode();
        viewSettings = LayoutController.getViewSettings();

        root.setCenter(null);

        if(viewSettings){
            App.setWindowTitle("Budget - Settings");
            root.setCenter(Settings.getRoot());
            return;
        }

        String route = currentView + "/" + currentMode;
        switch (route){
            case "/":
                App.setWindowTitle("Budget - Login");
                root.setCenter(Login.getRoot());
                break;
            case "income/view":
                App.setWindowTitle("Budget - Income (View)");
                root.setCenter(IncomeView.getRoot());
                break;
            case "income/edit":
                App.setWindowTitle("Budget - Income (Edit)");
                root.setCenter(IncomeEdit.getRoot());
                break;
            case "savings/view":
                App.setWindowTitle("Budget - Savings (View)");
                root.setCenter(SavingsView.getRoot());
                break;
            case "savings/edit":
                App.setWindowTitle("Budget - Savings (Edit)");
                root.setCenter(SavingsEdit.getRoot());
                break;
        }
    }
}
