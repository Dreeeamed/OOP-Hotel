package Entities;

import java.sql.Date;

public class Payment {
    private int id;
    private int reservationId;
    private double amount; // Changed to double
    private Date paymentDate; // Added date via SQL function

    public Payment() {}

    public Payment(int reservationId, double amount) {
        setReservationId(reservationId);
        setAmount(amount);
        this.paymentDate = new Date(System.currentTimeMillis());
    }

    //ENCAPSULATION
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; } // Changed to double

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Payment ID: " + id + " | Res ID: " + reservationId + " | Amount: $" + amount + " | Date: " + paymentDate;
    }
}
