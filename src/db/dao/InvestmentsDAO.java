package db.dao;

import db.dao.templates.TemplateSavingsDAO;
import db.models.InvestmentsRecord;

import java.sql.ResultSet;
import java.time.LocalDate;

public class InvestmentsDAO extends TemplateSavingsDAO<InvestmentsRecord> {
    public InvestmentsDAO(int userId) {
        super(userId, "investments_log");
    }

    @Override
    protected InvestmentsRecord createRecord(int id, int userId, LocalDate date, double change, double balance, String notes){
        return new InvestmentsRecord(id, userId, date, change, balance, notes);
    }

    @Override
    protected InvestmentsRecord createRecord(ResultSet result) throws Exception{
        return new InvestmentsRecord(
                result.getInt("id"),
                result.getInt("user_id"),
                LocalDate.parse(result.getString("date")),
                result.getDouble("change"),
                result.getDouble("balance"),
                result.getString("notes")
        );
    }
}
