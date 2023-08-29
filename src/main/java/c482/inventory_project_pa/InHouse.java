package c482.inventory_project_pa;

/**
 * Public class inherits parts from the abstract Part class. */
public class InHouse extends Part{

    private int machineId;
    /**
     * This constructor calls parameters from the superclass of the abstract Part class.
     * @param name The name of the part
     * @param price The price of the part
     * @param stock The inventory stock of a part
     * @param min The minimum amount of stock required for a part
     * @param max  The maximum amount of stock required for a part
     * @param machineId The ID for an in-house part*/
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     * The getter for the part's ID*/
    public int getMachineId() {
        return machineId;
    }
    /**
     * The setter for the part's ID*/
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}

