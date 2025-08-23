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

/*
This is the main interface for the database. Do not use other files.
 */

public class Database {
    private static final String URL = "jdbc:sqlite:src/db/budget.db";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static User currentUser;
    private static IncomeDAO incomeDAO;
    private static SavingsDAO savingsDAO;

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
            incomeDAO = null;
            savingsDAO = null;
            return;
        }
        currentUser = user;
        incomeDAO = new IncomeDAO(user.getId());
        savingsDAO = new SavingsDAO(user.getId());
    }

    public static User getCurrentUser() {return currentUser;}


    // DAOs
    public static IncomeDAO getIncomeDAO() {return incomeDAO;}
    public static SavingsDAO getSavingsDAO() {return savingsDAO;}


    // Date Format
    public static DateTimeFormatter getDateFormat() {return DATE_FORMAT;}
}
