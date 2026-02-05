package Services;

import Configuration.RoomInitialization;
import Entities.Room;
import Exceptions.RoomNotFoundException;
import Repositories.IRoomRepository;

import java.util.List;

public class RoomService {
    private final IRoomRepository roomRepository;

    public RoomService(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void initializeRoomsIfNeeded() {
        List<Room> rooms = roomRepository.getAll();

        if(rooms.isEmpty()) {
            System.out.println("Initializing rooms...");
            List<Room> newRooms = RoomInitialization.initializeRooms();
            for (Room room : newRooms) {
                roomRepository.add(room);
            }
        }
    }

    public void updateRoomAvailability(int roomId, boolean isAvailable) {
        roomRepository.updateAvailability(roomId, isAvailable);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getAll();
    }

    public Room getRoomByNumber(int roomNumber) throws RoomNotFoundException {
        Room room = roomRepository.getRoomByNumber(roomNumber);

        if (room == null) {
            throw new RoomNotFoundException("Room #" + roomNumber + " not found.");
        }
        return room;
    }
}