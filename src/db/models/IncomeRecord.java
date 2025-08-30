package db.models;

import db.models.templates.TemplateRecord;

import java.time.LocalDate;

public class IncomeRecord extends TemplateRecord {
    private LocalDate date;
    private String source;
    private double amount;

    public IncomeRecord(){
        super();
    }

    public IncomeRecord(int id, int userId, LocalDate date, String source, double amount, String notes) {
        super(id, userId, notes);
        this.date = date;
        this.source = source;
        this.amount = amount;
    }

    public IncomeRecord(LocalDate date, String source, double amount, String notes) {
        super(notes);
        this.date = date;
        this.source = source;
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public String getSource() { return source; }
    public double getAmount() { return amount; }

    public void setDate(LocalDate date) {this.date = date;}
    public void setSource(String source) {this.source = source;}
    public void setAmount(double amount) {this.amount = amount;}

    @Override
    public String toString(){
        return "IncomeRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date.toString() + '\'' +
                ", source='" + source + '\'' +
                ", amount=" + amount +
                ", notes='" + notes + '\'' +
                '}';
    }
}
