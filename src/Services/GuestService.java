package Services;

import Entities.Guest;
import Exceptions.GuestNotFoundException;
import Repositories.IGuestRepository;

import java.util.List;

public class GuestService {

    private final IGuestRepository guestRepository;

    public GuestService(IGuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public void registerGuest(String name, String email) {
        if (name == null || name.length() < 2) {
            System.out.println("Error: Name is too short.");
            return;
        }

        Guest newGuest = new Guest(name, email);
        guestRepository.add(newGuest);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.getAll();
    }

    public Guest getGuestById(int id) throws GuestNotFoundException {
        Guest guest = guestRepository.getById(id);
        if (guest == null) {
            throw new GuestNotFoundException("Guest with ID " + id + " was not found.");
        }
        return guest;
    }

    public Guest getAndRegisterGuest(String email, java.util.Scanner scanner) {
        Guest existingGuest = guestRepository.getGuestByEmail(email);

        if (existingGuest != null) {
            System.out.println("Welcome back, " + existingGuest.getName() + "!");
            return existingGuest;
        }

        System.out.println("It looks like you are new here.");
        System.out.print("Please enter your Full Name to register: ");

        String fullName = scanner.nextLine();

        Guest newGuest = new Guest(fullName, email);

        guestRepository.add(newGuest);

        System.out.println("Registration successful!");
        return newGuest;
    }
}
//s