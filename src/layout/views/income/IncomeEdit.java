package layout.views.income;

import db.Database;
import db.models.IncomeRecord;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
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
    private static TextField dateField, sourceField, amountField;
    private static TextArea notesField;

    public static ScrollPane getRoot(){
        form = new Form();

        dateField = form.addField("Date", "YYYY-MM-DD");
        sourceField = form.addField("Source", "Income Source");
        amountField = form.addField("Amount", "0.00");
        notesField = form.addAreaField("Notes", "Notes (optional)");

        card = new DashboardCard(form.getRoot());
        dashbaord = new Dashboard();
        dashbaord.initializeFormSettings();
        dashbaord.add(card, 0, 0, 2, 3);
        return dashbaord.getRoot();
    }
}
