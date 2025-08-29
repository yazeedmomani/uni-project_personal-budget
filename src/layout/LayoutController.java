package layout;

import javafx.scene.layout.BorderPane;
import layout.menus.LeftMenu;
import layout.menus.TopMenu;

public class LayoutController {
    private static final BorderPane root = new BorderPane();
    private static LayoutView currentView;
    private static LayoutMode currentMode;
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
        setCurrentView(LayoutView.SAVINGS);
        setCurrentMode(LayoutMode.VIEW);
        Router.route();
    }

    public static LayoutMode getCurrentMode(){return currentMode;}
    public static LayoutView getCurrentView(){return currentView;}
    public static boolean getIsSettings(){return isSettings;}

    public static void setCurrentMode(LayoutMode currentMode){
        LayoutController.currentMode = currentMode;

        if(isSettings) setIsSettings(false);

        TopMenu.setButton(currentMode);
        Router.route();
    }

    public static void setCurrentView(LayoutView currentView){
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
