public abstract class Vehicle {
    protected String model;
    protected String licensePlate;
    protected String type;

    public Vehicle(String model, String licensePlate, String type) {
        this.model = model;
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public abstract double calculateFare(double distance);

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getType() {
        return type;
    }
}

class EconomyCar extends Vehicle {
    public EconomyCar(String model, String licensePlate) {
        super(model, licensePlate, "Economy");
    }

    @Override
    public double calculateFare(double distance) {
        return distance * 1.5; // RM1.5 per km for economy cars
    }
}

class LuxuryCar extends Vehicle {
    public LuxuryCar(String model, String licensePlate) {
        super(model, licensePlate, "Luxury");
    }

    @Override
    public double calculateFare(double distance) {
        return distance * 3.0; // RM3 per km for luxury cars
    }
}

class SevenSeater extends Vehicle {
    public SevenSeater(String model, String licensePlate) {
        super(model, licensePlate, "7-Seater");
    }

    @Override
    public double calculateFare(double distance) {
        return distance * 2.5; // RM2.5 per km for 7-seaters
    }
}