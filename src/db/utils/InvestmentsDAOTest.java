package db.utils;

import db.dao.InvestmentsDAO;
import db.dao.SavingsDAO;
import db.models.InvestmentsRecord;

import java.time.LocalDate;
import java.util.List;

/*
Utility that tests InvestmentsDAO by calling all its methods then restoring the data to its original state.
 */

public class InvestmentsDAOTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running InvestmentsDAO test...");

        int userId = 1;
        InvestmentsDAO dao = new InvestmentsDAO(userId);
        double latestBalance = dao.getLastBalance();

        System.out.println("Latest balance: " + latestBalance);

        // 1) CREATE
        System.out.println("\n== create ==");
        InvestmentsRecord record = new InvestmentsRecord(LocalDate.parse("2024-02-10"), 25.0, "test insert from console");
        record = dao.create(record);
        System.out.println("created: " + record);

        // 2) GET
        System.out.println("\n== get ==");
        InvestmentsRecord fetchedRecord = dao.get(record.getId());
        System.out.println("fetched: " + fetchedRecord);

        // 3) UPDATE
        System.out.println("\n== update ==");
        record.setDate(LocalDate.parse("2025-04-13"));
        record.setNotes("updated note");
        record.setChange(50.0);
        InvestmentsRecord updatedRecord = dao.update(record);
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
        List<InvestmentsRecord> allRecords = dao.getAll(50);
        for (InvestmentsRecord rec : allRecords) {
            System.out.println(rec);
        }
    }
}
