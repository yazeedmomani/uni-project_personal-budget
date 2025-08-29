package db.dao;

import db.Database;
import db.models.SavingsRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SavingsDAO extends TemplateSavingsDAO<SavingsRecord>{
    public SavingsDAO(int userId) {
        super(userId, "savings_log");
    }

    @Override
    protected SavingsRecord createRecord(int id, int userId, LocalDate date, double change, double balance, String notes){
        return new SavingsRecord(id, userId, date, change, balance, notes);
    }

    @Override
    protected SavingsRecord createRecord(ResultSet result) throws Exception{
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
