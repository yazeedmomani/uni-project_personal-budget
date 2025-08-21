package db;

import db.dao.UsersDAO;
import db.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    private static final String URL = "jdbc:sqlite:src/db/budget.db";

    public static Connection getConnection() throws Exception{
        return DriverManager.getConnection(URL);
    }

    // Users
    public static User getUser(String username, String password) throws Exception{
        return UsersDAO.get(username, password);
    }

    public static void updateUser(User user) throws Exception{
        UsersDAO.update(user);
    }
}
