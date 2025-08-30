package db.models;

import java.time.LocalDate;

public class DebtsRecord extends TemplateRecord{
    private LocalDate date;
    private String party;
    private double amount;
    private final String type;

    public DebtsRecord(String type){
        super();
        this.type = type;
    }

    public DebtsRecord(int id, int userId, LocalDate date, String party, double amount, String notes, String type) {
        super(id, userId, notes);
        this.date = date;
        this.party = party;
        this.amount = amount;
        this.type = type;
    }

    public DebtsRecord(LocalDate date, String party, double amount, String notes, String type) {
        super(notes);
        this.date = date;
        this.party = party;
        this.amount = amount;
        this.type = type;
    }

    public LocalDate getDate() { return date; }
    public String getParty() { return party; }
    public double getAmount() { return amount; }
    public String getType() {return type;}

    public void setDate(LocalDate date) {this.date = date;}
    public void setParty(String source) {this.party = source;}
    public void setAmount(double amount) {this.amount = amount;}

    @Override
    public String toString(){
        return "DebtsRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date.toString() + '\'' +
                ", party='" + party + '\'' +
                ", amount=" + amount +
                ", notes='" + notes + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
