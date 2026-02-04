package Repositories;

import Entities.Guest;

import java.util.List;

public interface IGuestRepository {
    void addGuest(Guest guest);
    List<Guest> getAllGuests();
    Guest getGuestById(int id);
    Guest getGuestByEmail(String email); // <--- NEW METHOD
}