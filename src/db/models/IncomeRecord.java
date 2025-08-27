package db.models;

import db.Database;

import java.time.LocalDate;

public class IncomeRecord {
    private final int id;
    private final int userId;
    private LocalDate date;
    private String source;
    private double amount;
    private String notes;

    public IncomeRecord(){
        this.id = -1;
        this.userId = -1;
    }

    public IncomeRecord(int id, int userId, LocalDate date, String source, double amount, String notes) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.source = source;
        this.amount = amount;
        this.notes = notes;
    }

    public IncomeRecord(LocalDate date, String source, double amount, String notes) {
        this.id = -1;
        this.userId = -1;
        this.date = date;
        this.source = source;
        this.amount = amount;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public String getSource() { return source; }
    public double getAmount() { return amount; }
    public String getNotes() { return notes; }

    public void setDate(LocalDate date) {this.date = date;}
    public void setSource(String source) {this.source = source;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setNotes(String notes) {this.notes = notes;}

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
