package Entities;

import java.sql.Date;

public class Reservation {
    private int id;
    private int guestId;
    private int roomId;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(int guestId, int roomId, Date checkIn, Date checkOut) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getGuestId() { return guestId; }
    public int getRoomId() { return roomId; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
}