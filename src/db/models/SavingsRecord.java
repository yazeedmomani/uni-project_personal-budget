package db.models;

import db.Database;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SavingsRecord extends TemplateSavingsRecord{
    public SavingsRecord(int id, int userId, LocalDate date, double change, double balance, String notes) {
        super(id, userId, date, change, balance, notes);
        this.date = date;
        this.change = change;
        this.balance = balance;
    }

    public SavingsRecord(LocalDate date, double change, String notes) {
        super(date, change, notes);
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
