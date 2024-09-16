public abstract class Sale {
    protected String saleType;
    protected double finalPrice;

    public Sale(String saleType) {
        this.saleType = saleType;
    }



    public abstract double calculateFinalPrice(double basePrice);


    public void setDelivery(boolean b) {
    }
}
