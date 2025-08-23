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


    public static void init(){
        lock();
    }

    public static BorderPane getMainRoot(){return root;}

    public static void lock(){
        App.setWindowTitle("Budget - Login");
        root.setTop(null);
        root.setLeft(null);
        setCurrentView(null);
        setCurrentMode(null);

        root.setCenter(Login.getRoot());
    }

    public static void unlock(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());
        setCurrentView("income");
        setCurrentMode("view");

        root.setCenter(null);
    }

    public static void setCurrentMode(String currentMode){
        LayoutController.currentMode = currentMode;
        TopMenu.setButton(currentMode);

        //update center (router)
    }

    public static void setCurrentView(String currentView){
        LayoutController.currentView = currentView;
        LeftMenu.selectView(currentView);

        //update center (router)
    }
}
