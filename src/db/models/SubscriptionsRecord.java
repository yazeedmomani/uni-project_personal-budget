package db.models;

public class SubscriptionsRecord extends TemplateRecord {
    private String subscription;
    private double amount;
    private int expectedDay;

    public SubscriptionsRecord(){
        super();
    }

    public SubscriptionsRecord(int id, int userId, String subscription, double amount, int expectedDay, String notes) {
        super(id, userId, notes);
        this.subscription = subscription;
        this.amount = amount;
        this.expectedDay = expectedDay;
    }

    public SubscriptionsRecord(String subscription, double amount, int expectedDay, String notes) {
        super(notes);
        this.subscription = subscription;
        this.amount = amount;
        this.expectedDay = expectedDay;
    }

    public String getSubscription() { return subscription; }
    public double getAmount() { return amount; }
    public int getExpectedDay() { return expectedDay;}

    public void setSubscription(String subscription) {this.subscription = subscription;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setExpectedDay(int expectedDay) {this.expectedDay = expectedDay;}

    @Override
    public String toString(){
        return "SubscriptionsRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", subcription='" + subscription + '\'' +
                ", amount=" + amount +
                ", expected_day=" + expectedDay +
                ", notes='" + notes + '\'' +
                '}';
    }
}
