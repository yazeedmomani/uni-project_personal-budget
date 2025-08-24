package db.dao;

import db.Database;
import db.models.IncomeRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class IncomeDAO {
    private final int userId;

    public IncomeDAO(int userId) {
        this.userId = userId;
    }

    public IncomeRecord get(int id) throws Exception {
        String sql = "SELECT * FROM income_log WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, id);
            template.setInt(2, userId);

            try (ResultSet result = template.executeQuery()) {
                if (!result.next()) return null;

                return new IncomeRecord(
                        result.getInt("id"),
                        result.getInt("user_id"),
                        LocalDate.parse(result.getString("date")),
                        result.getString("source"),
                        result.getDouble("amount"),
                        result.getString("notes")
                );
            }
        }
    }

    public List<IncomeRecord> getAll() {
        List<IncomeRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM income_log WHERE user_id = ? ORDER BY id DESC";
        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);
            template.setInt(1, userId);
            try (ResultSet result = template.executeQuery()) {
                while (result.next()) {
                    IncomeRecord record = new IncomeRecord(
                            result.getInt("id"),
                            result.getInt("user_id"),
                            LocalDate.parse(result.getString("date")),
                            result.getString("source"),
                            result.getDouble("amount"),
                            result.getString("notes")
                    );
                    records.add(record);
                }
            }
        } catch (Exception e) {
            // Optionally, handle/log exception here or rethrow as unchecked
            throw new RuntimeException(e);
        }
        return records;
    }

    public IncomeRecord create(IncomeRecord record) throws Exception {
        String sql = "INSERT INTO income_log(user_id, date, source, amount, notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            template.setInt(1, userId);
            template.setString(2, record.getDate().toString());
            template.setString(3, record.getSource());
            template.setDouble(4, record.getAmount());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(5, Types.VARCHAR);
            else template.setString(5, record.getNotes());

            template.executeUpdate();

            try (ResultSet keys = template.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new IncomeRecord(id, userId, record.getDate(), record.getSource(), record.getAmount(), record.getNotes());
                } else {
                    throw new SQLException("Insert failed (no generated key).");
                }
            }
        }
    }

    public IncomeRecord update(IncomeRecord record) throws Exception {
        String sql = "UPDATE income_log SET date = ?, source = ?, amount = ?, notes = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setString(1, record.getDate().toString());
            template.setString(2, record.getSource());
            template.setDouble(3, record.getAmount());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(4, Types.VARCHAR);
            else template.setString(4, record.getNotes());
            template.setInt(5, record.getId());
            template.setInt(6, userId);

            template.executeUpdate();

            return new IncomeRecord(record.getId(), userId, record.getDate(), record.getSource(), record.getAmount(), record.getNotes());
        }
    }

    public void delete(IncomeRecord record) throws Exception {
        String sql = "DELETE FROM income_log WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, record.getId());
            template.setInt(2, userId);

            template.executeUpdate();
        }
    }
}
