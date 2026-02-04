package Entities;

//Room base. Next stop RoomInitialization.

public abstract class Room {
    private int id;
    private int roomNumber;
    private int floor;
    private double price; // FIX 1: Changed to double for consistency
    private boolean isAvailable;

    //Очень важно на этом моменте:
    //Там апай же говорила что Датабаза сама ставить Айдишки, и типо не обязательно их называть здесь
    //Тут из-за этого два Room-a

    //Room-1: Это мы будем использовать тогда, когда мы вытаскиваем с Датабазы (из-за ID)
    public Room(int id, int roomNumber, int floor, double price, boolean isAvailable) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.floor = floor;
        setPrice(price);
        this.isAvailable = isAvailable;
    }

    //Room-2: Это тогда, когда мы создаем Room и нет Датабазы, а там оно само все поставит
    //Потом заберем ID короче
    public Room(int roomNumber, int floor, double price, boolean isAvailable) {
        this(0, roomNumber, floor, price, isAvailable);
    }

    //Это чтоб у Детей Рума тоже были у всех
    //Чтоб понимать про какого ребенка идет речь создан.
    public abstract String getType();

    //GETTERS AND SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoomNumber() { return roomNumber; }

    public int getFloor() { return floor; }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("Room #%d | Floor: %d | Type: %-10s | Price: $%.2f | Status: %s",
                roomNumber,
                floor,
                getType(),
                price,
                isAvailable ? "Available" : "Booked");
    }
}