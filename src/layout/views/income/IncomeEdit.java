package layout.views.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import layout.components.Dashboard;
import layout.components.DashboardCard;
import layout.components.Form;
import layout.components.income.IncomeBarChart;

import java.util.Collections;
import java.util.List;

public class IncomeEdit {
    private static Dashboard dashbaord;
    private static DashboardCard card;
    private static Form form;

    public static ScrollPane getRoot(){
        form = new Form();

        form.addField("Date", "YYYY-MM-DD");
        form.addField("Source", "Income Source");
        form.addField("Amount", "0.00");
        form.addAreaField("Notes", "Notes (optional)");

        card = new DashboardCard(form.getRoot());
        dashbaord = new Dashboard();
        dashbaord.initializeFormSettings();
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }
}
