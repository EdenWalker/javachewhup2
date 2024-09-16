import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();
    private static ArrayList<Product> archivedProducts = new ArrayList<>();
    public static double gst = 0.09;
    public static double overheadMultiplier = 1.3;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        initialProducts();
        while (true) {
            mainMenu();
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    handleCustomer();
                    break;
                case 2:
                    handleAdmin();
                    break;
                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                case 4:
                    showArchivedProducts();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void mainMenu() {
        System.out.println("--- Main Menu ---");
        System.out.println("1. Customer");
        System.out.println("2. Company Admin");
        System.out.println("3. Exit");
    }

    private static void handleCustomer() {
        System.out.print("Enter customer ID or 'new' for new customer: ");
        String input = sc.nextLine();

        Customer customer;
        if (input.equalsIgnoreCase("new")) {
            customer = createNewCustomer();
            customers.add(customer);
        } else {
            customer = findCustomer(input);
            if (customer == null) {
                System.out.println("Customer not found.");
                return;
            }
        }

        Order order = new Order(customer);
        boolean continueOrder = true;

        while (continueOrder) {
            displayCustomerMenu();
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addToCart(order);
                    break;
                case 2:
                    order.viewCart();
                    break;
                case 3:
                    order.clearCart();
                    break;
                case 4:
                    placeOrder(order);
                    continueOrder = false;
                    break;
                case 5:
                    continueOrder = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAdmin() {
        boolean adminPage = true;

        while (adminPage) {
            displayAdminMenu();
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addNewProduct();
                    break;
                case 2:
                    modifyGST();
                    break;
                case 3:
                    modifyMargin();
                    break;
                case 4:
                    productList();
                    break;
                case 5:
                    modifyOrRemoveProduct();
                    break;
                case 6:
                    showRecentOrders();
                    break;
                case 7:
                    addStock();
                    break;
                case 8:
                    modifyCustomerData();
                    break;
                case 9:
                    adminPage = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static Customer createNewCustomer() {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        System.out.print("Enter contact number: ");
        String contactNumber = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        String id = "C" + (customers.size() + 1);
        return new Customer(id, name, contactNumber, address);
    }

    private static Customer findCustomer(String id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    private static void displayCustomerMenu() {
        System.out.println("--- Customer Menu ---");
        System.out.println("1. Add to cart");
        System.out.println("2. Look at cart");
        System.out.println("3. Clear cart");
        System.out.println("4. Place order");
        System.out.println("5. Return to main menu");
    }

    private static void addToCart(Order order) {
        productList();
        System.out.print("Enter SKU of the product you want to add: ");
        String sku = sc.nextLine();
        Product product = findProduct(sku);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();
        if (quantity <= 0 || quantity > product.getInventoryCount()) {
            System.out.println("Invalid quantity.");
            return;
        }
        order.addToCart(product, quantity);
        product.setInventoryCount(product.getInventoryCount() - quantity);
        System.out.println("Product added to cart.");
    }

    private static void placeOrder(Order order) {
        if (order.getCartItems().isEmpty()) {
            System.out.println("Cart is empty. Cannot place order.");
            return;
        }
        System.out.print("Is this a digital sale? (y/n): ");
        String isSaleDigital = sc.nextLine().toLowerCase();
        if (isSaleDigital.equals("y")) {
            order.setSaleType("Online");
            ((OnlineSale) order.getSale()).setPlatform(true);
        } else {
            order.setSaleType("Door");
            System.out.print("Is this for pickup or delivery? (p/d): ");
            String deliveryOption = sc.nextLine().toLowerCase();
            if (deliveryOption.equals("d")) {
                order.getSale().setDelivery(true);
            }
        }

        System.out.println("Order Summary:");
        order.displayOrderDetails();

        orders.add(order);
        System.out.println("Order placed successfully.");
    }


    private static void displayAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. New product");
        System.out.println("2. Modify GST");
        System.out.println("3. Modify margin");
        System.out.println("4. Product list");
        System.out.println("5. Modify/Remove product");
        System.out.println("6. Show recent orders");
        System.out.println("7. Add stock");
        System.out.println("8. Modify customer data");
        System.out.println("9. Return to main menu");
    }
    private static void modifyCustomerData() {
        System.out.print("Enter customer ID to modify: ");
        String customerId = sc.nextLine().toUpperCase();
        Customer customer = findCustomer(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Current customer data:");
        System.out.println(customer);

        System.out.println("Enter new values (or press Enter to keep current value):");

        System.out.print("Name (" + customer.getName() + "): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) customer.setName(name);

        System.out.print("Contact number (" + customer.getContactNumber() + "): ");
        String contactNumber = sc.nextLine();
        if (!contactNumber.isEmpty()) customer.setContactNumber(contactNumber);

        System.out.print("Address (" + customer.getAddress() + "): ");
        String address = sc.nextLine();
        if (!address.isEmpty()) customer.setAddress(address);

        System.out.println("Customer data updated successfully.");
        System.out.println("Updated customer data:");
        System.out.println(customer);
    }

    private static void addNewProduct() {
        System.out.print("Enter product name: ");
        String name = sc.nextLine();
        System.out.print("Enter product type: ");
        String type = sc.nextLine();
        System.out.print("Enter brand: ");
        String brand = sc.nextLine();
        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();
        System.out.print("Enter base price: ");
        double basePrice = sc.nextDouble();
        System.out.print("Enter initial inventory count: ");
        int inventoryCount = sc.nextInt();
        System.out.print("Enter weight (in kg): ");
        double weight = sc.nextDouble();
        sc.nextLine();

        Product newProduct = new Product(name, type, brand, sku, basePrice, inventoryCount, weight);
        products.add(newProduct);
        System.out.println("Product added successfully.");
    }

    private static void modifyGST() {
        System.out.printf("Current GST: %.2f", gst);
        System.out.print("Enter new GST (as a decimal, e.g., 0.09 for 9%): ");
        double newGST = sc.nextDouble();
        sc.nextLine();
        gst = newGST;
        System.out.println("GST updated successfully.");
    }
    private static void modifyMargin() {
        System.out.printf("Current overhead multiplier: %.2f", overheadMultiplier);
        System.out.print("Enter new overhead multiplier: ");
        double newMultiplier = sc.nextDouble();
        sc.nextLine();
        overheadMultiplier = newMultiplier;
        Order.setOverheadMultiplier(newMultiplier);
        System.out.println("Overhead multiplier updated successfully.");
    }


    private static void productList() {
        System.out.println("--- Product List ---");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void modifyOrRemoveProduct() {
        productList();
        System.out.print("Enter SKU of the product to modify/remove: ");
        String sku = sc.nextLine();
        Product product = findProduct(sku);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("1. Modify");
        System.out.println("2. Remove");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            modifyProduct(product);
        } else if (choice == 2) {
            removeProduct(product);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void modifyProduct(Product product) {
        System.out.println("Enter new values (or press Enter to keep current value):");

        System.out.print("Name (" + product.getName() + "): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) product.setName(name);

        System.out.print("Type (" + product.getType() + "): ");
        String type = sc.nextLine();
        if (!type.isEmpty()) product.setType(type);

        System.out.print("Brand (" + product.getBrand() + "): ");
        String brand = sc.nextLine();
        if (!brand.isEmpty()) product.setBrand(brand);

        System.out.print("Base price (" + product.getBasePrice() + "): ");
        String basePrice = sc.nextLine();
        if (!basePrice.isEmpty()) product.setBasePrice(Double.parseDouble(basePrice));

        System.out.print("Weight (" + product.getWeight() + "): ");
        String weight = sc.nextLine();
        if (!weight.isEmpty()) product.setWeight(Double.parseDouble(weight));

        System.out.println("Product updated successfully.");
    }

    private static void removeProduct(Product product) {
        products.remove(product);
        archivedProducts.add(product);
        System.out.println("Product removed and archived successfully.");
    }

    private static void showRecentOrders() {
        System.out.println("--- Recent Orders ---");
        for (int i = Math.max(0, orders.size() - 5); i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.printf("\nOrder for %s (ID: %s):", order.getCustomer().getName(), order.getCustomer().getId());
            order.viewCart();
            System.out.printf("Final price: $%.2f", order.getFinalPrice());
        }
    }

    private static void addStock() {
        productList();
        System.out.print("Enter SKU of the product to add stock: ");
        String sku = sc.nextLine();
        Product product = findProduct(sku);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.print("Enter quantity to add: ");
        int quantity = sc.nextInt();
        sc.nextLine(); // Consume newline
        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }
        product.addStock(quantity);
        System.out.println("Stock added successfully.");
    }

    private static Product findProduct(String sku) {
        for (Product product : products) {
            if (product.getSku().equals(sku)) {
                return product;
            }
        }
        return null;
    }

    private static void showArchivedProducts() {
        System.out.println("--- Archived Products ---");
        for (Product product : archivedProducts) {
            System.out.println(product);
        }
    }

    private static void initialProducts() {
        products.add(new Product("Rice Flour 25kg", "Rice Flour", "Erawan", "RFER25", 18, 100, 25));
        products.add(new Product("Corn Starch 25kg", "Corn Starch Flour", "A1", "CSA125", 20, 100, 25));
        products.add(new Product("Glutinous Rice Flour 25kg", "Glutinous Rice Flour", "Erawan", "GRFER25", 20, 100, 25));
        products.add(new Product("Wheat Maltose 25L", "Malt", "3 Goat", "WM3G25", 17, 100, 25));
    }
}