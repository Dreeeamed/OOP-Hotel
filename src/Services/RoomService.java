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

    //room initialization caller. Next stop. Hotel Application.
    public void initializeRoomsIfNeeded() {
        // Now using the interface method
        List<Room> Rooms = roomRepository.getAllRooms(); // Callim Load Method s
        //    public List<Room> getAllRooms() {
        //        List<Room> rooms = new ArrayList<>();
        //        String sql = "SELECT * FROM rooms ORDER BY room_number"; <- If Empty:

        if(Rooms.isEmpty()) {
            System.out.println("Initializing rooms");
            List<Room> newRooms = RoomInitialization.initializeRooms();
            for (Room room : newRooms) {
                roomRepository.addRoom(room);
            }
        }
    }

    public void updateRoomAvailability(int roomId, boolean isAvailable) {
        roomRepository.updateAvailability(roomId, isAvailable);
    }

    public List<Room> getAllRooms() {
        return roomRepository.getAllRooms();
    }

    public Room getRoomByNumber(int roomNumber) throws RoomNotFoundException {
        Room room = roomRepository.getRoomByNumber(roomNumber);

        if (room == null) {
            throw new RoomNotFoundException("Room #" + roomNumber + " not found.");
        }
        return room;
    }
}