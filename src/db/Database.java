package db;

import db.dao.UsersDAO;
import db.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    private static final String URL = "jdbc:sqlite:src/db/budget.db";

    public static Connection get_connection() throws Exception{
        return DriverManager.getConnection(URL);
    }

    // Users
    public static User get_user(String username, String password) throws Exception{
        return UsersDAO.get(username, password);
    }

    public static void update_user(User user) throws Exception{
        UsersDAO.update(user);
    }
}
