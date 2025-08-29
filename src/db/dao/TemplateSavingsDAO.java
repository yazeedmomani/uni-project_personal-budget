package db.dao;

import db.Database;
import db.models.SavingsRecord;
import db.models.TemplateSavingsRecord;

import java.sql.*;
import java.time.LocalDate;

public abstract class TemplateSavingsDAO<Record extends TemplateSavingsRecord> extends TemplateDAO<Record>{
    public TemplateSavingsDAO(int userId, String table) {
        super(userId, table);
    }

    public Record create(Record record) throws Exception {
        String sql = "INSERT INTO " + table +"(user_id, date, change, balance, notes) VALUES (?, ?, ?, ?, ?)";

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
                    return createRecord(id, userId, record.getDate(), record.getChange(), balance, record.getNotes());
                } else {
                    throw new SQLException("Insert failed (no generated key).");
                }
            }
        }
    }

    public Record update(Record record) throws Exception {
        String sql = "UPDATE " + table +" SET date = ?, change = ?, balance = ?, notes = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setString(1, record.getDate().toString());
            template.setDouble(2, record.getChange());
            template.setDouble(3, record.getBalance());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(4, Types.VARCHAR);
            else template.setString(4, record.getNotes());
            template.setInt(5, record.getId());
            template.setInt(6, userId);

            template.executeUpdate();

            return createRecord(record.getId(), userId, record.getDate(), record.getChange(), record.getBalance(), record.getNotes());
        }
    }

    public double getLastBalance() throws Exception {
        String sql = "SELECT balance FROM " + table + " WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);
            template.setInt(1, userId);

            try (ResultSet result = template.executeQuery()) {
                return result.next() ? result.getDouble("balance") : 0.0;
            }
        }
    }

    public int getLastID() throws Exception{
        String sql = "SELECT id FROM " + table + " WHERE user_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);
            template.setInt(1, userId);

            try (ResultSet result = template.executeQuery()) {
                return result.next() ? result.getInt("id") : 0;
            }
        }
    }

    @Override
    protected abstract Record createRecord(ResultSet result) throws Exception;
    protected abstract Record createRecord(int id, int userId, LocalDate date, double change, double balance, String notes);
}
