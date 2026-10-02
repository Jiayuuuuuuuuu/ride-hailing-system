import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RideHailingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static BookingSystem bookingSystem = new BookingSystem();
    private static User currentUser;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        initializeSystem();
        login();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private static void initializeSystem() {
        // Initialize drivers and vehicles
        bookingSystem.addDriver(new Driver("Ameera Sofia", "DL12345", new EconomyCar("Toyota Corolla", "JMP8545")));
        bookingSystem.addDriver(new Driver("Nisa", "DL67890", new LuxuryCar("Mercedes S-Class", "ALL6151")));
        bookingSystem.addDriver(new Driver("Syaza", "DL54321", new SevenSeater("Honda Odyssey", "WWL3661")));
    }

    private static void login() {
        System.out.println("====================================================");
        System.out.println("==== Welcome to the Ride-Hailing Booking System ====");
        System.out.println("====================================================");
        while (currentUser == null) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            currentUser = User.authenticate(username, password);
            if (currentUser != null) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║             Main Menu                ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ 1. Book A Ride                       ║");
            System.out.println("║ 2. Cancel A Ride                     ║");
            System.out.println("║ 3. Edit Ride Details                 ║");
            System.out.println("║ 4. Display Driver's Details          ║");
            System.out.println("║ 5. Display Booking History           ║");
            System.out.println("║ 6. Rate a Driver                     ║");
            System.out.println("║ 7. Logout                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    bookRide();
                    break;
                case 2:
                    cancelRide();
                    break;
                case 3:
                    editRideDetails();
                    break;
                case 4:
                    displayDriverDetails();
                    break;
                case 5:
                    displayBookingHistory();
                    break;
                case 6:
                    rateDriver();
                    break;
                case 7:
                    System.out.print("Are you sure you want to logout? (Y/N): ");
                    String confirmation = scanner.nextLine().toUpperCase();
                    if (confirmation.equals("Y")) {
                        System.out.println("Logging out. Goodbye!");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void bookRide() {
        System.out.println();
        System.out.println("+--------------------+");
        System.out.println("|                    |");
        System.out.println("|   Booking a Ride   |");
        System.out.println("|                    |");
        System.out.println("+--------------------+");
        System.out.println();
        System.out.print("Enter vehicle type (E for Economy, L for Luxury, 7 for 7-Seater): ");
        String vehicleTypeInput = scanner.nextLine().toUpperCase();
        String vehicleType;
        switch (vehicleTypeInput) {
            case "E":
                vehicleType = "Economy";
                break;
            case "L":
                vehicleType = "Luxury";
                break;
            case "7":
                vehicleType = "7-Seater";
                break;
            default:
                System.out.println("Invalid vehicle type. Please try again.");
                return;
        }

        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        LocalDateTime pickupDateTime = null;
        while (pickupDateTime == null) {
            System.out.print("Enter pickup time (YYYY-MM-DD HH:MM): ");
            String pickupTimeInput = scanner.nextLine();
            try {
                pickupDateTime = LocalDateTime.parse(pickupTimeInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date-time format. Please use YYYY-MM-DD HH:MM");
            }
        }

        System.out.print("Enter pickup location: ");
        String pickupLocation = scanner.nextLine();

        double distance = 0;
        while (distance <= 0) {
            System.out.print("Enter distance (km): ");
            try {
                distance = Double.parseDouble(scanner.nextLine());
                if (distance <= 0) {
                    System.out.println("Distance must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        try {
            Booking booking = bookingSystem.createBooking(currentUser, vehicleType, destination, pickupDateTime, pickupLocation, distance);
            System.out.println("\nBooking successful!");
            System.out.println(booking.getRideSummary());

            System.out.print("Choose payment method (C for Cash, O for OnlineBanking): ");
            String paymentMethodInput = scanner.nextLine().toUpperCase();
            Payment payment;
            switch (paymentMethodInput) {
                case "C":
                    payment = new CashPayment();
                    break;
                case "O":
                    payment = new OnlineBankingPayment();
                    break;
                default:
                    System.out.println("Invalid payment method. Booking cancelled.");
                    bookingSystem.cancelBooking(booking, "Invalid payment method");
                    return;
            }

            if (payment.processPayment(booking.getFare())) {
                System.out.println("Payment successful!");
                System.out.println("\nRide booked. Here's your booking details:");
                System.out.println(booking.getRideSummary());
            } else {
                System.out.println("Payment failed. Booking cancelled.");
                bookingSystem.cancelBooking(booking, "Payment failed");
            }
        } catch (NoAvailableDriverException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelRide() {
        System.out.println();
        System.out.println("+--------------------+");
        System.out.println("|                    |");
        System.out.println("|   Cancel a Ride    |");
        System.out.println("|                    |");
        System.out.println("+--------------------+");
        System.out.println();
        List<Booking> bookings = currentUser.getBookingHistory();
        List<Booking> activeBookings = bookings.stream()
                .filter(b -> b.getStatus().equals("Booked"))
                .toList();

        if (activeBookings.isEmpty()) {
            System.out.println("You have no active bookings to cancel.");
            return;
        }

        while (true) {
            System.out.println("Your current bookings:");
            for (int i = 0; i < activeBookings.size(); i++) {
                System.out.println((i + 1) + ". " + activeBookings.get(i).getRideSummary());
            }

            System.out.print("Enter the number of the booking you want to cancel (or 0 to go back): ");
            int bookingIndex = scanner.nextInt() - 1;
            scanner.nextLine(); 

            if (bookingIndex == -1) {
                return; // Go back to main menu
            }

            if (bookingIndex >= 0 && bookingIndex < activeBookings.size()) {
                Booking bookingToCancel = activeBookings.get(bookingIndex);
                System.out.print("Are you sure you want to cancel this booking? (Y/N): ");
                String confirmation = scanner.nextLine().toUpperCase();
                if (confirmation.equals("Y")) {
                    System.out.print("Enter the reason for cancellation: ");
                    String reason = scanner.nextLine();
                    bookingSystem.cancelBooking(bookingToCancel, reason);
                    System.out.println("Booking cancelled successfully.");
                    return;
                } else {
                    System.out.println("Cancellation aborted.");
                    return;
                }
            } else {
                System.out.println("Invalid booking number. Please try again.");
            }
        }
    }

    private static void editRideDetails() {
        System.out.println();
        System.out.println("+-----------------------+");
        System.out.println("|                       |");
        System.out.println("|   Edit Ride Details   |");
        System.out.println("|                       |");
        System.out.println("+-----------------------+");
        System.out.println();
        List<Booking> bookings = currentUser.getBookingHistory();
        List<Booking> activeBookings = bookings.stream()
                .filter(b -> b.getStatus().equals("Booked"))
                .toList();

        if (activeBookings.isEmpty()) {
            System.out.println("You have no active bookings to edit.");
            return;
        }

        System.out.println("Your current bookings:");
        for (int i = 0; i < bookings.size(); i++) {
            System.out.println((i + 1) + ". " + bookings.get(i).getRideSummary());
        }

        System.out.print("Enter the number of the booking you want to edit: ");
        int bookingIndex = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (bookingIndex >= 0 && bookingIndex < bookings.size()) {
            Booking bookingToEdit = bookings.get(bookingIndex);
            System.out.println("What would you like to edit?");
            System.out.println("1. Destination");
            System.out.println("2. Pickup Time");
            System.out.println("3. Pickup Location");
            System.out.println("4. Distance");
            System.out.print("Enter your choice: ");
            int editChoice = scanner.nextInt();
            scanner.nextLine(); 

            switch (editChoice) {
                case 1:
                    System.out.print("Enter new destination: ");
                    String newDestination = scanner.nextLine();
                    bookingToEdit.setDestination(newDestination);
                    break;
                case 2:
                    System.out.print("Enter new pickup time (yyyy-MM-dd HH:mm): ");
                    String newPickupTime = scanner.nextLine();
                    bookingToEdit.setPickupTime(LocalDateTime.parse(newPickupTime, formatter));
                    break;
                case 3:
                    System.out.print("Enter new pickup location: ");
                    String newPickupLocation = scanner.nextLine();
                    bookingToEdit.setPickupLocation(newPickupLocation);
                    break;
                case 4:
                    System.out.print("Enter new distance (km): ");
                    double newDistance = scanner.nextDouble();
                    scanner.nextLine(); 
                    bookingToEdit.setDistance(newDistance);
                    break;
                default:
                     System.out.println("Invalid choice.");
                     return;
            }
            System.out.println("Booking updated successfully.");
            System.out.println(bookingToEdit.getRideSummary());
        } else {
            System.out.println("Invalid booking number.");
        }
    }

    private static void displayDriverDetails() {
        List<Booking> bookings = currentUser.getBookingHistory();
        List<Booking> activeBookings = bookings.stream()
                .filter(Booking::isActive)
                .toList();
        
        System.out.println();
        System.out.println("+---------------------+");
        System.out.println("|                     |");
        System.out.println("|   Driver's Details  |");
        System.out.println("|                     |");
        System.out.println("+---------------------+");
        System.out.println();

        if (activeBookings.isEmpty()) {
            System.out.println("You have no active bookings to display driver details.");
            return;
        }

        System.out.println("Your active bookings:");
        for (int i = 0; i < activeBookings.size(); i++) {
            System.out.println((i + 1) + ". " + activeBookings.get(i).getRideSummary());
        }

        System.out.print("Enter the number of the booking to view driver details: ");
        int bookingIndex = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (bookingIndex >= 0 && bookingIndex < activeBookings.size()) {
            Driver driver = activeBookings.get(bookingIndex).getDriver();
            System.out.println(driver.getDetails());
        } else {
            System.out.println("Invalid booking number.");
        }
    }

    private static void displayBookingHistory() {
        System.out.println();
        System.out.println("+--------------------------+");
        System.out.println("|                          |");
        System.out.println("|   Your Booking History   |");
        System.out.println("|                          |");
        System.out.println("+--------------------------+");
        System.out.println();
        List<Booking> bookings = currentUser.getBookingHistory();
        if (bookings.isEmpty()) {
            System.out.println("You have no booking history.");
            return;
        }

        System.out.println("Your booking history:");
        for (Booking booking : bookings) {
            System.out.println(booking.getRideSummary());
            System.out.println("---------------------------");
        }
    }

    private static void rateDriver() {
        System.out.println();
        System.out.println("+----------------------+");
        System.out.println("|                      |");
        System.out.println("|  Rating Your Driver  |");
        System.out.println("|                      |");
        System.out.println("+----------------------+");
        System.out.println();
        List<Booking> bookings = currentUser.getBookingHistory();
        List<Booking> activeBookings = bookings.stream()
                .filter(Booking::isActive)
                .toList();

        if (activeBookings.isEmpty()) {
            System.out.println("You have no active bookings to rate.");
            return;
        }

        System.out.println("Your active bookings:");
        for (int i = 0; i < activeBookings.size(); i++) {
            System.out.println((i + 1) + ". " + activeBookings.get(i).getRideSummary());
        }

        System.out.print("Enter the number of the booking to rate the driver: ");
        int bookingIndex = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (bookingIndex >= 0 && bookingIndex < activeBookings.size()) {
            Booking bookingToRate = activeBookings.get(bookingIndex);
            System.out.println(bookingToRate.getRideSummary());
            System.out.print("Enter rating (1-5 stars): ");
            double rating = scanner.nextDouble();
            scanner.nextLine(); 

            if (rating >= 1 && rating <= 5) {
                bookingToRate.getDriver().addRating(rating);
                System.out.println("Rating submitted successfully.");
            } else {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
            }
        } else {
            System.out.println("Invalid booking number.");
        }
    }
}