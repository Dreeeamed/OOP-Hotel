package Services;

import Entities.Reservation;
import Entities.Room;
import Exceptions.ReservationException;
import Repositories.IReservationRepository;
import Repositories.IRoomRepository;
import java.sql.Date;
import java.util.List;

public class ReservationService {
    private final IReservationRepository reservationRepo;
    private final IRoomRepository roomRepo;

    public ReservationService(IReservationRepository reservationRepo, IRoomRepository roomRepo) {
        this.reservationRepo = reservationRepo;
        this.roomRepo = roomRepo;
    }

    public int createReservation(ReservationDetails details) {
        Room room = roomRepo.getById(details.getRoomId());

        if (room == null) {
            throw new RuntimeException("Room not found!");
        }

        double finalPrice = PricingPolicy.getInstance().calculateFinalPrice(room.getPrice());
        System.out.println("Pricing Policy applied. Total: $" + finalPrice);

        Reservation res = new Reservation(
                details.getGuestId(),
                details.getRoomId(),
                details.getCheckIn(),
                details.getCheckOut()
        );

        reservationRepo.add(res); // Generic 'add' name

        // 4. Update room status
        roomRepo.updateAvailability(details.getRoomId(), false);

        return res.getId();
    }

    public void cancelReservation(int resId) {
        Reservation res = reservationRepo.getById(resId);
        if (res != null) {
            roomRepo.updateAvailability(res.getRoomId(), true); // Free the room
            reservationRepo.delete(resId); // Inherited from BaseRepository
        }
    }

    public List<Reservation> getAll() {
        return reservationRepo.getAll();
    }
}