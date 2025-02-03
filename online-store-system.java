import java.util.*;

// Interface defining store operations
interface StoreOperations {
    void addProduct(Product product);
    void buyProduct(int productID, int quantity);
    void viewCart();
    void checkout();
}

// Abstract class representing a product
abstract class Product {
    protected int productID;
    protected String name;
    protected double price;
    protected int stock;

    public Product(int productID, String name, double price, int stock) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void reduceStock(int quantity) { this.stock -= quantity; }
}

// Concrete class extending Product
class Item extends Product {
    public Item(int productID, String name, double price, int stock) {
        super(productID, name, price, stock);
    }
}

// OnlineStore class implementing StoreOperations
class OnlineStore implements StoreOperations {
    List<Product> inventory = new ArrayList<>();
    private Map<Product, Integer> cart = new HashMap<>();

    @Override
    public void addProduct(Product product) {
        inventory.add(product);
    }

    @Override
    public void buyProduct(int productID, int quantity) {
        for (Product product : inventory) {
            if (product.getProductID() == productID && product.getStock() >= quantity) {
                cart.put(product, cart.getOrDefault(product, 0) + quantity);
                product.reduceStock(quantity);
                System.out.println(quantity + " " + product.getName() + "(s) added to cart.");
                return;
            }
        }
        System.out.println("Product unavailable or insufficient stock.");
    }

    @Override
    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\nItems in Cart:");
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            System.out.println(entry.getKey().getName() + " - Quantity: " + entry.getValue());
        }
    }

    @Override
    public void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Add products before checkout.");
            return;
        }
        double total = 0;
        System.out.println("\nCheckout Summary:");
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double cost = product.getPrice() * quantity;
            total += cost;
            System.out.println(product.getName() + " - " + quantity + " x $" + product.getPrice() + " = $" + cost);
        }
        System.out.println("Total: $" + total);
        System.out.println("Purchase successful!\n");
        cart.clear();
    }
}

// Main class for user interaction
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OnlineStore store = new OnlineStore();

        // Adding sample products
        store.addProduct(new Item(1, "Laptop", 800, 5));
        store.addProduct(new Item(2, "Phone", 500, 10));
        store.addProduct(new Item(3, "Headphones", 50, 20));

        while (true) {
            System.out.println("\n1. View Products\n2. Buy Product\n3. View Cart\n4. Checkout\n5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("\nAvailable Products:");
                    for (Product product : store.inventory) {
                        System.out.println(product.getProductID() + ". " + product.getName() + " - $" + product.getPrice() + " (Stock: " + product.getStock() + ")");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Product ID: ");
                    int productID = scanner.nextInt();
                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();
                    store.buyProduct(productID, quantity);
                }
                case 3 -> store.viewCart();
                case 4 -> store.checkout();
                case 5 -> {
                    System.out.println("Exiting... Thank you for shopping!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
