package db.dao;

import db.Database;
import db.models.IncomeRecord;

import java.sql.*;
import java.time.LocalDate;

public class IncomeDAO extends TemplateDAO<IncomeRecord>{
    public IncomeDAO(int userId) {
        super(userId, "income_log");
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

    @Override
    protected IncomeRecord createRecord(ResultSet result) throws Exception{
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
