package db;

import db.dao.*;
import db.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
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
    private static InvestmentsDAO investmentsDAO;
    private static SubscriptionsDAO subscriptionsDAO;
    private static DebtsDAO debtsDAO;

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
            investmentsDAO = null;
            subscriptionsDAO = null;
            debtsDAO = null;
            return;
        }
        currentUser = user;
        incomeDAO = new IncomeDAO(user.getId());
        savingsDAO = new SavingsDAO(user.getId());
        investmentsDAO = new InvestmentsDAO(user.getId());
        subscriptionsDAO = new SubscriptionsDAO(user.getId());
        debtsDAO = new DebtsDAO(user.getId());
    }

    public static User getCurrentUser() {return currentUser;}


    // DAOs
    public static IncomeDAO getIncomeDAO() {return incomeDAO;}
    public static SavingsDAO getSavingsDAO() {return savingsDAO;}
    public static InvestmentsDAO getInvestmentsDAO() {return investmentsDAO;}
    public static SubscriptionsDAO getSubscriptionsDAO() {return subscriptionsDAO;}
    public static DebtsDAO getDebtsDAO() {return debtsDAO;}


    // Date Format
    public static DateTimeFormatter getDateFormat() {return DATE_FORMAT;}
}
