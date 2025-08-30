package db.utils;

import db.dao.SubscriptionsDAO;
import db.models.SubscriptionsRecord;

import java.time.LocalDate;
import java.util.List;

/*
Utility that tests SubscriptionsDAO by calling all its methods then restoring the data to its original state.
 */

public class SubscriptionsDAOTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running SubscriptionsDAO test...");

        int userId = 1;
        SubscriptionsDAO dao = new SubscriptionsDAO(userId);

        // 1) CREATE
        System.out.println("\n== create ==");
        SubscriptionsRecord record = new SubscriptionsRecord("Canva", 16.45, 16, "test insert from console");
        record = dao.create(record);
        System.out.println("created: " + record);

        // 2) GET
        System.out.println("\n== get ==");
        SubscriptionsRecord fetchedRecord = dao.get(record.getId());
        System.out.println("fetched: " + fetchedRecord);

        // 3) UPDATE
        System.out.println("\n== update ==");
        record.setSubscription("PowerBI");
        record.setAmount(21.23);
        record.setExpectedDay(19);
        record.setNotes("updated note");
        SubscriptionsRecord updatedRecord = dao.update(record);
        System.out.println("updated: " + updatedRecord);

        // 5) DELETE
        System.out.println("\n== delete ==");
        dao.delete(updatedRecord);
        System.out.println("deleted: " + updatedRecord);

        // Test DELETE
        if(dao.get(updatedRecord.getId()) == null)
            System.out.println("Deleted successfully.");
        else
            System.out.println("Failed to delete.");

        // 6) GET ALL LIMIT 50
        System.out.println("\n== getAll (Limit 50) ==");
        List<SubscriptionsRecord> allRecords = dao.getAll(50);
        for (SubscriptionsRecord rec : allRecords) {
            System.out.println(rec);
        }
    }
}
