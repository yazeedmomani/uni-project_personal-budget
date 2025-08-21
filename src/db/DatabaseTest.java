package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args){
        String url = "jdbc:sqlite:src/db/budget.db";

        try(Connection connection = DriverManager.getConnection(url)){
            if(connection != null)
                System.out.println("Connected to Database Successfully.");
        }
        catch(Exception e) {
            System.out.println("Error while trying to connect to Database.");
            e.printStackTrace();
        }
    }
}
