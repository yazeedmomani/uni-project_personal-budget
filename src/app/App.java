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
