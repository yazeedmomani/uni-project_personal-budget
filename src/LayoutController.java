import javafx.scene.layout.BorderPane;

public class LayoutController {
    private static final BorderPane root = new BorderPane();

    public static BorderPane getMainRoot(){return root;}

    public static void viewLogin(){
        root.setTop(null);
        root.setLeft(null);
        root.setCenter(Login.getRoot());
    }

    public static void viewDashboard(){
        root.setTop(TopMenu.getRoot());
        root.setLeft(LeftMenu.getRoot());
        root.setCenter(null);
    }
}
