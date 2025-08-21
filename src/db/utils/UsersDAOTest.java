package db.utils;

import db.dao.UsersDAO;
import db.models.SavingsRecord;
import db.models.User;

public class UsersDAOTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running SavingsDAO test...");

        // 2) GET
        System.out.println("\n== get ==");

        System.out.println("Fetching first user...");
        User user1 = UsersDAO.get("admin", "123456");
        System.out.println("fetched: " + user1);

        System.out.println("Fetching second user...");
        User user2 = UsersDAO.get("bara", "bara123");
        System.out.println("fetched: " + user2);

        // 3) UPDATE
        System.out.println("\n== update ==");

        System.out.println("Updating first user...");
        user1.setName("Name1");
        user1.setUsername("username1");
        user1.setPassword("password1");
        UsersDAO.update(user1);
        User updatedUser1 = UsersDAO.get(user1.getUsername(), user1.getPassword());
        System.out.println("updated: " + updatedUser1);

        System.out.println("Updating second user...");
        user2.setName("Name2");
        user2.setUsername("username2");
        user2.setPassword("password2");
        UsersDAO.update(user2);
        User updatedUser2 = UsersDAO.get(user2.getUsername(), user2.getPassword());
        System.out.println("updated: " + updatedUser2);

        // Restore records to original
        System.out.println("\nRestoring original users");

        System.out.println("Restoring first user...");
        user1.setName("Yazeed");
        user1.setUsername("admin");
        user1.setPassword("123456");
        UsersDAO.update(user1);
        User restoredUser1 = UsersDAO.get(user1.getUsername(), user1.getPassword());
        System.out.println("restored: " + restoredUser1);

        System.out.println("Restoring second user...");
        user2.setName("Bara");
        user2.setUsername("bara");
        user2.setPassword("bara123");
        UsersDAO.update(user2);
        User restoredUser2 = UsersDAO.get(user2.getUsername(), user2.getPassword());
        System.out.println("restored: " + restoredUser2);
    }
}
