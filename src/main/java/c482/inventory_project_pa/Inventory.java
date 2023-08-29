package c482.inventory_project_pa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *This public class contains methods for tracking changes made to the parts and products tables.
 * */
public class Inventory {

    private static final ObservableList<Part> totalParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> totalProducts = FXCollections.observableArrayList();
    private static int lastPartID = 10;
    /**
     * This method is for confirming a part has been deleted and returns a boolean value.
     * @param selectedPart The part selected for deletion
     * @return Returns as true or false boolean. */
    public static boolean deletePart(Part selectedPart) {
        //if the part exists
        if(totalParts.contains(selectedPart)) {
            //remove it
            totalParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method confirms the deletion of a selected product and returns a boolean value.
     * @param confirmProduct The product selected for deletion
     * @return Returns as a true or false boolean. */
    public static boolean deleteProduct(Product confirmProduct) {

        return !totalProducts.contains(confirmProduct);
    }

    /**
     * getter for all parts available.
     * @return Returns all available parts */
    public static ObservableList<Part> getTotalParts() {

        return totalParts;
    }

    /**
     * a getter for all available products.
     * @return Returns all available products. */
    public static ObservableList<Product> getTotalProducts() {

        return totalProducts;
    }
    /**
     * A method for adding a part to the available inventory. */
    public static void addingNewPart(Part partAdd) {

        totalParts.add(partAdd);
    }

    /**
     * A method for adding a product to the available inventory. */
    public static void addingNewProduct(Product productAdd) {

        totalProducts.add(productAdd);
    }
    /**
     * This method allows the user to search for a part by its ID. */
    public static Part locateAvailablePart(int partId) {
        ObservableList<Part> allParts = getTotalParts();
        for (Part part : allParts) {
            if (partId == part.getId()) {
                return part;
            }
        }
        return null;
    }

    /**
     * The allows the user to search available parts by name to include part name queries. */
    public static ObservableList<Part> locateAvailablePart(String containsName) {
        ObservableList<Part> nameOfPart = FXCollections.observableArrayList();
        ObservableList<Part> totalPartsAvailable = getTotalParts();

        for (Part part : totalPartsAvailable) {
            if(part.getName().contains(containsName)) {
                nameOfPart.add(part);
            }
        }

        return nameOfPart;
    }

    /**
     * This method allows the user to search available products by its ID. */
    public static Product locateProductId(int id) {
        ObservableList<Product> allProductTables = getTotalProducts();
        for (Product productTable : allProductTables) {
            if (id == productTable.getId()) {
                return productTable;
            }
        }
        return null;
    }

    /**
     * This method allows the user to search available products by name to include partial name queries. */
    public static ObservableList<Product> locateProductId(String containsName) {
        ObservableList<Product> nameOfProduct = FXCollections.observableArrayList();
        ObservableList<Product> totalProductsAvailable = getTotalProducts();

        for (Product Product : totalProductsAvailable) {
            if(Product.getName().contains(containsName)) {
                nameOfProduct.add(Product);
            }
        }

        return nameOfProduct;
    }

    /**
     * This method creates a part id that increments by 5 for each part added to the parts table*/
    public static int createPartID() {
        lastPartID += 5;
        return lastPartID;
    }
    /**
     * This method is used to properly index the parts list to locate the correct part to be modified*/
    public static void updatePart(int id, Part selectedPart) {
        for (int i = 0; i < totalParts.size(); i++) {
            if (totalParts.get(i).getId() == id) {
                totalParts.set(i, selectedPart);
                break;
            }
        }
    }

}

