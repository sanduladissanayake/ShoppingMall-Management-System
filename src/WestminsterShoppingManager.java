import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class WestminsterShoppingManager implements ShoppingManager {
    private static Scanner obj;
    private static ArrayList<Product> productsList;
    private List<User> users;
    private UserGUI gui;

    public WestminsterShoppingManager() {
        productsList = new ArrayList<>();
        users = new ArrayList<>();
        obj = new Scanner(System.in);
    }

    public void options() {    // Main loop for the menu selection
        int selection;
        do {
            selection = this.displayMenu();
            switch (selection) {
                case 1:
                    this.addProduct();
                    break;
                case 2:
                    this.deleteProduct();
                    break;
                case 3:
                    this.printProductsList();
                    break;
                case 4:
                    this.saveToFile();
                    break;
                case 5:
                    this.loadFromFile();
                    break;
                case 6:
                    this.launchGUI();
                    break;
                case 7:
                    System.out.println("Have a good day!");
                    break;
                default:
                    System.out.println("Invalid Option!");
            }
        } while (selection != 7);
    }


    public static void main(String[] args) {
        WestminsterShoppingManager manager = new WestminsterShoppingManager();
        manager.options();
    }

    private void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            gui = new UserGUI(productsList, this);
            gui.setVisible(true);
        });
    }

    // Display the menu options to the user
    public int displayMenu() {
        while (true) {
            try {
                System.out.println("\n***************************************************");
                System.out.println("\n   Westminster Shopping Manager   ");
                System.out.println("   1. Add product");
                System.out.println("   2. Delete product");
                System.out.println("   3. Print list of products");
                System.out.println("   4. Save to file");
                System.out.println("   5. Load from file");
                System.out.println("   6. Open GUI");
                System.out.println("   7. Exit");
                System.out.println("\n***************************************************");
                System.out.print("Enter your option: ");
                int option = Integer.parseInt(obj.nextLine());
                return option;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Method to add a product to the list
    public void addProduct() {
        try {
            if (productsList.size() >= 50) {     // Check if product list has reached its limit
                System.out.println("Cannot add more products. Limit reached");
                return;
            }

            int productType;
            while (true) {
                System.out.println("Add Electronics : Enter 1");
                System.out.println("Add Clothing : Enter 2");
                try {
                    productType = Integer.parseInt(obj.nextLine());
                    if (productType != 1 && productType != 2) {
                        System.out.println("Invalid input. try again");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. enter number.");
                }
            }

            System.out.println("Enter product ID:");        //ID
            String productID = obj.nextLine();
            if (productID.isEmpty() || isProductIdValid(productID)) {
                throw new IllegalArgumentException("Invalid/existing product ID.");
            }

            System.out.println("Enter product name:");      //product name
            String productName = obj.nextLine();
            if (!isNameValid(productName)) {
                throw new IllegalArgumentException("Invalid name.");
            }

            int availableItems;
            while (true) {
                System.out.println("Enter number items available:");   //#available items
                try {
                    availableItems = Integer.parseInt(obj.nextLine());
                    if (availableItems < 0) {
                        System.out.println("Invalid input.Enter a non-negative number.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            double price;
            while (true) {
                System.out.println("Enter the price:");   //price
                try {
                    price = Double.parseDouble(obj.nextLine());
                    if (price < 0) {
                        System.out.println("Invalid input.Enter a non-negative number.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Try again.");
                }
            }

            Product product;
            if (productType == 1) {
                System.out.println("Enter the brand:");      //brand name
                String brand = obj.nextLine();
                if (!isNameValid(brand)) {
                    throw new IllegalArgumentException("Invalid brand name.");
                }

                int warrantyPeriod;
                while (true) {
                    System.out.println("Enter the warranty period in months :");     //warranty period
                    try {
                        warrantyPeriod = Integer.parseInt(obj.nextLine());
                        if (warrantyPeriod < 0) {
                            System.out.println("Invalid input.Enter a non-negative number.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }

                product = new Electronics(productID, productName, availableItems, price, brand, warrantyPeriod);
            } else {
                System.out.println("Enter the size:");   //size
                String size = obj.nextLine();

                Color color;
                while (true) {
                    System.out.println("Enter the color red, green, blue, yellow, black, white, gray, cyan, magenta, orange or pink:");
                    String colorInput = obj.nextLine().toLowerCase();
                    color = stringToColor(colorInput);
                    if (color == null) {
                        System.out.println("Invalid color. Try again.");
                    } else {
                        break;
                    }
                }

                product = new Clothing(productID, productName, availableItems, price, size, color);
            }

            productsList.add(product);
            System.out.println("Product added successfully.");
            if (gui != null) {
                gui.refreshDisplay();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    public void deleteProduct() {     // remove a product from the list method
        try {
            System.out.println("Enter the product ID to delete:");
            String productID = obj.nextLine().trim();

            if (productID.isEmpty()) {
                System.out.println("Product ID cannot be empty.Try again.");
                return;
            }

            Iterator<Product> iterator = productsList.iterator();
            boolean productFound = false;

            while (iterator.hasNext()) {   // Iterate until find and remove the product
                Product product = iterator.next();
                if (product.getProductId().equals(productID)) {
                    iterator.remove();
                    productFound = true;
                    System.out.println("Successfully deleted.");
                    break;
                }
            }

            if (productFound) {
                // show the total number of products left
                System.out.println("Number of products left: " + productsList.size());
                if (gui != null) {
                    gui.refreshDisplay();
                }
            } else {
                System.out.println("Product ID " + productID + " not found.");
            }

        } catch (Exception e) {
            System.out.println("Invalid data format. Please enter a valid product ID.");
        }
    }

    @Override
    public void printProductsList() {
        try {
            if (productsList != null && !productsList.isEmpty()) {
                System.out.println("\nProduct list:");

                // creating new list and sorting part
                ArrayList<Product> sortedList = new ArrayList<>(productsList);
                sortedList.sort(Comparator.comparing(Product::getProductId));

                // display the sorted list
                for (Product product : sortedList) {
                    System.out.println(product.toString());
                }

                // Display the total number of products
                System.out.println("Total number of products: " + sortedList.size());
            } else {
                System.out.println("Product list is empty.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public User registerUser(String username, String password) {   // method to register a new user if the username is not already taken
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return null;
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        return newUser;
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {  // validate username and password
                return user;
            }
        }
        return null;
    }

    @Override
    public void saveToFile() {  // method to save product details in a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("WestminsterShopData.ser"))) {
            oos.writeObject(productsList);
            oos.writeObject(users);
            System.out.println("Successfully Saved to the file.");
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile() {  // method to load product details from the file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("WestminsterShopData.ser"))) {
            productsList = (ArrayList<Product>) ois.readObject();
            users = (List<User>) ois.readObject();
            System.out.println("Successfully loaded from file.");
            if (gui != null) {
                gui.refreshDisplay();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }

    private boolean isProductIdValid(String productID) {
        return productsList.stream().anyMatch(p -> p.getProductId().equals(productID));
    }

    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty() && !name.matches(".*\\d.*");
    }

    private Color stringToColor(String colorStr) {  //to get the colour from color class
        switch (colorStr) {
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "blue": return Color.BLUE;
            case "yellow": return Color.YELLOW;
            case "black": return Color.BLACK;
            case "white": return Color.WHITE;
            case "gray": return Color.GRAY;
            case "cyan": return Color.CYAN;
            case "magenta": return Color.MAGENTA;
            case "orange": return Color.ORANGE;
            case "pink": return Color.PINK;
            default: return null;
        }
    }


    public ArrayList<Product> getProductsList() {
        return new ArrayList<>(productsList);
    }
}