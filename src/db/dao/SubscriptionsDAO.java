package db.dao;

import db.Database;
import db.dao.templates.TemplateDAO;
import db.models.SubscriptionsRecord;

import java.sql.*;

public class SubscriptionsDAO extends TemplateDAO<SubscriptionsRecord> {
    public SubscriptionsDAO(int userId) {
        super(userId, "subscriptions");
    }

    public SubscriptionsRecord create(SubscriptionsRecord record) throws Exception {
        String sql = "INSERT INTO subscriptions(user_id, subscription, amount, expected_day, notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            template.setInt(1, userId);
            template.setString(2, record.getSubscription());
            template.setDouble(3, record.getAmount());
            template.setInt(4, record.getExpectedDay());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(5, Types.VARCHAR);
            else template.setString(5, record.getNotes());

            template.executeUpdate();

            try (ResultSet keys = template.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new SubscriptionsRecord(id, userId, record.getSubscription(), record.getAmount(), record.getExpectedDay(), record.getNotes());
                } else {
                    throw new SQLException("Insert failed (no generated key).");
                }
            }
        }
    }

    public SubscriptionsRecord update(SubscriptionsRecord record) throws Exception {
        String sql = "UPDATE subscriptions SET subscription = ?, amount = ?, expected_day = ?, notes = ? WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setString(1, record.getSubscription());
            template.setDouble(2, record.getAmount());
            template.setInt(3, record.getExpectedDay());
            if (record.getNotes() == null || record.getNotes().isBlank()) template.setNull(4, Types.VARCHAR);
            else template.setString(4, record.getNotes());
            template.setInt(5, record.getId());
            template.setInt(6, userId);

            template.executeUpdate();

            return new SubscriptionsRecord(record.getId(), userId, record.getSubscription(), record.getAmount(), record.getExpectedDay(), record.getNotes());
        }
    }

    @Override
    protected SubscriptionsRecord createRecord(ResultSet result) throws Exception{
        return new SubscriptionsRecord(
                result.getInt("id"),
                result.getInt("user_id"),
                result.getString("subscription"),
                result.getDouble("amount"),
                result.getInt("expected_day"),
                result.getString("notes")
        );
    }
}
