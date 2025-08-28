package db.models;

import db.Database;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SavingsRecord extends TemplateRecord{
    private LocalDate date;
    private double change;
    private double balance;

    public SavingsRecord(int id, int userId, LocalDate date, double change, double balance, String notes) {
        super(id, userId, notes);
        this.date = date;
        this.change = change;
        this.balance = balance;
    }

    public SavingsRecord(LocalDate date, double change, String notes) {
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
    public String toString(){
        return "SavingsRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date.toString() +
                ", change=" + change +
                ", balance=" + balance +
                ", notes='" + notes + '\'' +
                '}';
    }
}
