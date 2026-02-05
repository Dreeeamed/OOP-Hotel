package Repositories;

import Entities.Room;
import java.util.List;

public interface IRoomRepository extends IRepository<Room> {

    Room getRoomByNumber(int roomNumber);
    void updateAvailability(int roomId, boolean isAvailable);
    boolean isRoomAvailable(int roomId);
    void createTable();
}