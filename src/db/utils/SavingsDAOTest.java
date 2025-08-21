package db.utils;

import db.dao.SavingsDAO;
import db.models.SavingsRecord;

public class SavingsDAOTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running SavingsDAO test...");

        int userId = 1;
        SavingsDAO dao = new SavingsDAO(userId);
        double latestBalance = dao.getLastBalance();

        System.out.println("Latest balance: " + latestBalance);

        // 1) CREATE
        System.out.println("\n== create ==");
        SavingsRecord record = new SavingsRecord("2024-02-10", 25.0, "test insert from console");
        record = dao.create(record);
        System.out.println("created: " + record);

        // 2) GET
        System.out.println("\n== get ==");
        SavingsRecord fetchedRecord = dao.get(record.getId());
        System.out.println("fetched: " + fetchedRecord);

        // 3) UPDATE
        System.out.println("\n== update ==");
        record.setDate("2025-04-13");
        record.setNotes("updated note");
        record.setChange(50.0);
        SavingsRecord updatedRecord = dao.update(record);
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
    }
}
