package db.dao.templates;

import db.Database;
import db.models.templates.TemplateRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TemplateDAO<Record extends TemplateRecord> {
    protected final int userId;
    protected final String table;

    protected TemplateDAO(int userId, String table) {
        this.userId = userId;
        this.table = table;
    }

    public Record get(int id) throws Exception {
        String sql = "SELECT * FROM " + table + " WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, id);
            template.setInt(2, userId);

            try (ResultSet result = template.executeQuery()) {
                if (!result.next()) return null;

                return createRecord(result);
            }
        }
    }

    public List<Record> getAll() throws Exception {
        return getAll(0);
    }

    public List<Record> getAll(int limit) throws Exception{
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM " + table + " WHERE user_id = ? ORDER BY id DESC";
        if (limit > 0) sql += " LIMIT ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, userId);
            if (limit > 0) template.setInt(2, limit);

            try (ResultSet result = template.executeQuery()) {
                while (result.next()) {
                    Record record = createRecord(result);
                    records.add(record);
                }
            }
        }

        return records;
    }

    public void delete(Record record) throws Exception {
        String sql = "DELETE FROM " + table + " WHERE id = ? AND user_id = ?";

        try (Connection connection = Database.getConnection()) {
            PreparedStatement template = connection.prepareStatement(sql);

            template.setInt(1, record.getId());
            template.setInt(2, userId);

            template.executeUpdate();
        }
    }

    public abstract Record create(Record record) throws Exception;
    public abstract Record update(Record record) throws Exception;

    protected abstract Record createRecord(ResultSet result) throws Exception;
}
