public class DoorSale extends Sale {

    private boolean isDelivery;

    public DoorSale() {
        super("Door");
        this.isDelivery = false;
    }

    @Override
    public double calculateFinalPrice(double basePrice) {
        this.finalPrice = basePrice;
        if (isDelivery) {
            this.finalPrice += OnlineSale.DELIVERY_FEE;
        }
        return finalPrice;
    }

    public void setDelivery(boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public boolean isDelivery() {
        return isDelivery;

    }
}