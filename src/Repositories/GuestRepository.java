package Repositories;

import Entities.Guest;
import Exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestRepository implements IGuestRepository {
    private final Connection connection;

    public GuestRepository(Connection connection) {
        this.connection = connection;
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
            throw new DatabaseException("Error creating guests table", e);
        }
    }

    @Override
    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, email) VALUES (?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, guest.getName());
            stmt.setString(2, guest.getEmail());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("id");
                guest.setId(generatedId);
                System.out.println("Guest saved with ID: " + generatedId);
            }
        } catch (SQLException e) {
            // We print here because duplicate email is a user error, not necessarily a system crash, so it goes back
            System.out.println("Error saving guest (Email might be duplicate): " + e.getMessage());
        }
    }

    @Override
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest g = new Guest();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setEmail(rs.getString("email"));
                guests.add(g);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching guests", e);
        }
        return guests;
    }

    @Override
    public Guest getGuestById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Guest g = new Guest();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setEmail(rs.getString("email"));
                return g;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching guest by ID: " + id, e);
        }
        return null;
    }

    @Override
    public Guest getGuestByEmail(String email) {
        String sql = "SELECT * FROM guests WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Guest g = new Guest();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setEmail(rs.getString("email"));
                return g;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found (means they are a new user)
    }
}