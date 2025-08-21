package db.models;

public class User {
    private final int id;
    private final String name;
    private final String username;
    private final String password; // plaintext by your choice

    public User(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
