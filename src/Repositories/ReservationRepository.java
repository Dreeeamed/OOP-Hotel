package Repositories;

import Entities.Reservation;
import Exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements IReservationRepository {
    private final Connection connection;

    public ReservationRepository(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        // FIX: Added check_out_date to the table schema
        String sql = """
            CREATE TABLE IF NOT EXISTS reservations (
                id SERIAL PRIMARY KEY,
                guest_id INT REFERENCES guests(id),
                room_id INT REFERENCES rooms(id),
                check_in_date DATE NOT NULL,
                check_out_date DATE NOT NULL
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Error creating reservations table", e);
        }
    }

    @Override
    public void createReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setDate(3, reservation.getCheckInDate());
            stmt.setDate(4, reservation.getCheckOutDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create reservation in DB", e);
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("guest_id"),
                        rs.getInt("room_id"),
                        rs.getDate("check_in_date"),
                        rs.getDate("check_out_date")
                );
                r.setId(rs.getInt("id"));
                list.add(r);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all reservations", e);
        }
        return list;
    }

    @Override
    public void deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No reservation found with ID: " + reservationId);
            } else {
                System.out.println("Reservation #" + reservationId + " deleted from database.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting reservation with ID: " + reservationId, e);
        }
    }
}