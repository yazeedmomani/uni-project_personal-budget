package layout;

import app.App;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import layout.menus.LeftMenu;
import layout.menus.TopMenu;
import layout.views.Login;
import layout.views.Settings;
import layout.views.debts_payables.DebtsPayablesEdit;
import layout.views.debts_payables.DebtsPayablesView;
import layout.views.debts_receivables.DebtsReceivablesEdit;
import layout.views.debts_receivables.DebtsReceivablesView;
import layout.views.fixed_expenses.FixedExpensesEdit;
import layout.views.fixed_expenses.FixedExpensesView;
import layout.views.income.IncomeEdit;
import layout.views.income.IncomeView;
import layout.views.investments.InvestmentsEdit;
import layout.views.investments.InvestmentsView;
import layout.views.savings.SavingsEdit;
import layout.views.savings.SavingsView;

public class LayoutController {
    private static final BorderPane root = new BorderPane();
    private static LayoutView currentView;
    private static LayoutMode currentMode;
    private static boolean isSettings;

    // INITIAL
    public static void init(){
        lock();
    }

    // GET ROOT
    public static BorderPane getMainRoot(){return root;}

    // LOCK / UNLOCK
    public static void lock(){
        root.setTop(null);
        root.setLeft(null);

        if(isSettings) setIsSettings(false);
        setCurrentView(null);
        setCurrentMode(null);
        route();
    }

    public static void unlock(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());

        setIsSettings(false);
        setCurrentView(LayoutView.INCOME);
        setCurrentMode(LayoutMode.VIEW);
        route();
    }

    // VIEW/MODE GETTERS
    public static LayoutMode getCurrentMode(){return currentMode;}
    public static LayoutView getCurrentView(){return currentView;}
    public static boolean getIsSettings(){return isSettings;}

    // VIEW/MODE SETTERS
    public static void setCurrentMode(LayoutMode currentMode){
        LayoutController.currentMode = currentMode;

        if(isSettings) setIsSettings(false);

        TopMenu.setButton(currentMode);
        route();
    }

    public static void setCurrentView(LayoutView currentView){
        LayoutController.currentView = currentView;

        if(isSettings) setIsSettings(false);

        LeftMenu.selectView(currentView);
        route();
    }

    public static void setIsSettings(boolean bool){
        isSettings = bool;
        TopMenu.selectSettings(bool);
        route();
    }

    // ROUTER
    private static void route(){
        root.setCenter(null);

        if(isSettings){
            setWindow("Budget - Settings", Settings.getRoot());
            return;
        }

        if(currentView == null || currentMode == null){
            setWindow("Budget - Login", Login.getRoot());
            return;
        }

        IncomeEdit incomeEdit = new IncomeEdit();
        IncomeView incomeView = new IncomeView();
        DebtsReceivablesEdit debtsReceivablesEdit = new DebtsReceivablesEdit();
        DebtsReceivablesView debtsReceivablesView = new DebtsReceivablesView();
        SavingsEdit savingsEdit = new SavingsEdit();
        SavingsView savingsView = new SavingsView();
        InvestmentsEdit investmentsEdit = new InvestmentsEdit();
        InvestmentsView investmentsView = new InvestmentsView();
        FixedExpensesEdit fixedExpensesEdit = new FixedExpensesEdit();
        FixedExpensesView fixedExpensesView = new FixedExpensesView();
        DebtsPayablesEdit debtsPayablesEdit = new DebtsPayablesEdit();
        DebtsPayablesView debtsPayablesView = new DebtsPayablesView();

        switch (currentView){
            case LayoutView.DASHBAORD:
                setDashboardWindow("Dashboard", incomeView.getRoot(), incomeView.getRoot());
                break;

            case LayoutView.INCOME:
                setDashboardWindow("Income", incomeView.getRoot(), incomeEdit.getRoot());
                break;

            case LayoutView.RECEIVABLES:
                setDashboardWindow("Receivables", debtsReceivablesView.getRoot(), debtsReceivablesEdit.getRoot());
                break;

            case LayoutView.SAVINGS:
                setDashboardWindow("Savings", savingsView.getRoot(), savingsEdit.getRoot());
                break;

            case LayoutView.INVESTMENTS:
                setDashboardWindow("Investments", investmentsView.getRoot(), investmentsEdit.getRoot());
                break;

            case LayoutView.FIXED_EXPENSES:
                setDashboardWindow("Fixed Expenses", fixedExpensesView.getRoot(), fixedExpensesEdit.getRoot());
                break;

            case LayoutView.PAYABLES:
                setDashboardWindow("Payables", debtsPayablesView.getRoot(), debtsPayablesEdit.getRoot());
                break;
        }
    }

    private static void setDashboardWindow(String title, Node view, Node mode){
        String adjustedTitle = "Budget - " + title;

        if(currentMode.equals(LayoutMode.VIEW))
            setWindow(adjustedTitle + " (View)", view);

        if(currentMode.equals(LayoutMode.EDIT))
            setWindow(adjustedTitle + " (Edit)", mode);
    }

    private static void setWindow(String title, Node view){
        App.setWindowTitle(title);
        root.setCenter(view);
    }
}
