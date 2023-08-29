package c482.inventory_project_pa;

/**
 * Public class inherits parts from the abstract Part class. */
public class Outsourced extends Part {

    private String companyName;

    /**
     * This constructor calls parameters from the superclass of the abstract Part class.
     * @param name The name of the part
     * @param price The price of the part
     * @param stock The inventory stock of a part
     * @param min The minimum amount of stock required for a part
     * @param max  The maximum amount of stock required for a part
     * @param companyName The company for an outsourced part*/

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id ,name, price, stock, min, max);
       this.companyName = companyName;
    }
    /**
     * The getter for the company name. */
    public String getCompanyName() {

        return companyName;
    }

    /**
     * The setter for the company name. */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }
}
