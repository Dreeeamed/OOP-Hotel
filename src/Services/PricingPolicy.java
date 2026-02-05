package Services;

public class PricingPolicy {
    private static PricingPolicy instance;
    private double seasonMultiplier = 1.0;

    private PricingPolicy() {}

    public static synchronized PricingPolicy getInstance() {
        if (instance == null) {
            instance = new PricingPolicy();
        }
        return instance;
    }

    public double calculateFinalPrice(double basePrice) {
        return basePrice * seasonMultiplier;
    }

    public void setSeason(String season) {
        this.seasonMultiplier = switch (season.toLowerCase()) {
            case "summer", "holiday" -> 1.5; // 50% increase
            case "winter" -> 0.8;           // 20% discount
            default -> 1.0;
        };
    }

    public double getMultiplier() { return seasonMultiplier; }
}