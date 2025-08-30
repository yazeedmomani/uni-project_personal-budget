package db.dao;

import db.Database;
import db.dao.templates.TemplateDAO;
import db.models.DebtsRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DebtsDAO extends TemplateDAO<DebtsRecord> {
    public DebtsDAO(int userId) {
        super(userId, "debts_log");
    }

    public List<DebtsRecord> getAll(String type) throws Exception {
        return getAll(0, type);
    }

    public List<DebtsRecord> getAll(int limit, String type) throws Exception{
        List<DebtsRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM debts_log WHERE user_id = ? AND type = ? ORDER BY id DESC";
        if (limit > 0) sql += " LIMIT ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, userId);
            template.setString(2, type);
            if (limit > 0) template.setInt(3, limit);

            try (ResultSet result = template.executeQuery()) {
                while (result.next()) {
                    DebtsRecord record = createRecord(result);
                    records.add(record);
                }
            }
        }

        return records;
    }

    public DebtsRecord create(DebtsRecord record) throws Exception {
        String sql = "INSERT INTO debts_log(user_id, date, party, amount, notes, type) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            template.setInt(1, userId);
            template.setString(2, record.getDate().toString());
            template.setString(3, record.getParty());
            template.setDouble(4, record.getAmount());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(5, Types.VARCHAR);
            else template.setString(5, record.getNotes());
            template.setString(6, record.getType());

            template.executeUpdate();

            try (ResultSet keys = template.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new DebtsRecord(id, userId, record.getDate(), record.getParty(), record.getAmount(), record.getNotes(), record.getType());
                } else {
                    throw new SQLException("Insert failed (no generated key).");
                }
            }
        }
    }

    public DebtsRecord update(DebtsRecord record) throws Exception {
        String sql = "UPDATE debts_log SET date = ?, party = ?, amount = ?, notes = ? WHERE id = ? AND user_id = ? AND type = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setString(1, record.getDate().toString());
            template.setString(2, record.getParty());
            template.setDouble(3, record.getAmount());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(4, Types.VARCHAR);
            else template.setString(4, record.getNotes());
            template.setInt(5, record.getId());
            template.setInt(6, userId);
            template.setString(7, record.getType());

            template.executeUpdate();

            return new DebtsRecord(record.getId(), userId, record.getDate(), record.getParty(), record.getAmount(), record.getNotes(), record.getType());
        }
    }

    @Override
    protected DebtsRecord createRecord(ResultSet result) throws Exception{
        return new DebtsRecord(
                result.getInt("id"),
                result.getInt("user_id"),
                LocalDate.parse(result.getString("date")),
                result.getString("party"),
                result.getDouble("amount"),
                result.getString("notes"),
                result.getString("type")
        );
    }
}
