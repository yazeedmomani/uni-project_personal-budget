import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class App extends Application {
    public void start(Stage primaryStage){
        Label l1 = new Label("Test");

        FlowPane root = new FlowPane(l1);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Test");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
