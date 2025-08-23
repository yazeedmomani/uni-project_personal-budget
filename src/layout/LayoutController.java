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

    public static void init(){
        lock();
    }

    public static BorderPane getMainRoot(){return root;}

    public static void lock(){
        App.setWindowTitle("Budget - Login");
        root.setTop(null);
        root.setLeft(null);
        root.setCenter(Login.getRoot());
    }

    public static void unlock(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());
        root.setCenter(IncomeDashboard.getRoot());
    }
}
