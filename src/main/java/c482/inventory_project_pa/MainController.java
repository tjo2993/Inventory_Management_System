package c482.inventory_project_pa;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * This class is the controller to initialize the main screen with the available parts and products tables. */
public class MainController implements Initializable {

    private static Part changeThePart = null;
    private static Product changeTheProductTable = null;


    @FXML private TextField searchParts;
    @FXML private TextField searchProducts;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, Integer> colProductId;
    @FXML private TableColumn<Product, Double> colProductPrice;
    @FXML private TableColumn<Product, Integer> colProductInventory;
    @FXML private TableView<Product> tableViewProducts;
    @FXML private TableView<Part> tableViewParts;
    @FXML private TableColumn<Part, Double> colPartsPrice;
    @FXML private TableColumn<Part, Integer> colPartId;
    @FXML private TableColumn<Part, String> colPartName;
    @FXML private TableColumn<Part, Integer> colPartsInventory;
    /**
     * This method initializes the data shown in the parts and products tables. */
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

        tableViewParts.sort();
        tableViewProducts.sort();
        tableViewParts.sort();
        tableViewProducts.sort();
        tableViewParts.setItems(Inventory.getTotalParts());
        colPartId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPartId.setSortType(TableColumn.SortType.ASCENDING);
        tableViewParts.getSortOrder().add(colPartId);
        colProductId.setSortType(TableColumn.SortType.ASCENDING);
        tableViewProducts.getSortOrder().add(colProductId);
        tableViewProducts.setItems(Inventory.getTotalProducts());
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**
     * This method is used as a template for error messages*/
    private void showWarning(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Method for getting the part to be changed. */
    public static Part getChangeThePart() {

        return changeThePart;
    }
    /**
     * Method for getting the product to be changed. */
    public static Product getChangeTheProduct() {

        return changeTheProductTable;
    }

    /**
     * Method for loading the AddProduct screen. */
    public void addingProductScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddProductView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Method for loading the AddPart screen. */
    public void addPartScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AddPartView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    /**
     * Method for changing an existing part entry and loading the ModifyPart screen.*/
    public void makePartChange(ActionEvent event) throws IOException {
        Part partChosen = tableViewParts.getSelectionModel().getSelectedItem();

        if (partChosen == null) {
            showWarning("Error: No part was selected.",
                    "To modify a part, a selection must be made.");
            return;
        }
        changeThePart = partChosen;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyPartView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Method used to change an existing product entry and loads the ModifyProduct screen. */
    public void makeProductChange(ActionEvent event) throws IOException {
        Product productTableChosen = tableViewProducts.getSelectionModel().getSelectedItem();

        if (productTableChosen == null) {
            showWarning("No product was selected.",
                    "To modify a product, a selection must be made.");
            return;
        }
        changeTheProductTable = productTableChosen;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModifyProductView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Method for deleting an existing product entry and checks products selected for deletion that there are no parts associated first.
     * This method will not allow a user to delete an existing product that has parts associated to it.
     * After a product is allowed to proceed with deletion, the system prompts the user to confirm they want to delete the product.*/
    public void deleteAProduct() {
        Product productTableChosen = tableViewProducts.getSelectionModel().getSelectedItem();

        if (productTableChosen == null) {
            showWarning("No product was selected.",
                    "To modify a product, a selection must be made.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm");
        alert.setHeaderText("Deleting " + productTableChosen.getName());
        alert.setContentText("Do you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (productTableChosen.getAllAssociatedParts().isEmpty()) {
                Inventory.getTotalProducts().remove(productTableChosen);

            } else {
                showWarning("Product selected has associated parts!",
                        "Remove associated parts before deleting the product.");
                return;
            }
        }

        if (Inventory.deleteProduct(productTableChosen)) {
            Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
            confirmed.setTitle("Product has been deleted.");
            confirmed.setContentText("Selected product has been deleted successfully.");
            confirmed.showAndWait();
        }
    }

    /**
     * Method for deleting a part and handles exceptions for when a part is not selected.*/
    public void deleteAPart() {
        Part partChosen = tableViewParts.getSelectionModel().getSelectedItem();

        if (partChosen == null) {
            showWarning("Error: No part was selected.",
                    "To delete a part, a selection must be made.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm");
        alert.setHeaderText("Deleting " + partChosen.getName());
        alert.setContentText("Do you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.getTotalParts().remove(partChosen);
        }

        if (Inventory.deletePart(partChosen)) {
            Alert confirmed = new Alert(Alert.AlertType.INFORMATION);
            confirmed.setTitle("Part has been deleted");
            confirmed.setContentText("Selected part has been deleted successfully.");
            confirmed.showAndWait();
        }
    }

    /**
     * Method for locating an existing part entry. */
    public void getSearchPartData() {
        String search = searchParts.getText();
        ObservableList<Part> parts = FXCollections.observableArrayList();
        ObservableList<Part> foundParts = Inventory.locateAvailablePart(search);

        boolean isPartId = true;
        try {
            int partId = Integer.parseInt(search);
            parts.add(Inventory.locateAvailablePart(partId));

        } catch (NumberFormatException ignored) {
            isPartId = false;
        }
        if (!isPartId) {
            parts.addAll(foundParts);
        }
        tableViewParts.setItems(parts);
    }
    /**
     * Method used to locate an existing product entry. */
    public void getSearchProductData() {
        String search = searchProducts.getText();
        ObservableList<Product> productTables = Inventory.locateProductId(search);

        if (productTables.size() == 0) {
            try {
                int id = Integer.parseInt(search);
                Product productTable = Inventory.locateProductId(id);
                if (productTable != null) {
                    productTables.add(productTable);
                }
            } catch (NumberFormatException ignored) {}
        }

        tableViewProducts.setItems(productTables);
    }

    /**
     * Method used to exit the program application and requires the user to confirm prior to closing the application. */
    public void closeProgram() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }

}