package Repositories;

import Entities.Payment;
import java.sql.*;

public class PaymentRepository extends BaseRepository<Payment> implements IPaymentRepository {

    public PaymentRepository(Connection connection) {
        super(connection, "payments");
    }

    @Override
    protected Payment mapResultSetToEntity(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setReservationId(rs.getInt("reservation_id"));
        p.setAmount(rs.getDouble("amount"));
        p.setPaymentDate(rs.getDate("payment_date"));
        return p;
    }

    @Override
    protected void prepareAddStatement(PreparedStatement stmt, Payment entity) throws SQLException {
        stmt.setInt(1, entity.getReservationId());
        stmt.setDouble(2, entity.getAmount());
        stmt.setDate(3, entity.getPaymentDate());
    }

    @Override
    public void add(Payment payment) {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            prepareAddStatement(stmt, payment);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exceptions.DatabaseException("Error recording payment", e);
        }
    }

    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS payments (
                id SERIAL PRIMARY KEY,
                reservation_id INT REFERENCES reservations(id) ON DELETE CASCADE,
                amount DECIMAL(10, 2) NOT NULL,
                payment_date DATE DEFAULT CURRENT_DATE
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new Exceptions.DatabaseException("Error creating payments table", e);
        }
    }
}