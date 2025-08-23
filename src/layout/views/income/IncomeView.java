package layout.views.income;

import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class IncomeView {
    private static GridPane root;
    private static Label title;
    private static HBox dashboardCard;

    public static GridPane getRoot(){
        dashboardCard = new HBox(title);
        dashboardCard.getStyleClass().add("dashboardCard");

        root = new GridPane();
        root.add(dashboardCard, 0, 0);
        root.getStyleClass().add("incomeView");

        GridPane.setHgrow(dashboardCard, Priority.ALWAYS);
        GridPane.setVgrow(dashboardCard, Priority.ALWAYS);

        return root;
    }
}
