import java.io.Serializable;
public abstract class Product implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L; // Add a version UID
    private String productId;
    private String productName;
    private int noOfAvailableItems;
    private double productPrice;

    public Product(String productId, String productName, int noOfAvailableItems, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.noOfAvailableItems = noOfAvailableItems;
        this.productPrice = productPrice;
    }

    public Product clone() {
        try {
            return (Product) super.clone();
        }catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clonning not supported", e);
        }
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNoOfAvailableItems() {
        return noOfAvailableItems;
    }
    public void setNoOfAvailableItems(int noOfAvailableItems) {
        this.noOfAvailableItems = noOfAvailableItems;
    }

    public double getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return String.format("Product Id: %s, Product Name: %s, Available Quantity: %d, Product Price: %.2f",
                getProductId(), getProductName(), getNoOfAvailableItems(), getProductPrice());
    }
}