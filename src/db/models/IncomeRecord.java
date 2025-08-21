package db.models;

import db.Database;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class IncomeRecord {
    private final int id;
    private final int userId;
    private String date;
    private String source;
    private double amount;
    private String notes;

    public IncomeRecord(int id, int userId, String date, String source, double amount, String notes) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.source = source;
        this.amount = amount;
        this.notes = notes;
    }

    public IncomeRecord(String date, String source, double amount, String notes) {
        this.id = -1;
        this.userId = -1;
        this.date = date;
        this.source = source;
        this.amount = amount;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public String getSource() { return source; }
    public double getAmount() { return amount; }
    public String getNotes() { return notes; }

    public void setDate(String date){
        try {
            LocalDate parsed = LocalDate.parse(date, Database.getDateFormat());
            this.date = parsed.toString();

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Date must be in format yyyy-MM-dd, e.g. 2024-02-10"
            );
        }
    }
    public void setSource(String source) {this.source = source;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setNotes(String notes) {this.notes = notes;}

    @Override
    public String toString(){
        return "IncomeRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", source'=" + source + '\'' +
                ", amount=" + amount +
                ", notes='" + notes + '\'' +
                '}';
    }
}
