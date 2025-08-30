package layout.views.templates;

import db.dao.templates.TemplateDAO;
import db.models.templates.TemplateRecord;
import javafx.scene.control.ScrollPane;
import layout.components.dashboard.Dashboard;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class TemplateView<Record extends TemplateRecord, DAO extends TemplateDAO<Record>> {
    protected Dashboard dashboard;
    protected DAO dao;
    protected List<Record> data;

    protected TemplateView(DAO dao){
        this.dao = dao;
        initializeData();
        dashboard = new Dashboard();
        initializeDashboardCards();
    }

    public ScrollPane getRoot(){
        return dashboard.getRoot();
    }

    private void initializeData(){
        try{
            data = dao.getAll();
        }
        catch (Exception e){
            System.out.println("View Error: " + e.getMessage());
        }
        if (data == null) data = Collections.emptyList();
    }

    protected String formatJOD(double amount){
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return "JOD " + nf.format(Math.round(amount));
    }

    protected abstract void initializeDashboardCards();
}
