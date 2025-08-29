package layout.components.dashboard;

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

    public void setTItleStyle(String style){this.title.setStyle(style);}

    public DashboardCard(String title, Node node){
        initializeTitle();
        setTitle(title);
        root = new VBox(node);
        initializeRootSettings();
    }

    public DashboardCard(Node... nodes){
        initializeTitle();
        root = new VBox(nodes);
        initializeRootSettings();
    }

    private void initializeTitle(){
        title = new Label();
        title.getStyleClass().add("dashboardCard_title");
    }

    private void initializeRootSettings(){
        root.getChildren().addFirst(title);
        root.getStyleClass().add("dashboardCard");
        root.setMaxWidth(Double.MAX_VALUE);
        root.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(root, Priority.ALWAYS);
    }
}
