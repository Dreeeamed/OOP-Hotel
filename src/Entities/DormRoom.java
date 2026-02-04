package Entities;

public class DormRoom extends Room {
    public DormRoom(int roomNumber, int floor) {
        super(0, roomNumber, floor, 50, true);
    }

    public DormRoom(int id, int roomNumber, int floor, double price, boolean isAvailable) {
        super(id, roomNumber, floor, price, isAvailable);
    }

    @Override public String getType() { return "Dorm"; }
}
