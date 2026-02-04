package Services;

import Entities.Reservation;
import Exceptions.InvalidDateRangeException;
import Exceptions.ReservationException;
import Repositories.IReservationRepository;
import Repositories.IRoomRepository;
import java.sql.Date;
import java.util.List; // FIX: Added import

public class ReservationService {
    private final IReservationRepository reservationRepo;
    private final IRoomRepository roomRepo;

    public ReservationService(IReservationRepository reservationRepo, IRoomRepository roomRepo) {
        this.reservationRepo = reservationRepo;
        this.roomRepo = roomRepo;
    }

    // FIX: Changed return type from void to int
    public int createReservation(int guestId, int roomId, Date checkIn, Date checkOut)
            throws ReservationException, InvalidDateRangeException {

        Date today = new Date(System.currentTimeMillis());
        if (checkIn.before(today)) throw new InvalidDateRangeException("Check-in cannot be in the past.");
        if (checkOut.before(checkIn) || checkOut.equals(checkIn))
            throw new InvalidDateRangeException("Check-out must be after check-in.");

        try {
            Reservation res = new Reservation(guestId, roomId, checkIn, checkOut);

            // Your Repository should be updated to return the ID
            reservationRepo.createReservation(res);
            int generatedId = res.getId();

            // Update room to occupied
            roomRepo.updateAvailability(roomId, false);

            System.out.println("Success! Room status updated.");
            return generatedId; // Return the ID for the PaymentService
        } catch (Exception e) {
            throw new ReservationException("Failed to finalize booking: " + e.getMessage());
        }
    }

    public void cancelReservation(int resId) {
        reservationRepo.deleteReservation(resId);
        System.out.println("Reservation #" + resId + " cancelled.");
    }

    public List<Reservation> getAllReservations() {
        return reservationRepo.getAllReservations();
    }
}