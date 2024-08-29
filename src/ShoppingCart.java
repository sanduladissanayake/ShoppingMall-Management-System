import java.util.*;
import java.io.Serializable;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Product> productsList;
    private User user;

    public ShoppingCart(User user) {
        this.productsList = new ArrayList<>();
        this.user = user;
    }

    public void addProduct(Product product) {   // Add a product to the shopping cart
        for (Product p : productsList) {
            if (p.getProductId().equals(product.getProductId())) {     // If the product already in the cart, increase its quantity
                p.setNoOfAvailableItems(p.getNoOfAvailableItems() + 1);
                return;
            }
        }
        Product newProduct = product.clone();
        newProduct.setNoOfAvailableItems(1);
        this.productsList.add(newProduct);
    }

    public Map<String, Double> totalCostCalc() {
        double totalCost = 0.0;
        double discountAmount = 0.0;
        boolean categoryDiscountApplied = false;
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (Product product : productsList) {
            totalCost += product.getProductPrice() * product.getNoOfAvailableItems();
            String category = product.getClass().getSimpleName();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + product.getNoOfAvailableItems());
        }

        // Apply 20% category discount
        for (int count : categoryCounts.values()) {
            if (count >= 3) {
                discountAmount += totalCost * 0.2;
                totalCost *= 0.8;
                categoryDiscountApplied = true;
                break;
            }
        }

        // Apply 10% first purchase discount
        if (user.gotFirstPurchaseDiscount()) {
            double firstPurchaseDiscount = totalCost * 0.1;
            discountAmount += firstPurchaseDiscount;
            totalCost -= firstPurchaseDiscount;
        }

        Map<String, Double> priceDetails = new HashMap<>();
        priceDetails.put("total", totalCost);
        priceDetails.put("discount", discountAmount);
        priceDetails.put("categoryDiscountApplied", categoryDiscountApplied ? 1.0 : 0.0);
        return priceDetails;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clear() {
        productsList.clear();
    }

    public boolean isEmpty() {
        return productsList.isEmpty();
    }

    @Override
    public String toString() {  // string representation of the shopping cart
        StringBuilder sb = new StringBuilder();
        sb.append("Shopping Cart for ").append(user.getUsername()).append(":\n");
        for (Product product : productsList) {
            sb.append(product.toString()).append("\n");
        }
        Map<String, Double> priceDetails = totalCostCalc();
        sb.append("Total Cost: $").append(String.format("%.2f", priceDetails.get("total")));
        sb.append("\nTotal Discount: $").append(String.format("%.2f", priceDetails.get("discount")));
        return sb.toString();
    }
}

