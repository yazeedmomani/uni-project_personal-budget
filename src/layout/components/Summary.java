package layout.components;

import javafx.scene.control.Label;

public class Summary {
    private Label label;

    public Label getSummary() {return label;}

    public Summary(String summary){
        label= new Label(summary);
        label.getStyleClass().add("summary");
    }
}
