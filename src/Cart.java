import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class Cart extends JFrame {
    private JTable cartTable;
    private ShoppingCart shoppingCart;
    private JLabel totalPriceLabel;

    public Cart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        setTitle("Shopping Cart - " + shoppingCart.getUser().getUsername());
        setSize(500, 400);
        setLayout(new BorderLayout());
        int padding = 10;
        getRootPane().setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        String[] columnNames = {"Product", "Quantity", "Price"};    // put the column names for the table
        DefaultTableModel cartTableModel = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(cartTableModel);   // Create the new table to display the cart items
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());   // bottom panel to display the prices
        totalPriceLabel = new JLabel("Total: $0.00");
        totalPriceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalPriceLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshCart();
        setVisible(false);
    }

    public void refreshCart() {
        updateCartDisplay();
        updateTotalPriceDisplay();
    }

    private void updateTotalPriceDisplay() {      // Update the total price label with current price details
        Map<String, Double> priceDetails = shoppingCart.totalCostCalc();
        double total = priceDetails.getOrDefault("total", 0.0);
        double discount = priceDetails.getOrDefault("discount", 0.0);
        boolean categoryDiscountApplied = priceDetails.getOrDefault("categoryDiscountApplied", 0.0) == 1.0;
        StringBuilder priceInfo = new StringBuilder();
        priceInfo.append("<html>Subtotal: $").append(String.format("%.2f", total + discount));
        if (discount > 0) {
            priceInfo.append("<br>Discount: -$").append(String.format("%.2f", discount));
            if (categoryDiscountApplied) {
                priceInfo.append(" (incl. 20% Category Discount)");
            }
            if (shoppingCart.getUser().gotFirstPurchaseDiscount()) {
                priceInfo.append("<br>First Purchase Discount (10%) Applied");
            }
        }
        priceInfo.append("<br><b>Final Total: $").append(String.format("%.2f", total)).append("</b>");
        priceInfo.append("</html>");
        totalPriceLabel.setText(priceInfo.toString());
    }

    private void updateCartDisplay() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0);
        for (Product product : shoppingCart.getProductsList()) {
            String productInfo = product.getProductId() + " - " + product.getProductName();
            int quantity = product.getNoOfAvailableItems();
            double price = product.getProductPrice() * quantity;
            model.addRow(new Object[]{productInfo, quantity, String.format("$%.2f", price)});
        }
    }
}