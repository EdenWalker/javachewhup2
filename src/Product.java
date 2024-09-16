public class Product {
    private String name;
    private String type;
    private String brand;
    private String sku;
    private double basePrice;
    private int inventoryCount;
    private double weight;

    public Product(String name, String type, String brand, String sku, double basePrice, int inventoryCount, double weight) {
        this.name = name;
        this.type = type;
        this.brand = brand;
        this.sku = sku;
        this.basePrice = basePrice;
        this.inventoryCount = inventoryCount;
        this.weight = weight;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getSku() { return sku; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public int getInventoryCount() { return inventoryCount; }
    public void setInventoryCount(int inventoryCount) { this.inventoryCount = inventoryCount; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public void addStock(int quantity) {
        this.inventoryCount += quantity;
    }

    @Override
    public String toString() {
        double adjustedPrice = basePrice * Main.overheadMultiplier;

        return String.format("SKU: %s, Name: %s, Brand: %s, Type: %s, Price: $%.2f, Inventory: %d, Weight: %.2f kg",
                sku, name, brand, type, adjustedPrice, inventoryCount, weight);

    }
}
