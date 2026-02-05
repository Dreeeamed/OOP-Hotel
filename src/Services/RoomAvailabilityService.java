package Services;

import Entities.Room;
import Repositories.IRoomRepository;
import Configuration.RoomFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomAvailabilityService {
    private final Connection connection;
    private final IRoomRepository roomRepo;

    public RoomAvailabilityService(Connection connection, IRoomRepository roomRepo) {
        this.connection = connection;
        this.roomRepo = roomRepo;
    }

    public List<Room> findAvailableRooms(Date checkIn, Date checkOut) throws SQLException {
        List<Room> availableRooms = new ArrayList<>();

        String sql = """
            SELECT * FROM rooms r WHERE r.id NOT IN (
                SELECT room_id FROM reservations 
                WHERE (check_in_date < ? AND check_out_date > ?)
            ) ORDER BY r.room_number;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, checkOut);
            stmt.setDate(2, checkIn);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int number = rs.getInt("room_number");
                    int floor = rs.getInt("floor");
                    double price = rs.getDouble("price");
                    boolean isAvailable = rs.getBoolean("is_available");
                    String type = rs.getString("room_type");

                    Room room = RoomFactory.createRoom(type, id, number, floor, price, isAvailable);
                    availableRooms.add(room);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during availability search: " + e.getMessage());
            throw e;
        }

        return availableRooms;
    }
}