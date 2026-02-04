package Repositories;

import Entities.Room;
import java.util.List;

public interface IRoomRepository {
    void addRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomByNumber(int roomNumber);
    void updateAvailability(int roomId, boolean isAvailable); // For status switching
    boolean isRoomAvailable(int roomId);
}