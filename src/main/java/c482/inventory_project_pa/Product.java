package c482.inventory_project_pa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Class for getters and setters of the product's parameters. */
public class Product {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private static int autoProductId = 10;

    /**
     * This constructor contains the parameters for the Product class.
     * @param name The name of the part
     * @param price The price of the part
     * @param stock The inventory stock of a part
     * @param min The minimum amount of stock required for a part
     * @param max  The maximum amount of stock required for a part
     * */
    public Product(String name, double price, int stock, int min, int max) {
        setId(autoProductId);
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        autoProductId += 20;
    }

    /**
     * The setter for the product ID. */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * The getter for the product ID. */
    public int getId() {

        return id;
    }

    /**
     * The setter for the product name. */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * The getter for the product name. */
    public String getName() {

        return name;
    }

    /**
     * The setter for the product price. */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * The getter for the product price. */
    public double getPrice() {

        return price;
    }

    /**
     * The setter for the product stock. */
    public void setStock(int stock) {

        this.stock = stock;
    }

    /**
     * The getter for the product stock. */
    public int getStock() {

        return stock;
    }

    /**
     * The setter for the product minimum inventory. */
    public void setMin(int min) {

        this.min = min;
    }

    /**
     * The getter for the product minimum inventory. */
    public int getMin() {

        return min;
    }

    /**
     * The setter for the product maximum inventory. */
    public void setMax(int max) {

        this.max = max;
    }

    /**
     * The getter for the product maximum inventory. */
    public int getMax() {

        return max;
    }

    /**
     * The method for adding the product's associated parts.
     * @param part passes in each part available for association. */
    public void addAssociatedPart(Part part) {

        associatedParts.add(part);
    }

    /**
     * The method for getting the product's associated parts. */
    public ObservableList<Part> getAllAssociatedParts() {

        return associatedParts;
    }

}
