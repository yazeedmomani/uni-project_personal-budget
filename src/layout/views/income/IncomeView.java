package layout.views.income;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class IncomeView {
    private static GridPane root;
    private static Label summaryLabel1, summaryLabel2;
    private static Label summaryContent1, summaryContent2;
    private static VBox dashboardCard1, dashboardCard2;

    public static GridPane getRoot(){
        summaryLabel1 = new Label("Income This Month");
        summaryLabel1.getStyleClass().add("summaryLabel");
        summaryContent1 = new Label("JOD 240.00");
        summaryContent1.getStyleClass().add("summaryContent");

        summaryLabel2 = new Label("Income Last Month");
        summaryLabel2.getStyleClass().add("summaryLabel");
        summaryContent2 = new Label("JOD 256.00");
        summaryContent2.getStyleClass().add("summaryContent");

        dashboardCard1 = new VBox(summaryLabel1, summaryContent1);
        dashboardCard1.getStyleClass().add("dashboardCard");
        dashboardCard2 = new VBox(summaryLabel2, summaryContent2);
        dashboardCard2.getStyleClass().add("dashboardCard");

        root = new GridPane();
        // Add two columns, 50% width each, fill width
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        c1.setHgrow(Priority.ALWAYS);
        c1.setFillWidth(true);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        c2.setHgrow(Priority.ALWAYS);
        c2.setFillWidth(true);
        root.getColumnConstraints().setAll(c1, c2);

        // Ensure cards expand horizontally
        dashboardCard1.setMaxWidth(Double.MAX_VALUE);
        dashboardCard2.setMaxWidth(Double.MAX_VALUE);

        // Each card in its own column
        root.add(dashboardCard1, 0, 0);
        root.add(dashboardCard2, 1, 0);
        root.getStyleClass().add("incomeView");
        root.setVgap(24);
        root.setHgap(24);

        GridPane.setHgrow(dashboardCard1, Priority.ALWAYS);
        GridPane.setHgrow(dashboardCard2, Priority.ALWAYS);

        return root;
    }
}
