import java.util.ArrayList;
import java.util.List;

public class Driver {
    private String name;
    private String licenseNumber;
    private Vehicle vehicle;
    private List<Double> ratings;

    public Driver(String name, String licenseNumber, Vehicle vehicle) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.vehicle = vehicle;
        this.ratings = new ArrayList<>();
    }

    public void addRating(double rating) {
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0;
        }
        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public String getName() {
        return name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getDetails() {
        return "Driver: " + name + "\nLicense: " + licenseNumber + "\nVehicle: " + vehicle.getModel() +
               "\nAverage Rating: " + String.format("%.2f", getAverageRating());
    }
}