package Repositories;

import Entities.Guest;
import java.sql.*;

public class GuestRepository extends BaseRepository<Guest> implements IGuestRepository {

    public GuestRepository(Connection connection) {
        super(connection, "guests");
    }

    @Override
    public void add(Guest guest) {
        String sql = "INSERT INTO guests (name, email) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            prepareAddStatement(stmt, guest);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) guest.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new Exceptions.DatabaseException("Error adding guest", e);
        }
    }

    public void createTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS guests (
            id SERIAL PRIMARY KEY,
            name VARCHAR(100) NOT NULL,
            email VARCHAR(100) UNIQUE NOT NULL
        );
    """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new Exceptions.DatabaseException("Error creating guests table", e);
        }
    }

    @Override
    protected Guest mapResultSetToEntity(ResultSet rs) throws SQLException {
        Guest g = new Guest(rs.getString("name"), rs.getString("email"));
        g.setId(rs.getInt("id"));
        return g;
    }

    @Override
    protected void prepareAddStatement(PreparedStatement stmt, Guest entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getEmail());
    }


    @Override
    public Guest getGuestByEmail(String email) {
        String sql = "SELECT * FROM guests WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}