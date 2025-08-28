package layout;

import app.App;
import javafx.scene.layout.BorderPane;
import layout.menus.LeftMenu;
import layout.views.Login;
import layout.menus.TopMenu;
import layout.views.income.*;

public class LayoutController {
    private static final BorderPane root = new BorderPane();
    private static String currentView;
    private static String currentMode;
    private static boolean isSettings;


    public static void init(){
        lock();
    }

    public static BorderPane getMainRoot(){return root;}

    public static void lock(){
        root.setTop(null);
        root.setLeft(null);

        if(isSettings) setisSettings(false);
        setCurrentView(null);
        setCurrentMode(null);
        Router.route();
    }

    public static void unlock(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());

        setisSettings(false);
        setCurrentView("income");
        setCurrentMode("edit"); // TODO change to view
        Router.route();
    }

    public static String getCurrentMode(){return currentMode;}
    public static String getCurrentView(){return currentView;}
    public static boolean getisSettings(){return isSettings;}

    public static void setCurrentMode(String currentMode){
        LayoutController.currentMode = currentMode;

        if(isSettings) setisSettings(false);

        TopMenu.setButton(currentMode);
        Router.route();
    }

    public static void setCurrentView(String currentView){
        LayoutController.currentView = currentView;

        if(isSettings) setisSettings(false);

        LeftMenu.selectView(currentView);
        Router.route();
    }

    public static void setisSettings(boolean bool){
        isSettings = bool;
        TopMenu.selectSettings(bool);
        Router.route();
    }
}
