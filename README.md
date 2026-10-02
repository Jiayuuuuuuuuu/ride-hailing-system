# Ride-Hailing System

A console-based ride-hailing booking system written in Java. Users log in, book a ride with a driver of their chosen vehicle type, manage their bookings, pay, and rate the driver. The project demonstrates core object-oriented programming concepts.

## Features

- User login (authentication)
- Book a ride by vehicle type, pickup location, destination, pickup time and distance
- Automatic fare calculation based on vehicle type
- Random assignment of an available driver (no double-booking: each ride is assumed to take 1 hour)
- Cancel a ride and edit ride details
- View driver details and average rating
- View booking history
- Rate a driver
- Payment by cash or online banking

## Fare Rates

| Vehicle Type | Rate per km |
|--------------|-------------|
| Economy      | RM 1.50     |
| 7-Seater     | RM 2.50     |
| Luxury       | RM 3.00     |

## OOP Concepts Used

| Concept | Where |
|---------|-------|
| Abstraction / Inheritance | `Vehicle` (abstract) -> `EconomyCar`, `SevenSeater`, `LuxuryCar` |
| Polymorphism | `calculateFare()` overridden per vehicle type |
| Interface | `Payment` -> `CashPayment`, `OnlineBankingPayment` |
| Custom exception | `NoAvailableDriverException` |
| Composition / Aggregation | `Driver` has a `Vehicle`; `BookingSystem` has `Driver`s and `Booking`s; `User` has booking history |
| Encapsulation | Private fields with getters/setters |

## Project Structure

```
.
├── RideHailingSystem.java          # Main class (console menu)
├── BookingSystem.java              # Booking and driver-availability logic
├── Booking.java                    # Booking details, status and fare
├── User.java                       # User accounts and authentication
├── Driver.java                     # Driver details and ratings
├── Vehicle.java                    # Abstract Vehicle + EconomyCar, SevenSeater, LuxuryCar
├── Payment.java                    # Payment interface + CashPayment, OnlineBankingPayment
├── NoAvailableDriverException.java # Custom checked exception
└── docs/
    └── uml-class-diagram.png       # UML class diagram
```

## Getting Started

### Requirements

- Java 16 or later (the code uses `Stream.toList()`)

### Compile and Run

```bash
javac *.java
java RideHailingSystem
```

### Demo Accounts

| Username | Password |
|----------|----------|
| Jia Yu   | abcd     |
| Boon     | 1234     |

## Known Limitations

- Passwords are stored in plain text (for demonstration only)
- Data is held in memory and is lost when the program exits
- Distance is entered manually rather than calculated from locations
