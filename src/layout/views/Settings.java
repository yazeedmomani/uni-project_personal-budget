package layout.views;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class Settings {
    private static FlowPane root;
    private static Label temp;

    public static FlowPane getRoot(){
        temp = new Label("Settings");
        root = new FlowPane(temp);
        return root;
    }
}
