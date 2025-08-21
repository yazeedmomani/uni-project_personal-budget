package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String URL = "jdbc:sqlite:src/db/budget.db";

    public static Connection get_connection() throws Exception{
        return DriverManager.getConnection(URL);
    }
}
