package app;

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
    }

    public static void login(){
        LayoutController.unlock();
    }

    public static void setWindowTitle(String title){
        primaryStage.setTitle(title);
    }
}
