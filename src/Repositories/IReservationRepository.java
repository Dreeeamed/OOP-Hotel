package Repositories;

import Entities.Reservation;

public interface IReservationRepository extends IRepository<Reservation> {
    void createTable();
}