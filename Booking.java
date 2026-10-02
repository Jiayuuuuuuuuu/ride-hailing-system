import java.time.LocalDateTime;

public class Booking {
    private User user;
    private Driver driver;
    private String destination;
    private LocalDateTime pickupTime;
    private String pickupLocation;
    private double distance;
    private double fare;
    private String status;

    public Booking(User user, Driver driver, String destination, LocalDateTime pickupTime, String pickupLocation, double distance) {
        this.user = user;
        this.driver = driver;
        this.destination = destination;
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.distance = distance;
        this.fare = driver.getVehicle().calculateFare(distance);
        this.status = "Booked";
    }

    public void cancelRide(String reason) {
        this.status = "Booking Cancelled";
    }

    public void completeRide() {
        this.status = "booking Completed";
    }

    public String getRideSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Ride Summary:\n");
        summary.append("Status: ").append(status).append("\n");
        summary.append("Driver: ").append(driver.getName()).append("\n");
        summary.append("Vehicle: ").append(driver.getVehicle().getModel()).append(" (").append(driver.getVehicle().getType()).append(")\n");
        summary.append("Pickup Location: ").append(pickupLocation).append("\n");
        summary.append("Destination: ").append(destination).append("\n");
        summary.append("Distance: ").append(String.format("%.2f", distance)).append(" km\n");
        summary.append("Fare: RM").append(String.format("%.2f", fare)).append("\n");
        return summary.toString();
    }

    public User getUser() { return user; }
    public Driver getDriver() { return driver; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { 
        this.distance = distance;
        this.fare = driver.getVehicle().calculateFare(distance);
    }
    public double getFare() { return fare; }
    public String getStatus() { return status; }
    public boolean isActive() {
        return status.equals("Booked");
    }
}