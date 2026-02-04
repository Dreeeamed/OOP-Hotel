package edu.aitu.oop3.db;

import Configuration.DatabaseConnection;
import Repositories.*;
import Services.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Establish Database Connection
            Connection connection = DatabaseConnection.getConnection();

            // 2. Initialize Repositories (Order by Dependency)
            GuestRepository guestRepo = new GuestRepository(connection);
            RoomRepository roomRepo = new RoomRepository(connection);
            ReservationRepository resRepo = new ReservationRepository(connection);
            PaymentRepository paymentRepo = new PaymentRepository(connection);

            // Create Tables
            guestRepo.createTable();
            roomRepo.createTable();
            resRepo.createTable();
            paymentRepo.createTable();

            // 3. Initialize Services (Inject Repositories)
            GuestService guestService = new GuestService(guestRepo);
            RoomService roomService = new RoomService(roomRepo);
            RoomAvailabilityService availabilityService = new RoomAvailabilityService(connection, roomRepo);
            ReservationService resService = new ReservationService(resRepo, roomRepo);
            PaymentService paymentService = new PaymentService(paymentRepo);

            // 4. Initial Setup
            roomService.initializeRoomsIfNeeded();

            // 5. Start Application with 5 arguments
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