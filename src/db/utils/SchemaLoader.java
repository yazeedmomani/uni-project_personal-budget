package db.utils;

/*
This class is a utility made to execute schema and seed files.
It creates users table, income_log table, and savings_log
table in the database and fills them with mock data.

This class is made to be run once by the developer during the development phase.
The user shouldn't have access to it, nor run it.
 */

import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SchemaLoader {
    private static final String DB_URL = "jdbc:sqlite:src/db/budget.db";

    public static void main(String[] args) {
        System.out.println("Running SchemaLoader...");

        deleteCurrentDatabase();

        try (Connection connection = DriverManager.getConnection(DB_URL)) {

            runSqlFile(connection, Paths.get("src", "db", "seed", "01_schema.sql"));
            runSqlFile(connection, Paths.get("src", "db", "seed", "02_users.sql"));
            runSqlFile(connection, Paths.get("src", "db", "seed", "03_income_log.sql"));
            runSqlFile(connection, Paths.get("src", "db", "seed", "04_savings_log.sql"));
            runSqlFile(connection, Paths.get("src", "db", "seed", "05_investments_log.sql"));
            runSqlFile(connection, Paths.get("src", "db", "seed", "06_debts_log.sql"));
            runSqlFile(connection, Paths.get("src", "db", "seed", "07_subscriptions.sql"));

            System.out.println("Schema loaded successfully.");

        } catch (Exception e) {
            System.out.println("Error while connecting to database or running sql files.");
            e.printStackTrace();
        }
    }

    private static void deleteCurrentDatabase() {
        Path dbPath = Paths.get("src", "db", "budget.db");
        try {
            if (Files.exists(dbPath)) {
                Files.delete(dbPath);
                System.out.println("Deleted existing database file: " + dbPath);
            }
        } catch (Exception e) {
            System.out.println("Failed to delete database file: " + dbPath);
            e.printStackTrace();
        }
    }

    private static void runSqlFile(Connection connection, Path path) throws Exception {
        String sql = Files.readString(path);
        String[] sql_lines = sql.split(";");
        int executed_statements = 0;

        for (String line : sql_lines) {
            line = line.trim();

            if (line.isEmpty()) continue;

            try (Statement statement = connection.createStatement()) {
                statement.execute(line);
                executed_statements++;
            }
        }

        System.out.println("Ran " + executed_statements + " statements from " + path);
    }
}
