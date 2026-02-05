package Services;

import Entities.Reservation;
import Entities.Room;
import Repositories.IReservationRepository;
import Repositories.IRoomRepository;
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


        Reservation res = new Reservation(
                details.getGuestId(),
                details.getRoomId(),
                details.getCheckIn(),
                details.getCheckOut()
        );

        reservationRepo.add(res);
        roomRepo.updateAvailability(details.getRoomId(), false);

        return res.getId();
    }

    public void cancelReservation(int resId) {
        Reservation res = reservationRepo.getById(resId);
        if (res != null) {
            roomRepo.updateAvailability(res.getRoomId(), true);
            reservationRepo.delete(resId);
        }
    }

    public List<Reservation> getAll() {
        return reservationRepo.getAll();
    }
}
