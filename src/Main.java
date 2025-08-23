import app.App;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import layout.LayoutController;

public class Main extends Application {
    public void start(Stage primaryStage){
        App.init(primaryStage);

        BorderPane root = LayoutController.getMainRoot();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
