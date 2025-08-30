package db.utils.dao_test;

import db.dao.DebtsDAO;
import db.models.DebtsRecord;

import java.time.LocalDate;
import java.util.List;

/*
Utility that tests DebtsDAO by calling all its methods then restoring the data to its original state.
 */

public class DebtsDAOTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running DebtsDAO test...");

        int userId = 1;
        String type = "Credited";
        DebtsDAO dao = new DebtsDAO(userId);

        // 1) CREATE
        System.out.println("\n== create ==");
        DebtsRecord record = new DebtsRecord(LocalDate.parse("2024-02-10"), "Test Party", 25.0, "test insert from console", type);
        record = dao.create(record);
        System.out.println("created: " + record);

        // 2) GET
        System.out.println("\n== get ==");
        DebtsRecord fetchedRecord = dao.get(record.getId());
        System.out.println("fetched: " + fetchedRecord);

        // 3) UPDATE
        System.out.println("\n== update ==");
        record.setDate(LocalDate.parse("2025-04-13"));
        record.setParty("Updated Party");
        record.setAmount(50.0);
        record.setNotes("updated note");
        DebtsRecord updatedRecord = dao.update(record);
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
        List<DebtsRecord> allRecords = dao.getAll(50, type);
        for (DebtsRecord rec : allRecords) {
            System.out.println(rec);
        }
    }
}
