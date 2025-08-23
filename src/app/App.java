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
        LayoutController.lock();
        Database.setCurrentUser(null);
    }

    public static void login(User user){
        LayoutController.unlock();
        Database.setCurrentUser(user);
    }

    public static void setWindowTitle(String title){
        primaryStage.setTitle(title);
    }
}
