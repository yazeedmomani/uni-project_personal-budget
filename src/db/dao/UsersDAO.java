package db.dao;

import db.Database;
import db.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsersDAO {
    public static User get(String username, String password) throws Exception{
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try(Connection connection = Database.getConnection()){
            PreparedStatement template = connection.prepareStatement(sql);

            template.setString(1, username);
            template.setString(2, password);

            try(ResultSet result = template.executeQuery()){
                if(result.next()){
                    return new User(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("username"),
                            result.getString("password")
                    );
                }
                return null;
            }
        }
    }

    public static void update(User user) throws Exception{
        String sql = "UPDATE users SET name = ?, username = ?, password = ? WHERE id = ?";

        try(Connection connection = Database.getConnection()){
            PreparedStatement template = connection.prepareStatement(sql);

            template.setString(1, user.getName());
            template.setString(2, user.getUsername());
            template.setString(3, user.getPassword());
            template.setInt(4, user.getId());

            template.executeUpdate();
        }
    }
}
