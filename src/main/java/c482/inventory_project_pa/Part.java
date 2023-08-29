package c482.inventory_project_pa;

/**
 * @author Troy Jones
 * Student ID: 010761648
 * Date: 08/03/23
 * <p>
 * FUTURE ENHANCEMENTS:
 * If I were to suggest a future improvement for this application, it would be to use a SQL database extension for storing and processing the data.
 * SQL contains better options for validating data and can be configured to not allow duplicate entries into the database.
 * There also needs to be features that could be added to where only certain parts would be available to add to a product.
 * These suggestions would provide greater optimization and scalability of this application.
 * </p>
 *<p>
 *     Using provided Part class file below per project requirements.
 *</p>
 * */


public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;


    /**
     * This constructor calls parameters from the superclass of the abstract Part class.
     * @param name The name of the part
     * @param price The price of the part
     * @param stock The inventory stock of a part
     * @param min The minimum amount of stock required for a part
     * @param max  The maximum amount of stock required for a part
     * */

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * The getter for the part ID. */
    public int getId() {

        return id;
    }

    /**
     * The setter for the part ID. */
    public void setId(int partId) {

        this.id = partId;
    }

    /**
     * The getter for the part name. */
    public String getName() {

        return name;
    }

    /**
     * The setter for the part name. */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * The getter for the part price. */
    public double getPrice() {

        return price;
    }

    /**
     * The setter for the part price. */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * The getter for the part stock amount. */
    public int getStock() {

        return stock;
    }

    /**
     * The setter for the part stock amount. */
    public void setStock(int stock) {

        this.stock = stock;
    }

    /**
     * The getter for the minimum part inventory. */
    public int getMin() {

        return min;
    }

    /**
     * The setter for the minimum part inventory. */
    public void setMin(int min) {

        this.min = min;
    }

    /**
     * The getter for the maximum part inventory. */
    public int getMax() {

        return max;
    }

    /**
     * The setter for the maximum part inventory. */
    public void setMax(int max) {

        this.max = max;
    }

}
