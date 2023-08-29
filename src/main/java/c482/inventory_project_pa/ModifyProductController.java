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

/**<p>
 * RUNTIME ERROR:
 * When testing the product fields, I encountered a problem. If I changed a product name to contain only numbers and clicked the Save button,
 * the program would throw a runtime error because it couldn't handle invalid data.
 * To address this issue, I implemented formatters to validate the data entered into the fields, ensuring that only appropriate data types
 * are allowed. For example, I prevented users from entering letter values into a price field. Additionally, I set up an event handler to
 * handle exceptions in case such invalid data is entered.
 *</p>
 * Class controller for initializing the available products with associated parts data when the ModifyProduct screen loads.
 * */
public class ModifyProductController implements Initializable {

    @FXML private TableColumn<Part, Integer> partIdColumn, associatedPartIdColumn, partInventoryColumn, associatedPartInventoryColumn;
    @FXML private TextField partSearchField, productNameField, productInventoryField, productPriceField, productMinField, productMaxField;

    @FXML private TableColumn<Part, Double> associatedPartsPriceColumn, partPriceColumn;
    @FXML private TableColumn<Part, String> associatedPartsNameColumn, partNameColumn;
    @FXML private TableView<Part> associatedPartsTable, availableProductsTable;

    private Product productToModify;

    /**
     * Initialization method to load selected product data and starts the string and numeric data validation checks. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productToModify = MainController.getChangeTheProduct();
        ObservableList<Part> associatedParts = productToModify.getAllAssociatedParts();

        setupAvailablePartsTable();
        setupAssociatedPartsTable();

        setProductFields(productToModify);
        loadAssociatedParts(associatedParts);

        sortTableViews();
        setupNumericTextFormatter(productPriceField);
        setupNumericTextFormatter(productMinField);
        setupNumericTextFormatter(productMaxField);
        setupNumericTextFormatter(productInventoryField);
        setupStringTextFormatter(productNameField);
    }

    /**
     * This method sets up validation checks for any strings entered on the ModifyProduct screen. It allows upper and lower case letters.
     * The method also allows white space between words a user may input for a product name. */
    private void setupStringTextFormatter(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        });
        textField.setTextFormatter(formatter);
    }
    /**
     * This method sets up validation checks for any numerical values entered on the ModifyProduct screen.
     * It will allow only numeric values that can be a whole number or one with a decimal place such as product price. */
    private void setupNumericTextFormatter(TextField textField) {
        TextFormatter<Double> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        });
        textField.setTextFormatter(formatter);
    }
    /**
     * This method is used as a template for setting the fields for an associated part. */
    private void setupAvailablePartsTable() {
        availableProductsTable.setItems(Inventory.getTotalParts());
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        availableProductsTable.getSortOrder().add(associatedPartIdColumn);
        availableProductsTable.sort();
    }

    /**
     * This method is used as a template for setting the table data for available parts. */
    private void setupAssociatedPartsTable() {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        associatedPartsTable.getSortOrder().add(partIdColumn);
        associatedPartsTable.sort();
    }

    /**
     * This method is used as a template for setting the table data for an existing product. */
    private void setProductFields(Product selectedProduct) {
        productNameField.setText(selectedProduct.getName());
        productInventoryField.setText(String.valueOf(selectedProduct.getStock()));
        productPriceField.setText(String.valueOf(selectedProduct.getPrice()));
        productMinField.setText(String.valueOf(selectedProduct.getMin()));
        productMaxField.setText(String.valueOf(selectedProduct.getMax()));
    }

    /**
     * This method is used as a template for loading the associated parts for a product. */
    private void loadAssociatedParts(ObservableList<Part> associatedParts) {
        associatedPartsTable.getItems().addAll(associatedParts);
    }

    /**
     * This method is used as a template for sorting the associated parts and products tables. */
    private void sortTableViews() {
        availableProductsTable.sort();
        associatedPartsTable.sort();
    }
    /**
     * This method is used for searching an existing product by ID. */
    public void getPartSearchResults() {
        String search = partSearchField.getText();
        ObservableList<Part> parts = Inventory.locateAvailablePart(search);

        if (parts.isEmpty()) {
            try {
                int partId = Integer.parseInt(search);
                Part part = Inventory.locateAvailablePart(partId);

                if (part != null) {
                    parts.add(part);
                }
            } catch (NumberFormatException ignored) {

            }
        }

        availableProductsTable.setItems(parts);
    }
    /**
     * This method checks that the inventory value is between the enter min and max.
     * This method also checks that min value is less than max value.
     * The conditions are checked as the user updates the fields.
     * */
    public boolean saveProductChanges(ActionEvent event) throws IOException {
        try {
            int min = Integer.parseInt(productMinField.getText());
            int max = Integer.parseInt(productMaxField.getText());
            int inventory = Integer.parseInt(productInventoryField.getText());

            if (min > max) {
                showWarning("Error: Product selected cannot be modified.",
                        "Minimum value must be less than Maximum value!");
                return false;

            } else if (inventory < min || inventory > max) {
                showWarning("Error: Product selected cannot be modified.",
                        "Please ensure that the inventory value is greater than or equal to the minimum value and less than or equal to the maximum value.");
                return false;

            } else {
                productToModify.setName(productNameField.getText());
                productToModify.setPrice(Double.parseDouble(productPriceField.getText()));
                productToModify.setStock(inventory);
                productToModify.setMin(min);
                productToModify.setMax(max);
                productToModify.getAllAssociatedParts().clear();
                for (Part part : associatedPartsTable.getItems()) {
                    productToModify.addAssociatedPart(part);
                }
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                return true;
            }
        } catch (NumberFormatException ignored) {
            showWarning("Error: Product selected cannot be modified",
                    "Verify the values entered are correct.");
            setProductFields(productToModify);
            return false;
        }

    }

    /**
     * This method is used as a template for error messages. */
    private void showWarning(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    /**
     * This method is used to handle the event when the user clicks on the cancel button on the ModifyProduct screen and returns the user to the Main_View screen.
     * */
    public void cancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method is used to allow a user to add a part to a product and throws an error when a part has not been selected.
     * */
    public void addAssociatedPartButton() {
        Part selectedPart = availableProductsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            showWarning("Error: No part was selected!",
                    "Select a part from table to associate to this product.");
            return;
        }

        associatedPartsTable.getItems().add(selectedPart);
    }

    /**
     * This method is used to allow a user to remove an associated part from a product and prompts user to confirm prior to deleting an associated part from a product.
     * Error is thrown is no associated part is selected.
     * */
    public void removeAssociatedPartButton() {
        Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            showWarning("Error: No part was selected!",
                    "Select a part from table to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm");
        alert.setHeaderText("Removing " + selectedPart.getName());
        alert.setContentText("Do you want to delete the selected part from the product?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            associatedPartsTable.getItems().remove(selectedPart);
        }
    }

}
