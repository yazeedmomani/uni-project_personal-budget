package db.dao;

import db.Database;
import db.models.IncomeRecord;
import db.models.SavingsRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SavingsDAO {
    private final int userId;

    public SavingsDAO(int userId) {
        this.userId = userId;
    }

    public SavingsRecord get(int id) throws Exception {
        String sql = "SELECT * FROM savings_log WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, id);
            template.setInt(2, userId);

            try (ResultSet result = template.executeQuery()) {
                if (!result.next()) return null;

                return new SavingsRecord(
                        result.getInt("id"),
                        result.getInt("user_id"),
                        LocalDate.parse(result.getString("date")),
                        result.getDouble("change"),
                        result.getDouble("balance"),
                        result.getString("notes")
                );
            }
        }
    }

    public List<SavingsRecord> getAll() throws Exception {
        return getAll(0);
    }

    public List<SavingsRecord> getAll(int limit) throws Exception{
        List<SavingsRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM savings_log WHERE user_id = ? ORDER BY id DESC";
        if (limit > 0) sql += " LIMIT ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, userId);
            if (limit > 0) template.setInt(2, limit);

            try (ResultSet result = template.executeQuery()) {
                while (result.next()) {
                    SavingsRecord record = new SavingsRecord(
                            result.getInt("id"),
                            result.getInt("user_id"),
                            LocalDate.parse(result.getString("date")),
                            result.getDouble("change"),
                            result.getDouble("balance"),
                            result.getString("notes")
                    );
                    records.add(record);
                }
            }
        }

        return records;
    }

    public SavingsRecord create(SavingsRecord record) throws Exception {
        String sql = "INSERT INTO savings_log(user_id, date, change, balance, notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            double balance = this.getLastBalance() + record.getChange();

            template.setInt(1, userId);
            template.setString(2, record.getDate().toString());
            template.setDouble(3, record.getChange());
            template.setDouble(4, balance);
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(5, Types.VARCHAR);
            else template.setString(5, record.getNotes());

            template.executeUpdate();

            try (ResultSet keys = template.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new SavingsRecord(id, userId, record.getDate(), record.getChange(), balance, record.getNotes());
                } else {
                    throw new SQLException("Insert failed (no generated key).");
                }
            }
        }
    }

    public SavingsRecord update(SavingsRecord record) throws Exception {
        String sql = "UPDATE savings_log SET date = ?, change = ?, balance = ?, notes = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            double balance = this.getLastBalance() + record.getChange();

            template.setString(1, record.getDate().toString());
            template.setDouble(2, record.getChange());
            template.setDouble(3, balance);
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(4, Types.VARCHAR);
            else template.setString(4, record.getNotes());
            template.setInt(5, record.getId());
            template.setInt(6, userId);

            template.executeUpdate();

            return new SavingsRecord(record.getId(), userId, record.getDate(), record.getChange(), balance, record.getNotes());
        }
    }

    public void delete(SavingsRecord record) throws Exception {
        String sql = "DELETE FROM savings_log WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, record.getId());
            template.setInt(2, userId);

            template.executeUpdate();
        }
    }

    public double getLastBalance() throws Exception {
        String sql = "SELECT balance FROM savings_log WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);
            template.setInt(1, userId);

            try (ResultSet result = template.executeQuery()) {
                return result.next() ? result.getDouble("balance") : 0.0;
            }
        }
    }
}
