import java.util.ArrayList;
import java.util.List;

public class Order {
    private Customer customer;
    private List<CartItem> cartItems;
    private Sale sale;


    public Order(Customer customer) {
        this.customer = customer;
        this.cartItems = new ArrayList<>();
    }

    public void addToCart(Product product, int quantity) {
        CartItem item = new CartItem(product, quantity);
        cartItems.add(item);
    }

    public void viewCart() {
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Your cart:");
        for (CartItem item : cartItems) {
            System.out.println(item);
        }
        double subtotal = calculateTotal();
        double totalgst = subtotal * (1+ Main.gst);
        System.out.printf("Subtotal: $%.2f\n", subtotal);
        if (sale instanceof OnlineSale && ((OnlineSale) sale).isPlatform()) {
            System.out.printf("Online Processing Fee + Delivery Fee: $%.2f + $%.2f\n", subtotal * OnlineSale.ADDITIONAL_OVERHEAD_PERCENTAGE, OnlineSale.DELIVERY_FEE);
            System.out.printf("GST: $%.2f\n", (subtotal * OnlineSale.ADDITIONAL_OVERHEAD_PERCENTAGE+OnlineSale.DELIVERY_FEE)*Main.gst);
            System.out.printf("Total (including GST): $%.2f\n", ((subtotal *(1+ OnlineSale.ADDITIONAL_OVERHEAD_PERCENTAGE)) + (OnlineSale.DELIVERY_FEE))*(1+Main.gst));
        } else if (sale instanceof DoorSale && ((DoorSale) sale).isDelivery()) {
            System.out.printf("Delivery Fee: $%.2f\n", OnlineSale.DELIVERY_FEE);
            System.out.printf("GST: $%.2f\n", (subtotal + OnlineSale.DELIVERY_FEE) * Main.gst);
            System.out.printf("Total (including GST): $%.2f\n", (subtotal + OnlineSale.DELIVERY_FEE) *(1+ Main.gst));
        } else {
            System.out.printf("GST: $%.2f\n", (subtotal ) * Main.gst);
            System.out.printf("Total (including GST): $%.2f\n", totalgst);
        }
    }

    public void clearCart() {
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            product.addStock(item.getQuantity());
        }
        cartItems.clear();
        System.out.println("Cart cleared.");
    }





    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getBasePrice() * item.getQuantity()* Main.overheadMultiplier;
        }
        return total;
    }

    public void setSaleType(String saleType) {
        if (saleType.equalsIgnoreCase("Online")) {
            this.sale = new OnlineSale();
        } else {
            this.sale = new DoorSale();
        }
    }

    public double getFinalPrice() {
        return sale.calculateFinalPrice(calculateTotal())*(1+Main.gst);
    }

    public void displayOrderDetails() {
        System.out.println("\nCustomer Details:");
        System.out.println("Name: " + customer.getName());
        System.out.println("Contact Number: " + customer.getContactNumber());
        System.out.println("Address: " + customer.getAddress());
        System.out.println("\nOrder Details:");
        viewCart();
        System.out.printf("Final Price: $%.2f\n", getFinalPrice());
    }

    // Getters and setters
    public Customer getCustomer() { return customer; }
    public List<CartItem> getCartItems() { return cartItems; }
    public Sale getSale() { return sale; }

    public static void setOverheadMultiplier(double multiplier) {
        Main.overheadMultiplier = multiplier;
    }
}