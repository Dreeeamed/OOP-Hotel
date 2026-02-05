package Repositories;

import Entities.Guest;

public interface IGuestRepository extends IRepository<Guest> {
    Guest getGuestByEmail(String email);
    void createTable();
}