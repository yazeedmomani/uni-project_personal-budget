package db.utils;

import db.dao.IncomeDAO;
import db.models.IncomeRecord;
import java.time.LocalDate;
import java.util.List;

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
        IncomeRecord record = new IncomeRecord(LocalDate.parse("2024-02-10"), "Test Source", 25.0, "test insert from console");
        record = dao.create(record);
        System.out.println("created: " + record);

        // 2) GET
        System.out.println("\n== get ==");
        IncomeRecord fetchedRecord = dao.get(record.getId());
        System.out.println("fetched: " + fetchedRecord);

        // 3) UPDATE
        System.out.println("\n== update ==");
        record.setDate(LocalDate.parse("2025-04-13"));
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

        // 6) GET ALL LIMIT 100
        System.out.println("\n== getAll (Limit 50) ==");
        List<IncomeRecord> allRecords = dao.getAll(50);
        for (IncomeRecord rec : allRecords) {
            System.out.println(rec);
        }
    }
}
