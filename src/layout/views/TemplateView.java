package layout.views;

import db.dao.TemplateDAO;
import db.models.TemplateRecord;
import javafx.scene.control.ScrollPane;
import layout.components.Dashboard;

import java.util.Collections;
import java.util.List;

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

    protected abstract void initializeDashboardCards();
}
