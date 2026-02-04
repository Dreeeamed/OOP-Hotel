package Entities;

public class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber, int floor) {
        super(0, roomNumber, floor, 200, true);
    }

    public SuiteRoom(int id, int roomNumber, int floor, double price, boolean isAvailable) {
        super(id, roomNumber, floor, price, isAvailable);
    }

    @Override public String getType() { return "Suite"; }
}
