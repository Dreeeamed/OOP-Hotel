package Configuration;

import Entities.*;

public class RoomFactory {
    public static Room createRoom(String type, int id, int number, int floor, double price, boolean avail) {
        return switch (type.toLowerCase()) {
            case "standard" -> new StandardRoom(id, number, floor, price, avail);
            case "suite" -> new SuiteRoom(id, number, floor, price, avail);
            case "dorm" -> new DormRoom(id, number, floor, price, avail);
            default -> throw new IllegalArgumentException("Unknown room type: " + type);
        };
    }
}