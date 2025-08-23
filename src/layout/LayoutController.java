package layout;

import app.App;
import javafx.scene.layout.BorderPane;
import layout.menus.LeftMenu;
import layout.views.Login;
import layout.menus.TopMenu;
import layout.views.*;
import layout.views.savings.*;
import layout.views.income.*;

public class LayoutController {
    private static final BorderPane root = new BorderPane();
    private static String currentView;
    private static String currentEditType;
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
        setCurrentEditType(null);

        root.setCenter(Login.getRoot());
    }

    public static void unlock(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());
        root.setCenter(IncomeDashboard.getRoot());
    }

    public static void setCurrentMode(String currentMode){
        LayoutController.currentMode = currentMode;
        //update topmenu
        TopMenu.setButton(currentMode);
        //update leftmenu toggle lower buttons
        //update center (router)
    }

    public static void setCurrentView(String currentView){
        LayoutController.currentView = currentView;
        //update leftmenu
        LeftMenu.selectView(currentView);
        //update center (router)
    }

    public static void setCurrentEditType(String currentEdit){
        LayoutController.currentEditType = currentEdit;
        //update left menu
        //update center
    }
}
