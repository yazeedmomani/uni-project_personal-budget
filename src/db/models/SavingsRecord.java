package db.models;

import db.Database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SavingsRecord {
    private final int id;
    private final int userId;
    private String date;
    private double change;
    private double balance;
    private String notes;

    public SavingsRecord(int id, int userId, String date, double change, double balance, String notes) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.change = change;
        this.balance = balance;
        this.notes = notes;
    }

    public SavingsRecord(String date, double change, String notes) {
        this.id = -1;
        this.userId = -1;
        this.date = date;
        this.change = change;
        this.balance = -1;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public double getChange() { return change; }
    public double getBalance() { return balance; }
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
    public void setChange(double change) {
        this.balance = this.balance + (change - this.change);
        this.change = change;
    }
    public void setNotes(String notes) {this.notes = notes;}

    @Override
    public String toString(){
        return "SavingsRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", change=" + change +
                ", balance=" + balance +
                ", notes='" + notes + '\'' +
                '}';
    }
}
