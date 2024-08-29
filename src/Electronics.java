import java.io.Serializable;
public class Electronics extends Product implements Serializable {
    private static final long serialVersionUID = 1L; // Add a version UID
    private String productBrand;
    private int warranty;

    public Electronics(String productId, String productName, int noOfAvailableItems, double productPrice, String productBrand, int warranty) {
        super(productId, productName, noOfAvailableItems, productPrice);
        this.productBrand = productBrand;
        this.warranty = warranty;
    }

    public String getProductBrand() {
        return productBrand;
    }
    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public int getWarranty() {
        return warranty;
    }
    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return String.format("%s, Product Type: Electronics, Product Brand: %s, Warranty Time: %d months",
                super.toString(), getProductBrand(), getWarranty());
    }
}
