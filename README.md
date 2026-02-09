# OOP-Hotel
Adbullah Orinbasar, Bolatbek Bolatk
-----------------------------------
1. Project Overview & Topic
The goal of this project is to develop a robust, 3-tier Java application for managing hotel operations. The system handles the core business logic of a hotel, including room availability management, guest registration, dynamic pricing based on seasons, and reservation processing.

This project uses a custom domain (Topic 12) to demonstrate advanced Object-Oriented Programming principles, including polymorphism, strict encapsulation, and design patterns.

2. Domain Entities & Database Schema
The system is built around four core entities which map directly to a PostgreSQL database hosted on Supabase.

2.1 Entities
Guest: Represents a hotel customer.

Attributes: id, name, email.

Constraint: Emails must be unique to prevent duplicate profiles.

Room: An abstract base class representing a physical room.

Subclasses: StandardRoom, SuiteRoom, DormRoom.

Attributes: id, room_number, floor, price, type, is_available.

Reservation: The central entity linking a Guest to a Room for a specific date range.

Attributes: id, check_in_date, check_out_date.

Relationships: Many-to-One with Guest; Many-to-One with Room.

Payment: A financial record associated with a completed reservation.

Attributes: id, amount, payment_date.

Relationship: One-to-One with Reservation.

2.2 Database Tables (Schema)
guests: id (PK, Serial), name (VARCHAR), email (VARCHAR, Unique)

rooms: id (PK, Serial), room_number (INT, Unique), floor (INT), price (DECIMAL), room_type (VARCHAR), is_available (BOOLEAN)

reservations: id (PK, Serial), guest_id (FK -> guests.id), room_id (FK -> rooms.id), check_in_date (DATE), check_out_date (DATE)

payments: id (PK, Serial), reservation_id (FK -> reservations.id), amount (DECIMAL), payment_date (DATE)

3. System Architecture
The application follows a strict 3-Tier Architecture to separate concerns and ensure maintainability.

3.1 Packages
Presentation Layer (edu.aitu.oop3.db): Contains HotelApplication and Main. Handles user input via Console and displays results. It depends only on the Service layer.

Business Logic Layer (Services): Contains logic classes like ReservationService, PricingPolicy, and RoomAvailabilityService. This layer performs calculations and validations (e.g., checking date overlaps).

Data Access Layer (Repositories): Contains BaseRepository and specific implementations (RoomRepository, etc.). This layer handles JDBC connections and SQL queries.

Configuration: Manages database connections (DatabaseConnection) and object factories (RoomFactory).

3.2 Component Principles (REP, CCP, CRP)
REP (Reuse-Release Equivalence Principle): The Entities package acts as a reusable component. It contains only POJOs (Plain Old Java Objects) and has no dependencies on the database or UI, allowing it to be reused in other contexts.

CCP (Common Closure Principle): Classes that change together are packaged together.

Example: If we migrate from PostgreSQL to MySQL, we only need to modify the Repositories package. The Services and UI packages remain untouched, proving high cohesion.

CRP (Common Reuse Principle): We ensure that classes do not depend on things they don't use.

Example: The UI (HotelApplication) depends only on the Service Interfaces. It does not import or depend on the internal JDBC logic in the Repositories.

4. Applied Design Patterns (Milestone 2)
4.1 Singleton Pattern
Class: Services.PricingPolicy

Purpose: To maintain a single global configuration for seasonal pricing (e.g., "Holiday Season" vs. "Standard Season").

Implementation: A private constructor ensures no other class can instantiate it. A static getInstance() method provides global access to the single instance.

4.2 Factory Method Pattern
Class: Configuration.RoomFactory

Purpose: To encapsulate the logic of creating different room subtypes.

Implementation: The client code calls RoomFactory.createRoom("Suite", ...). The factory handles the switch logic to return a SuiteRoom instance. This satisfies the Open/Closed Principle (OCP), as adding a new room type only requires updating the factory.

4.3 Builder Pattern
Class: Services.ReservationDetails.Builder

Purpose: To simplify the construction of complex reservation objects that require multiple parameters (guest ID, room ID, check-in, check-out).

Implementation: Prevents "Telescoping Constructor" anti-patterns and improves code readability (e.g., .withDates(in, out)).

5. Technical Implementation Highlights
5.1 Generics
We implemented a generic repository pattern in BaseRepository<T>.

Function: It defines common database operations like add(), getAll(), getById(), and delete().

Benefit: This reduced code duplication by approximately 40%, as we wrote the SELECT * FROM ... logic once for all entities.

5.2 Lambdas & Functional Interfaces
Used in HotelApplication to process lists of rooms efficiently.

Example: availableRooms.forEach(System.out::println); uses a Consumer functional interface to print room details cleanly.

5.3 Exception Handling
Custom exceptions were created to manage specific error states:

PaymentDeclinedException: Thrown when a payment exceeds the limit ($10,000).

GuestNotFoundException: Thrown when searching for a non-existent user.

DatabaseException: Wraps SQL errors to prevent leaky abstractions.

6. Main User Flows
Availability Search: The user inputs a date range. RoomAvailabilityService executes a SQL query to find rooms not present in the reservations table for those dates.

Booking Engine: The user selects a room. The system checks if the guest email exists; if not, it auto-registers the guest. A Reservation is then created using the Builder pattern.

Payment Processing: The user enters a reservation ID. The system validates the amount and method (Cash/Credit) before committing the transaction to the database.

7. Conclusion
In this milestone, we successfully migrated from a basic console app to a fully layered, database-backed system. We demonstrated mastery of OOP concepts by replacing hard-coded logic with polymorphic structures and integrating industry-standard design patterns. The strict adherence to SOLID and Component principles ensures the application is ready for future scaling.
