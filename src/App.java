import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    public void start(Stage primaryStage){
        logout();
        BorderPane root = LayoutController.getMainRoot();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Budget");
        primaryStage.show();
    }

    public static void logout(){
        LayoutController.viewLogin();
    }

    public static void login(){
        LayoutController.viewDashboard();
    }

    public static void main(String[] args){
        launch(args);
    }
}
