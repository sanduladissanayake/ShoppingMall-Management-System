import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class UserGUI extends JFrame {
    private JTable productTable;
    private JTextArea detailTextArea;
    private JComboBox<String> selectProductType;
    private ArrayList<Product> productList;
    private ShoppingCart shoppingCart;
    private Cart shoppingCartWindow;
    private JButton addToCartButton;
    private DefaultTableModel tableModel;
    private WestminsterShoppingManager manager;
    private User currentUser;

    public UserGUI(ArrayList<Product> productList, WestminsterShoppingManager manager) {
        this.productList = productList;
        this.manager = manager;

        authenticateUser();    // Authenticate the user

        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "Authentication failed.");
            System.exit(0);
        }

        this.shoppingCart = new ShoppingCart(currentUser);  // Initialize the shopping cart and GUI components
        this.shoppingCartWindow = new Cart(this.shoppingCart);
        this.initGUI();
    }

    private void authenticateUser() {
        String[] options = {"Login", "Create New"};
        int choice;
        do {
            choice = JOptionPane.showOptionDialog(null, "Westminster Shopping Centre", "User Authentication",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) { // Login
                currentUser = loginUser();
            } else if (choice == 1) { // Register
                currentUser = registerUser();
            } else {
                System.exit(0);
            }
        } while (currentUser == null);
    }

    private User loginUser() {  //user login
        while (true) {
            String username = JOptionPane.showInputDialog("Username:");
            if (username == null) return null; // cancelled

            String password = JOptionPane.showInputDialog("Password:");
            if (password == null) return null; // cancelled

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty.");
                continue;
            }

            User user = manager.loginUser(username, password);
            if (user == null) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Invalid username or password.Try again?",
                        "Login Failed", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) {
                    return null;
                }
            } else {
                return user;
            }
        }
    }

    private User registerUser() {  //user registration method
        while (true) {
            String username = JOptionPane.showInputDialog("New username:");
            if (username == null) return null; // User cancelled

            String password = JOptionPane.showInputDialog("New password:");
            if (password == null) return null; // User cancelled

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty.");
                continue;
            }

            User newUser = manager.registerUser(username, password);
            if (newUser == null) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Already exists.Try again?",
                        "Registration Failed", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) {
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Successfully Registered");
                return newUser;
            }
        }
    }

    private void initGUI() {  // Method to initialize the GUI components
        setTitle("Westminster Shopping Centre " + currentUser.getUsername());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));   // Top panel with product category and cart button
        selectProductType = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        JButton cartButton = new JButton("Shopping Cart");
        cartButton.addActionListener(e -> shoppingCartWindow.setVisible(true));
        topPanel.add(new JLabel("Select Product Category: "));
        topPanel.add(selectProductType);
        topPanel.add(cartButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Category", "Price", "Info"}, 0) {   // products table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setDefaultRenderer(Object.class, new CustomCellRenderer());
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();  // Bottom panel with product details and add to cart button
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        detailTextArea = new JTextArea(10, 20);
        detailTextArea.setEditable(false);
        bottomPanel.add(new JScrollPane(detailTextArea));
        addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToCartButton.addActionListener(e -> addSelectedProductToCart());
        bottomPanel.add(addToCartButton);
        add(bottomPanel, BorderLayout.SOUTH);

        selectProductType.addActionListener(e -> updateProductTable((String) selectProductType.getSelectedItem())); // Update after category is selected
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                displayProductData(productTable.getSelectedRow());
            }
        });

        updateProductTable("All");
    }

    public void refreshDisplay() {
        SwingUtilities.invokeLater(() -> {
            updateProductTable((String) selectProductType.getSelectedItem());
        });
    }

    private void updateProductTable(String category) {
        tableModel.setRowCount(0);
        ArrayList<Product> filteredList = new ArrayList<>(productList);

        if (!category.equals("All")) {
            filteredList.removeIf(product ->
                    (category.equals("Electronics") && !(product instanceof Electronics)) ||
                            (category.equals("Clothing") && !(product instanceof Clothing)));
        }

        filteredList.sort(Comparator.comparing(Product::getProductId));

        for (Product product : filteredList) {
            String type = product instanceof Electronics ? "Electronics" : "Clothing";
            String info = product instanceof Electronics
                    ? "Brand: " + ((Electronics) product).getProductBrand()
                    : "Size: " + ((Clothing) product).getSize();
            tableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    type,
                    String.format("$%.2f", product.getProductPrice()),
                    info
            });
        }
    }

    private void displayProductData(int rowIndex) {    // Display details selected product
        String productId = (String) tableModel.getValueAt(rowIndex, 0);
        Product product = productList.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (product != null) {
            StringBuilder details = new StringBuilder();
            details.append("Product ID: ").append(product.getProductId()).append("\n");
            details.append("Name: ").append(product.getProductName()).append("\n");
            details.append("Category: ").append(product instanceof Electronics ? "Electronics" : "Clothing").append("\n");
            details.append("Price: $").append(String.format("%.2f", product.getProductPrice())).append("\n");
            details.append("Available Items: ").append(product.getNoOfAvailableItems()).append("\n");

            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                details.append("Brand: ").append(electronics.getProductBrand()).append("\n");
                details.append("Warranty Period: ").append(electronics.getWarranty()).append(" months\n");
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                details.append("Size: ").append(clothing.getSize()).append("\n");
                details.append("Color: ").append(colorToString(clothing.getColor())).append("\n");
            }

            detailTextArea.setText(details.toString());
        } else {
            detailTextArea.setText("Please select a product");
        }
    }

    private String colorToString(Color color) { //// Convert Color object to string
        if (Color.RED.equals(color)) return "red";
        if (Color.GREEN.equals(color)) return "green";
        if (Color.BLUE.equals(color)) return "blue";
        if (Color.YELLOW.equals(color)) return "yellow";
        if (Color.BLACK.equals(color)) return "black";
        if (Color.WHITE.equals(color)) return "white";
        if (Color.GRAY.equals(color)) return "gray";
        if (Color.CYAN.equals(color)) return "cyan";
        if (Color.MAGENTA.equals(color)) return "magenta";
        if (Color.ORANGE.equals(color)) return "orange";
        if (Color.PINK.equals(color)) return "pink";
        return "unknown";
    }

    private void addSelectedProductToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = (String) tableModel.getValueAt(selectedRow, 0);
            Product selectedProduct = productList.stream()
                    .filter(p -> p.getProductId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (selectedProduct != null) {
                shoppingCart.addProduct(selectedProduct);
                JOptionPane.showMessageDialog(this, "Successfully added to the Cart");
                shoppingCartWindow.refreshCart();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product.");
        }
    }

    private class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String productId = (String) tableModel.getValueAt(row, 0);
            Product product = productList.stream()
                    .filter(p -> p.getProductId().equals(productId))
                    .findFirst()
                    .orElse(null);
            if (product != null && product.getNoOfAvailableItems() < 3) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(table.getForeground());
            }
            return c;
        }
    }

    public void updateProductList(ArrayList<Product> newProductList) {
        this.productList = newProductList;
        refreshDisplay();
    }
}