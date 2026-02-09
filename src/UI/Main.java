package UI;

import Configuration.DatabaseConnection;
import Repositories.*;
import Services.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            IGuestRepository guestRepo = new GuestRepository(connection);
            IRoomRepository roomRepo = new RoomRepository(connection);
            IReservationRepository resRepo = new ReservationRepository(connection);
            IPaymentRepository paymentRepo = new PaymentRepository(connection);

            guestRepo.createTable();
            roomRepo.createTable();
            resRepo.createTable();
            paymentRepo.createTable();

            GuestService guestService = new GuestService(guestRepo);
            RoomService roomService = new RoomService(roomRepo);
            RoomAvailabilityService availabilityService = new RoomAvailabilityService(connection, roomRepo);
            ReservationService resService = new ReservationService(resRepo, roomRepo);
            PaymentService paymentService = new PaymentService(paymentRepo);

            PricingPolicy.getInstance().setSeason("Holiday");
            System.out.println("--- HOLYDAY SEASON - ON (test) ---");

            roomService.initializeRoomsIfNeeded();

            HotelApplication app = new HotelApplication(
                    guestService,
                    roomService,
                    availabilityService,
                    resService,
                    paymentService
            );
            app.start();

        } catch (Exception e) {
            System.err.println("Critical System Failure: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
