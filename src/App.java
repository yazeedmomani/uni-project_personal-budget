import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class App extends Application {
    public void start(Stage primaryStage){
        Scene scene = new Scene(Login.init());
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Budget");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
