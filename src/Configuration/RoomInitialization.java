package Configuration;

import Entities.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomInitialization {
    public static List<Room> initializeRooms() {
        List<Room> allRooms = new ArrayList<>();
        int roomsPerFloor = 12;
        int idCounter = 1;

        for (int floor = 1; floor <= 3; floor++) {
            for (int i = 1; i <= roomsPerFloor; i++) {
                int roomNumber = (floor * 100) + i;

                int typeIndex = idCounter % 3;
                String type = switch (typeIndex) {
                    case 1 -> "STANDARD";
                    case 2 -> "SUITE";
                    default -> "DORM";
                };

                Room room = RoomFactory.createRoom(type, 0, roomNumber, floor, 100.0, true);

                allRooms.add(room);
                idCounter++;
            }
        }
        return allRooms;
    }
}