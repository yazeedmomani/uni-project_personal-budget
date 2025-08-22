import javafx.scene.layout.BorderPane;

import javax.swing.border.Border;

public class LayoutController {
    private static final BorderPane root = new BorderPane();

    public static BorderPane getMainRoot(){return root;}

    public static void viewLogin(){
        root.setTop(null);
        root.setLeft(null);
        root.setCenter(Login.getRoot());
    }

    public static void viewDashboard(){
        root.setTop(Menu.getRoot());
        root.setLeft(null);
        root.setCenter(null);
    }
}
