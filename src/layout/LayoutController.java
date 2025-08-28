package layout;

import app.App;
import javafx.scene.layout.BorderPane;
import layout.menus.LeftMenu;
import layout.views.Login;
import layout.menus.TopMenu;
import layout.views.income.*;

public class LayoutController {
    private static final BorderPane root = new BorderPane();
    public static enum View {INCOME, SAVINGS};
    public static enum Mode {VIEW, EDIT};
    private static View currentView;
    private static Mode currentMode;
    private static boolean isSettings;


    public static void init(){
        lock();
    }

    public static BorderPane getMainRoot(){return root;}

    public static void lock(){
        root.setTop(null);
        root.setLeft(null);

        if(isSettings) setIsSettings(false);
        setCurrentView(null);
        setCurrentMode(null);
        Router.route();
    }

    public static void unlock(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());

        setIsSettings(false);
        setCurrentView(View.INCOME);
        setCurrentMode(Mode.EDIT); // TODO change to view
        Router.route();
    }

    public static Mode getCurrentMode(){return currentMode;}
    public static View getCurrentView(){return currentView;}
    public static boolean getIsSettings(){return isSettings;}

    public static void setCurrentMode(Mode currentMode){
        LayoutController.currentMode = currentMode;

        if(isSettings) setIsSettings(false);

        TopMenu.setButton(currentMode);
        Router.route();
    }

    public static void setCurrentView(View currentView){
        LayoutController.currentView = currentView;

        if(isSettings) setIsSettings(false);

        LeftMenu.selectView(currentView);
        Router.route();
    }

    public static void setIsSettings(boolean bool){
        isSettings = bool;
        TopMenu.selectSettings(bool);
        Router.route();
    }
}
