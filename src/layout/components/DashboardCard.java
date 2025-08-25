package layout.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardCard {
    private VBox root;
    private Label title;

    public VBox getRoot(){return root;}

    public void setTitle(String title){this.title.setText(title);}

    public DashboardCard(Node... nodes){
        title = new Label();
        title.getStyleClass().add("dashboardCard_title");

        root = new VBox(nodes);
        root.getChildren().addFirst(title);

        root.getStyleClass().add("dashboardCard");
        root.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(root, Priority.ALWAYS);


    }
}
