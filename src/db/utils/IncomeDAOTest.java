package db.utils;

import db.dao.IncomeDAO;
import db.models.IncomeRecord;

/*
Utility that tests IncomeDAO by calling all its methods then restoring the data to its original state.
 */

public class IncomeDAOTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running IncomeDAO test...");

        int userId = 1;
        IncomeDAO dao = new IncomeDAO(userId);

        // 1) CREATE
        System.out.println("\n== create ==");
        IncomeRecord record = new IncomeRecord("2024-02-10", "Test Source", 25.0, "test insert from console");
        record = dao.create(record);
        System.out.println("created: " + record);

        // 2) GET
        System.out.println("\n== get ==");
        IncomeRecord fetchedRecord = dao.get(record.getId());
        System.out.println("fetched: " + fetchedRecord);

        // 3) UPDATE
        System.out.println("\n== update ==");
        record.setDate("2025-04-13");
        record.setSource("Updated Source");
        record.setAmount(50.0);
        record.setNotes("updated note");
        IncomeRecord updatedRecord = dao.update(record);
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
