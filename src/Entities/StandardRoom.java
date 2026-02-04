package Entities;

public class StandardRoom extends Room {
    public StandardRoom(int roomNumber, int floor) {
        super(0, roomNumber, floor, 100, true);
    }

    public StandardRoom(int id, int roomNumber, int floor, double price, boolean isAvailable) {
        super(id, roomNumber, floor, price, isAvailable);
    }

    @Override public String getType() { return "Standard"; }
}
