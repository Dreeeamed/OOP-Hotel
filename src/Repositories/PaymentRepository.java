package Repositories;

import Entities.Payment;
import Exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements IPaymentRepository {
    private final Connection connection;

    public PaymentRepository(Connection connection) {
        this.connection = connection;
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
            throw new DatabaseException("Error creating payments table", e);
        }
    }

    @Override
    public void addPayment(Payment payment) {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, payment.getReservationId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setDate(3, payment.getPaymentDate());
            stmt.executeUpdate();
            System.out.println("Payment recorded successfully.");
        } catch (SQLException e) {
            System.out.println("Error: Could not record payment. Check if Reservation ID exists.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setReservationId(rs.getInt("reservation_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentDate(rs.getDate("payment_date"));
                list.add(p);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching payments", e);
        }
        return list;
    }
}