package Repositories;

import Entities.Reservation;
import java.util.List;

public interface IReservationRepository {
    void createReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    void deleteReservation(int reservationId); // Required for Cancellation
}