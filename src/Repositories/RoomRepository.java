package Repositories;

import Entities.DormRoom;
import Entities.Room;
import Entities.StandardRoom;
import Entities.SuiteRoom;
import Exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository implements IRoomRepository {
    private final Connection connection;

    public RoomRepository(Connection connection) {
        this.connection = connection;
    }

    // Not in Interface, but needed for Setup in Main
    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS rooms (
                id SERIAL PRIMARY KEY,
                room_number INT NOT NULL UNIQUE,
                floor INT NOT NULL,
                price INT NOT NULL,
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

    @Override
    public void addRoom(Room room) {
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

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int number = rs.getInt("room_number");
                int floor = rs.getInt("floor");
                int price = rs.getInt("price");
                boolean avail = rs.getBoolean("is_available");
                String type = rs.getString("room_type");

                Room room;
                switch (type) {
                    case "Standard":
                        room = new StandardRoom(id, number, floor, price, avail);
                        break;
                    case "Suite":
                        room = new SuiteRoom(id, number, floor, price, avail);
                        break;
                    case "Dorm":
                        room = new DormRoom(id, number, floor, price, avail);
                        break;
                    default:
                        System.out.println("Unknown room type: " + type);
                        continue;
                }
                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all rooms", e);
        }
        return rooms;
    }

    @Override
    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int number = rs.getInt("room_number");
                    int floor = rs.getInt("floor");
                    int price = rs.getInt("price");
                    boolean avail = rs.getBoolean("is_available");
                    String type = rs.getString("room_type");

                    switch (type) {
                        case "Standard":
                            return new StandardRoom(id, number, floor, price, avail);
                        case "Suite":
                            return new SuiteRoom(id, number, floor, price, avail);
                        case "Dorm":
                            return new DormRoom(id, number, floor, price, avail);
                        default:
                            return null;
                    }
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
                if (rs.next()) {
                    return rs.getBoolean("is_available");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking availability for room ID: " + roomId, e);
        }
        return false;
    }
}