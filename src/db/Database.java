package db;

import db.dao.IncomeDAO;
import db.dao.SavingsDAO;
import db.dao.UsersDAO;
import db.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

public class Database {
    private static final String URL = "jdbc:sqlite:src/db/budget.db";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static User currentUser;

    public static IncomeDAO incomeLog;
    public static SavingsDAO savingsLog;

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

    public static void setCurrentUser(User user){
        if(user == null){
            currentUser = user;
            incomeLog = null;
            savingsLog = null;
            return;
        }
        currentUser = user;
        incomeLog = new IncomeDAO(user.getId());
        savingsLog = new SavingsDAO(user.getId());
    }

    public static User getCurrentUser() {return currentUser;}


    // Date Format
    public static DateTimeFormatter getDateFormat() {return DATE_FORMAT;}
}
