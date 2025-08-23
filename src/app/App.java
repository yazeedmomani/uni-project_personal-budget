package app;

import db.Database;
import db.models.User;
import javafx.stage.Stage;
import layout.LayoutController;

public class App {
    private static Stage primaryStage;

    public static void init(Stage primaryStage){
        App.primaryStage = primaryStage;

        LayoutController.init();

        // TODO remove this piece
        try{
            User user = Database.getUser("admin", "123456");
            login(user);
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        // TODO *****************

        primaryStage.setMaximized(true);
    }

    public static void logout(){
        Database.setCurrentUser(null);
        LayoutController.lock();
    }

    public static void login(User user){
        Database.setCurrentUser(user);
        LayoutController.unlock();
    }

    public static void setWindowTitle(String title){
        primaryStage.setTitle(title);
    }
}
