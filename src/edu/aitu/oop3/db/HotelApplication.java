package edu.aitu.oop3.db;

import Services.*;
import Entities.*;
import Exceptions.*;

import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class HotelApplication {
    private final GuestService guestService;
    private final RoomService roomService;
    private final RoomAvailabilityService availabilityService;
    private final ReservationService resService; // Ensure this name is consistent
    private final PaymentService paymentService;
    private final Scanner scanner = new Scanner(System.in);

    public HotelApplication(GuestService gs, RoomService rs, RoomAvailabilityService as,
                            ReservationService resS, PaymentService ps) {
        this.guestService = gs;
        this.roomService = rs;
        this.availabilityService = as;
        this.resService = resS;
        this.paymentService = ps;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- HOTEL BOOKING SYSTEM (Milestone 1) ---");
            System.out.println("1. Find Available Rooms by Date");
            System.out.println("2. Make a Reservation (Auto-Register)");
            System.out.println("3. View All Reservations");
            System.out.println("4. Pay for Reservation (Process Payment)");
            System.out.println("5. Cancel a Reservation"); // Added for Milestone 1
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            try {
                switch (choice) {
                    case 1 -> showAvailableRoomsByDate();
                    case 2 -> makeReservation();
                    case 3 -> showReservations();
                    case 4 -> makePayment();
                    case 5 -> cancelReservation();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showAvailableRoomsByDate() throws Exception {
        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        Date checkIn = Date.valueOf(scanner.nextLine());
        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        Date checkOut = Date.valueOf(scanner.nextLine());

        List<Room> available = availabilityService.findAvailableRooms(checkIn, checkOut);
        if (available.isEmpty()) {
            System.out.println("No rooms available for these dates.");
        } else {
            System.out.println("--- Available Rooms ---");
            available.forEach(System.out::println);
        }
    }

    private void makeReservation() {
        try {
            // 1. Collect basic info
            System.out.print("Check-in Date (YYYY-MM-DD): ");
            Date checkIn = Date.valueOf(scanner.nextLine());
            System.out.print("Check-out Date (YYYY-MM-DD): ");
            Date checkOut = Date.valueOf(scanner.nextLine());
            System.out.print("Enter Room NUMBER: ");
            int roomNumber = Integer.parseInt(scanner.nextLine());

            Room room = roomService.getRoomByNumber(roomNumber);
            Guest guest = guestService.getAndRegisterGuest(scanner.nextLine(), scanner);


            ReservationDetails details = new ReservationDetails.Builder(guest.getId(), room.getId())
                    .withDates(checkIn, checkOut)
                    .build();

            int reservationId = resService.createReservation(details);


            double basePrice = room.getPrice();
            double finalPrice = PricingPolicy.getInstance().calculateFinalPrice(basePrice);

            System.out.println("Total Price calculated (Season applied): $" + finalPrice);
            System.out.print("Payment Method: ");
            String method = scanner.nextLine();
            paymentService.processPayment(reservationId, finalPrice, method);

        } catch (Exception e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    private void showReservations() {
        List<Reservation> reservations = resService.getAll();

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        System.out.println("--- Current Reservations ---");
        reservations.forEach(System.out::println);
    }

    private void makePayment() {
        try {
            System.out.println("--- Payment Counter ---");
            System.out.print("Enter Reservation ID: ");
            int resId = scanner.nextInt();
            System.out.print("Enter Amount to Pay: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Payment Method (or 'DECLINE' to test): ");
            String method = scanner.nextLine();

            // Updated to handle PaymentDeclinedException
            paymentService.processPayment(resId, amount, method);
        } catch (PaymentDeclinedException e) {
            System.out.println("Payment Rejected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.print("Enter Reservation ID to cancel: ");
        int resId = scanner.nextInt();
        try {
            resService.cancelReservation(resId);
        } catch (Exception e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }
}