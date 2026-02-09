# OOP-Hotel
Adbullah Orinbasar, Bolatbek Bolatk
-----------------------------------
1. Project Overview & Main User Flows
Course: Object Oriented Programming (Java)

Instructor: Traxel Xeniya Alexandrovna

Assignment: #4 Project Defense

The project is a professional-grade booking engine designed to handle real-world scenarios like dynamic pricing and diverse room types. It focuses on maintaining a clean separation between business logic and technical implementation details.

Main User Flows
Dynamic Room Search: Users browse real-time availability across different categories (Suites, Standard, Dorms).

Automated Guest Registration: New users are identified by email and registered seamlessly during their first booking; returning users are identified and welcomed back.

Seasonal Pricing Adjustment: The system automatically calculates the final price based on the hotel’s global "mode" (e.g., applying a holiday multiplier via the Singleton policy).

Integrated Booking & Payment: A single flow that creates a reservation, updates room availability, and records a corresponding payment entry in the database.

2. Database Schema
The system uses a relational database to maintain data integrity across four primary entities.

Entities & Relationships
Guests Table: Stores unique user data (ID, Name, Email). It has a one-to-many relationship with Reservations.

Rooms Table: Stores room details (ID, Number, Floor, Type, Base Price, Availability). It has a one-to-many relationship with Reservations.

Reservations Table: The central link. It holds foreign keys for guest_id and room_id, along with check-in/out dates. It has a one-to-one relationship with Payments.

Payments Table: Records the final amount paid and the timestamp. It links back to a specific reservation_id.

3. Architecture & Design Patterns
The project transitioned from a monolithic structure to a decoupled, package-based architecture.

Package Structure
Entities: Contains the Domain models (Room, SuiteRoom, Guest, Reservation).

Repositories: Contains the Data Access logic (BaseRepository<T>, RoomRepository).

Services: Contains the Business Logic (ReservationService, PaymentService).

Configuration: Contains system setup and global rules (RoomInitialization, PricingPolicy).

UI: Contains the user interaction classes (HotelApplication).

Implemented Design Patterns
Singleton (PricingPolicy): Provides a "Single Source of Truth" for global pricing multipliers. It ensures the entire application uses the same financial logic.

Factory (RoomFactory): Decouples the creation of room subclasses from the database logic. This satisfies the Open/Closed Principle, allowing new room types to be added without changing existing code.

Builder (ReservationDetails): Manages complex object creation with many optional parameters (check-in dates, breakfast), making the service layer code readable and safe.

Generic DAO (BaseRepository<T>): Uses Java Generics to provide reusable CRUD logic for all entities, following the DRY (Don't Repeat Yourself) principle.

4. Component Description & Principles
The project is organized into independent components that follow the principles of component-level design.

Component 1: Domain / Core
Contents: Entities and the PricingPolicy.

CCP (Common Closure Principle): Classes that change for the same business reason (e.g., changing how a "Room" is defined) are grouped here.

CRP (Common Reuse Principle): These core rules are reused by both the Business and Persistence layers; they contain no UI or SQL code.

Component 2: Persistence / Data
Contents: BaseRepository<T>, DatabaseConnection, and SQL-specific repositories.

REP (Release-Reuse Equivalency Principle): This component is a standalone package that can be updated (e.g., switching from H2 to PostgreSQL) without affecting the UI or Business logic.

Component 3: Business / Use Case
Contents: ReservationService, RoomService.

CCP: If the rules for "how to book a room" change, only this component is modified.

CRP: It depends on the Domain component for entities but is decoupled from the UI, allowing it to be reused for a mobile or web interface in the future.

Component 4: UI / Controller
Contents: HotelApplication and console menus.

Responsibility: Only handles user input and output. It depends on the Business component but never interacts with the Persistence layer directly, maintaining a strict one-way dependency flow.
