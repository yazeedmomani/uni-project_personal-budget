package db.models;

import db.models.templates.TemplateSavingsRecord;

import java.time.LocalDate;

public class InvestmentsRecord extends TemplateSavingsRecord {
    public InvestmentsRecord(int id, int userId, LocalDate date, double change, double balance, String notes) {
        super(id, userId, date, change, balance, notes);
        this.date = date;
        this.change = change;
        this.balance = balance;
    }

    public InvestmentsRecord(LocalDate date, double change, String notes) {
        super(date, change, notes);
    }

    @Override
    public String toString(){
        return "InvestmentsRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date.toString() +
                ", change=" + change +
                ", balance=" + balance +
                ", notes='" + notes + '\'' +
                '}';
    }
}
