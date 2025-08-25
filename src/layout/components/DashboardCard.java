package layout.components;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardCard {
    private VBox root;

    public VBox getRoot(){return root;}

    public DashboardCard(Node... nodes){
        root = new VBox(nodes);

        root.getStyleClass().add("dashboardCard");
        root.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(root, Priority.ALWAYS);
    }
}
