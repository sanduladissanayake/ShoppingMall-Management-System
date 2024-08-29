import java.awt.*;
import java.io.Serializable;

public class Clothing extends Product implements Serializable {
    private static final long serialVersionUID = 1L; // Add a version UID
    private String size;
    private Color color;

    public Clothing(String productId, String productName, int noOfAvailableItems, double productPrice, String size, Color color) {
        super(productId, productName, noOfAvailableItems, productPrice);
        this.size = size;
        this.color = color;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    private String colorToString(Color color) {
        if (color.equals(Color.RED)) return "red";
        if (color.equals(Color.GREEN)) return "green";
        if (color.equals(Color.BLUE)) return "blue";
        if (color.equals(Color.YELLOW)) return "yellow";
        if (color.equals(Color.BLACK)) return "black";
        if (color.equals(Color.WHITE)) return "white";
        if (color.equals(Color.GRAY)) return "gray";
        if (color.equals(Color.CYAN)) return "cyan";
        if (color.equals(Color.MAGENTA)) return "magenta";
        if (color.equals(Color.ORANGE)) return "orange";
        if (color.equals(Color.PINK)) return "pink";
        return "unknown";
    }

    @Override
    public String toString() {
        return String.format("%s, Product Type: Clothing, Product Size: %s, Product Color: %s",
                super.toString(), getSize(), colorToString(getColor()));
    }
}
