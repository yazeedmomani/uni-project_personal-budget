package db.models.templates;

public abstract class TemplateRecord {
    protected final int id;
    protected final int userId;
    protected String notes;

    protected TemplateRecord(){
        this.id = -1;
        this.userId = -1;
    }

    protected TemplateRecord(int id, int userId, String notes) {
        this.id = id;
        this.userId = userId;
        this.notes = notes;
    }

    protected TemplateRecord(String notes) {
        this.id = -1;
        this.userId = -1;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getNotes() { return notes; }

    public void setNotes(String notes) {this.notes = notes;}

    @Override
    public abstract String toString();
}
