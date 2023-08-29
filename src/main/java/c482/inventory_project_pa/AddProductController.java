package c482.inventory_project_pa;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This public class AddProduct Controller contains all the methods for loading the AddProduct screen for the user to interact with and
 * performing data validation checks for the values a user enters. */

public class AddProductController implements Initializable {

    @FXML private TableColumn<Part, Double> colAssocParts, colPricePartProd;
    @FXML private TableColumn<Part, Integer> colAddAssocPartsId, colAddPartProdId;
    @FXML private TableView<Part> addTableProdParts, addTableAssocProdParts;
    @FXML private TableColumn<Part, String> colAddAssocName, colAddProdPartName;
    @FXML private TextField searchParts;
    @FXML private TextField productNameField, productInventoryField, productPriceField, productMaxField, productMinField;
    @FXML private TableColumn<Part, Integer> colAddAssocPartsInventory, colAddProdPartInventory;
    /**
     * This method loads the add part screen with the parts table data to associate any available parts for a product.
     * @param url URL standard
     * @param resourceBundle resourceBundle standard*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTableProdParts.setItems(Inventory.getTotalParts());
        colAddPartProdId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colAddProdPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddProdPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPricePartProd.setCellValueFactory(new PropertyValueFactory<>("price"));

        colAddAssocPartsId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colAddAssocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddAssocPartsInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colAssocParts.setCellValueFactory(new PropertyValueFactory<>("price"));

        colAddPartProdId.setSortType(TableColumn.SortType.ASCENDING);
        addTableProdParts.getSortOrder().add(colAddPartProdId);

        colAddAssocPartsId.setSortType(TableColumn.SortType.ASCENDING);
        addTableAssocProdParts.getSortOrder().add(colAddAssocPartsId);

        addTableProdParts.sort();
        addTableAssocProdParts.sort();

        setupNumericTextFormatter(productMinField);
        setupNumericTextFormatter(productPriceField);
        setupNumericTextFormatter(productMaxField);
        setupNumericTextFormatter(productInventoryField);
        setupStringTextFormatter(productNameField);
    }
    /**
     * This method sets up validation checks for any strings entered on the add product form. It allows upper and lower case letters.
     * The method also allows white space between words a user may input for a product name. */
    private void setupStringTextFormatter(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]+")) {
                return change;
            }
            return null;
        });
        textField.setTextFormatter(formatter);
    }
    /**
     * This method sets up validation checks for any numerical values entered on the add product form.
     * It will allow only numeric values that can be a whole number or one with a decimal place such as product price. */
    private void setupNumericTextFormatter(TextField textField) {
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        });
        textField.setTextFormatter(formatter);
    }
    /**
     * This method is used as a template for error messages that are triggered by the adding a product or associating/disassociating a part to a product.
     * @param content description of error message.
     * @param header header of error message.
     * @param title Title of error message.
     * */
    private void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This method is triggered when the user selects the Save button on the AddProduct screen. This method checks that the inventory value is between the enter min and max.
     * This method also checks that min value is less than max value.
     * After conditions are checked, the user is returned to the Main_View screen.
     * @param event handles the action from the user selecting the Save button.
     * @throws IOException An exception is thrown outside the try/catch conditions.
     * */
    public void Save_Adding_Product(ActionEvent event) throws IOException {
        Product productTable;

        try {
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());
            int inventory = Integer.parseInt(productInventoryField.getText());

            if (min > max) {
                showWarning("Error Message:", "Product cannot be added.",
                        "Minimum value must be less than the Maximum value.");
                return;

            } else if (!(min < inventory) || !(inventory < max)) {
                showWarning("Error Message:", "Cannot add Product to Table.",
                        "Please ensure that the inventory value is between the maximum and minimum values.");
                return;

            } else {
                productTable = new Product(
                        productNameField.getText(),
                        Double.parseDouble(productPriceField.getText()),
                        inventory,
                        min,
                        max
                );
                Inventory.addingNewProduct(productTable);
            }
        } catch (NumberFormatException ignore) {
            showWarning("Error:", "Cannot add the Product to Table.",
                    "Please validate the values entered are correct.");
            return;
        }

        for (Part part : addTableAssocProdParts.getItems()) {
            productTable.addAssociatedPart(part);
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    /**
     * This method returns the user to the Main_View screen when the Cancel button is clicked.
     * @param event Handles the action for when the Save button is clicked.
     * */
    public void cancel_Product_Change(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    /**
     * This method is used when the user searches for a product by name or ID.
     * */
    public void getPartSearchResults() {
        String search = searchParts.getText();
        ObservableList<Part> parts = Inventory.locateAvailablePart(search);

        if (parts.size() == 0) {
            try {
                int Id = Integer.parseInt(search);
                Part part = Inventory.locateAvailablePart(Id);
                if (part != null) {
                    parts.add(part);
                }
            } catch (NumberFormatException ignored) {}
        }

        addTableProdParts.setItems(parts);
    }

    /**
     * This method is for adding the associated parts selected to a product.
     * */
    public void addAssocParts() {
        Part selectedPart = addTableProdParts.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            showWarning("Error:", "No item was selected.",
                    "Select a part to add and associate to this product.");
            return;
        }

        addTableAssocProdParts.getItems().add(selectedPart);
    }

    /**
     * This method is for removing the associated parts listed for a product.
     * Once an associated part is selected for deletion, a confirmation message is sent to the user to confirm part deletion. */
    public void removeAssocParts() {
        Part selectedPart = addTableAssocProdParts.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            showWarning("Error:", "No item was selected.",
                    "Select a part to remove.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove?");
        alert.setHeaderText("Removing " + selectedPart.getName());
        alert.setContentText("Are you sure you want to remove this part from the product?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            addTableAssocProdParts.getItems().remove(selectedPart);
        }
    }

}
