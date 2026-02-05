package Repositories;

import Entities.Room;
import Configuration.RoomFactory;
import Exceptions.DatabaseException;
import java.sql.*;
import java.util.List;

// 1. MUST extend BaseRepository and implement the interface
public class RoomRepository extends BaseRepository<Room> implements IRoomRepository {

    public RoomRepository(Connection connection) {
        // Pass "rooms" table name to the base class
        super(connection, "rooms");
    }

    // FIX: Rename addRoom to add to match the Interface/Generic pattern
    @Override
    public void add(Room room) {
        String sql = "INSERT INTO rooms (room_number, floor, price, room_type, is_available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement value = connection.prepareStatement(sql)) {
            value.setInt(1, room.getRoomNumber());
            value.setInt(2, room.getFloor());
            value.setDouble(3, room.getPrice());
            value.setString(4, room.getType());
            value.setBoolean(5, room.isAvailable());
            value.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding room: " + room.getRoomNumber(), e);
        }
    }

    // 2. REQUIRED: Implement mapResultSetToEntity for BaseRepository to work
    @Override
    protected Room mapResultSetToEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int number = rs.getInt("room_number");
        int floor = rs.getInt("floor");
        double price = rs.getDouble("price");
        boolean avail = rs.getBoolean("is_available");
        String type = rs.getString("room_type");

        return RoomFactory.createRoom(type, id, number, floor, price, avail);
    }

    // 3. REQUIRED: Implement prepareAddStatement for BaseRepository
    @Override
    protected void prepareAddStatement(PreparedStatement stmt, Room entity) throws SQLException {
        stmt.setInt(1, entity.getRoomNumber());
        stmt.setInt(2, entity.getFloor());
        stmt.setDouble(3, entity.getPrice());
        stmt.setString(4, entity.getType());
        stmt.setBoolean(5, entity.isAvailable());
    }

    // NOTE: We REMOVED getAllRooms() because BaseRepository.getAll() replaces it perfectly.

    @Override
    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs); // Reuse the mapper
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching room number: " + roomNumber, e);
        }
        return null;
    }

    @Override
    public void updateAvailability(int roomId, boolean isAvailable) {
        String sql = "UPDATE rooms SET is_available = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public boolean isRoomAvailable(int roomId) {
        String sql = "SELECT is_available FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBoolean("is_available");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking availability for room ID: " + roomId, e);
        }
        return false;
    }

    @Override
    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS rooms (
                id SERIAL PRIMARY KEY,
                room_number INT NOT NULL UNIQUE,
                floor INT NOT NULL,
                price DECIMAL(10, 2) NOT NULL,
                room_type VARCHAR(50) NOT NULL,
                is_available BOOLEAN DEFAULT TRUE
            );
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Error creating rooms table", e);
        }
    }
}