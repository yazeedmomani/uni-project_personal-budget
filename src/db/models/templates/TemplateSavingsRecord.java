package db.models.templates;

import java.time.LocalDate;

public abstract class TemplateSavingsRecord extends TemplateRecord{
    protected LocalDate date;
    protected double change;
    protected double balance;

    protected TemplateSavingsRecord(int id, int userId, LocalDate date, double change, double balance, String notes) {
        super(id, userId, notes);
        this.date = date;
        this.change = change;
        this.balance = balance;
    }

    protected TemplateSavingsRecord(LocalDate date, double change, String notes) {
        super(notes);
        this.date = date;
        this.change = change;
        this.balance = -1;
    }

    public LocalDate getDate() { return date; }
    public double getChange() { return change; }
    public double getBalance() { return balance; }

    public void setDate(LocalDate date) {this.date = date;}
    public void setChange(double change) {
        this.balance = this.balance + (change - this.change);
        this.change = change;
    }

    @Override
    public abstract String toString();
}
