import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private List<ShoppingCart> pastPurchases;
    private boolean gotFirstPurchaseDiscount;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.pastPurchases = new ArrayList<>();
        this.gotFirstPurchaseDiscount = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ShoppingCart> getPastPurchases() {
        return pastPurchases;
    }

    public void addPurchase(ShoppingCart cart) {
        pastPurchases.add(new ShoppingCart(this));
        for (Product product : cart.getProductsList()) {
            pastPurchases.get(pastPurchases.size() - 1).addProduct(product.clone());
        }
        gotFirstPurchaseDiscount = false;
    }

    public boolean gotFirstPurchaseDiscount() {
        return gotFirstPurchaseDiscount;
    }

    @Override
    public String toString() {
        return "User: " + username + ", Purchases: " + pastPurchases.size();
    }
}
