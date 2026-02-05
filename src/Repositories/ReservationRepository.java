package Repositories;

import Entities.Reservation;
import java.sql.*;

public class ReservationRepository extends BaseRepository<Reservation> implements IReservationRepository {

    public ReservationRepository(Connection connection) {
        super(connection, "reservations");
    }

    @Override
    protected Reservation mapResultSetToEntity(ResultSet rs) throws SQLException {
        Reservation r = new Reservation(
                rs.getInt("guest_id"),
                rs.getInt("room_id"),
                rs.getDate("check_in_date"),
                rs.getDate("check_out_date")
        );
        r.setId(rs.getInt("id"));
        return r;
    }

    @Override
    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS reservations (
                id SERIAL PRIMARY KEY,
                guest_id INT REFERENCES guests(id) ON DELETE CASCADE,
                room_id INT REFERENCES rooms(id) ON DELETE CASCADE,
                check_in_date DATE NOT NULL,
                check_out_date DATE NOT NULL
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new Exceptions.DatabaseException("Error creating reservations table", e);
        }
    }

    @Override
    protected void prepareAddStatement(PreparedStatement stmt, Reservation entity) throws SQLException {
        stmt.setInt(1, entity.getGuestId());
        stmt.setInt(2, entity.getRoomId());
        stmt.setDate(3, entity.getCheckInDate());
        stmt.setDate(4, entity.getCheckOutDate());
    }

    @Override
    public void add(Reservation res) {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            prepareAddStatement(stmt, res);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) res.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new Exceptions.DatabaseException("Error creating reservation", e);
        }
    }
}