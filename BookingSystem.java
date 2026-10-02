import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookingSystem {
    private List<Driver> drivers;
    private List<Booking> bookings;

    public BookingSystem() {
        this.drivers = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public Booking createBooking(User user, String vehicleType, String destination, LocalDateTime pickupTime, String pickupLocation, double distance) throws NoAvailableDriverException {
        Driver driver = findAvailableDriver(vehicleType, pickupTime);
        if (driver == null) {
            throw new NoAvailableDriverException("No drivers available for the selected vehicle type and time.");
        }

        Booking booking = new Booking(user, driver, destination, pickupTime, pickupLocation, distance);
        bookings.add(booking);
        user.addBooking(booking);
        return booking;
    }

    private Driver findAvailableDriver(String vehicleType, LocalDateTime pickupTime) {
        List<Driver> availableDrivers = drivers.stream()
            .filter(driver -> driver.getVehicle().getType().equalsIgnoreCase(vehicleType))
            .filter(driver -> isDriverAvailable(driver, pickupTime))
            .toList();

        if (availableDrivers.isEmpty()) {
            return null;
        }

        Random rand = new Random();
        return availableDrivers.get(rand.nextInt(availableDrivers.size()));
    }

    private boolean isDriverAvailable(Driver driver, LocalDateTime pickupTime) {
        return bookings.stream()
            .filter(booking -> booking.getDriver().equals(driver))
            .filter(booking -> booking.getStatus().equals("Booked"))
            .noneMatch(booking -> {
                LocalDateTime bookingStart = booking.getPickupTime();
                LocalDateTime bookingEnd = bookingStart.plusHours(1); // Assume each ride takes 1 hour
                return (pickupTime.isEqual(bookingStart) || pickupTime.isAfter(bookingStart)) 
                    && pickupTime.isBefore(bookingEnd);
            });
    }

    public void cancelBooking(Booking booking, String reason) {
        booking.cancelRide(reason);
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }
}