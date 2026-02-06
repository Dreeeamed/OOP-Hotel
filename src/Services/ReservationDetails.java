package Services; // or package Entities;

import java.sql.Date;

public class ReservationDetails {
    private final int roomId;
    private final int guestId;
    private final Date checkIn;  // Added
    private final Date checkOut; // Added
    private final String breakfastType;
    private final boolean lateCheckout;

    private ReservationDetails(Builder builder) {
        this.roomId = builder.roomId;
        this.guestId = builder.guestId;
        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
        this.breakfastType = builder.breakfastType;
        this.lateCheckout = builder.lateCheckout;
    }

    // Getters are needed so the Service can read these values!
    public int getRoomId() { return roomId; }
    public int getGuestId() { return guestId; }
    public Date getCheckIn() { return checkIn; }
    public Date getCheckOut() { return checkOut; }

    public static class Builder {
        private final int roomId;
        private final int guestId;
        private Date checkIn;
        private Date checkOut;
        private String breakfastType = "None";
        private boolean lateCheckout = false;

        public Builder(int guestId, int roomId) {
            this.guestId = guestId;
            this.roomId = roomId;
        }

        public Builder withDates(Date in, Date out) {
            this.checkIn = in;
            this.checkOut = out;
            return this;
        }

        public Builder withBreakfast(String type) { this.breakfastType = type; return this; }
        public Builder withLateCheckout(boolean late) { this.lateCheckout = late; return this; }

        public ReservationDetails build() { return new ReservationDetails(this); }
    }
}