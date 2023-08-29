package c482.inventory_project_pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The Add Part Controller class controls the AddPartView form.
 * The class also contains methods to handle data entry as well as storing new parts into the parts table and associated parts table for the products. */

public class AddPartController {


    @FXML
    private RadioButton inHousePart, outsourcedPart;
    @FXML
    public ToggleGroup partType;
    @FXML
    private Label partLabel;
    @FXML
    private TextField nameField, inventoryPartText, partPriceField, partMaxField, partMinField, partTypeField;

    /**
     * This is a method for getting the values for "Company Name" for an outsourced part or "Machine ID" if the part is in-house.
     */
    public void getPartType() {
        if (inHousePart.isSelected()) {
            partLabel.setText("Machine ID");
        } else if (outsourcedPart.isSelected()) {
            partLabel.setText("Company Name");
        }
    }

    /**
     * This method provides a template for each error thrown for Save_Adding_Part method.
     */
    private void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * This method loads the main screen when called.
     */
    private void Main_Screen_View(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method returns to the Main_View screen when the user clicks Cancel on the AddPart screen.
     *
     * @param event Associated with the cancel button on the AddPart screen.
     */
    public void Cancel_Adding_Part(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method is used for validating the values entered on the AddPart screen.
     * The part price is checked to ensure it is a number and not a string.
     * The inventory value is checked to ensure it is less than the max and more than the min.
     * The part name is checked to ensure it is a string text value and not numbers.
     *
     * @param event Used to trigger this method when the Save button is clicked on the AddPart screen.
     */
    public void Save_Adding_Part(ActionEvent event) throws IOException {
        String partTypeLabelText = partLabel.getText();
        String name = nameField.getText();
        double price;
        int inventory, min, max;
        String typeStr = partTypeField.getText();

        if (nameField.getText().isEmpty()) {
            showWarning("Error Message:", "Part name cannot be empty.",
                    "Please enter a name for the part.");
            return;
        }

        if (name.matches(".*\\d.*")) {
            showWarning("Error Message:", "Part name cannot be a number.",
                    "Please enter a valid name for the part.");
            return;
        }
        if (!partPriceField.getText().matches("\\d*\\.?\\d*")) {
            showWarning("Error Message:", "Price must be a number.",
                    "Please enter a valid decimal number for the price.");
            return;
        }

        if (!inventoryPartText.getText().matches("\\d+")) {
            showWarning("Error Message:", "Inventory must be a number.",
                    "Please enter a valid whole number for the inventory.");
            return;
        }

        if (!partMinField.getText().matches("\\d+")) {
            showWarning("Error Message:", "Minimum stock must be a number.",
                    "Please enter a valid whole number for the minimum stock.");
            return;
        }

        if (!partMaxField.getText().matches("\\d+")) {
            showWarning("Error Message:", "Maximum stock must be a number.",
                    "Please enter a valid whole number for the maximum stock.");
            return;
        }
        try {
            int id = Inventory.createPartID();
            price = Double.parseDouble(partPriceField.getText());
            inventory = Integer.parseInt(inventoryPartText.getText());
            min = Integer.parseInt(partMinField.getText());
            max = Integer.parseInt(partMaxField.getText());

            if (max < min) {
                showWarning("Error Message:", "Part cannot be changed.",
                        "Maximum value must be greater than the Minimum value.");

            }

            if (min > max) {
                showWarning("Error Message:", "Part cannot be changed.",
                        "Minimum value must be less than the Maximum value.");
                return;
            }

            if (inventory < min || inventory > max) {
                showWarning("Error Message", "Part cannot be added.",
                        "Please ensure that the inventory value is greater than or equal to the minimum value and less than or equal to the maximum value.");
                return;
            }

            if (partTypeLabelText.equals("Machine ID")) {
                if (typeStr.isBlank()) {
                    showWarning("Error Message", "Part cannot be added.",
                            "Machine ID cannot be empty.");
                    return;
                }

                if (!typeStr.matches("\\d+")) {
                    showWarning("Error Message", "Part cannot be added.",
                            "Machine ID must be a positive number and cannot contain letters.");
                    return;
                }

                int type = Integer.parseInt(typeStr);
                InHouse part = new InHouse(id, name, price, inventory, min, max, type);
                Inventory.addingNewPart(part);

            } else if (partTypeLabelText.equals("Company Name")) {
                if (typeStr.isBlank()) {
                    showWarning("Error Message", "Part cannot be added.",
                            "Company Name field cannot be empty.");
                    return;
                }

                if (typeStr.matches(".*\\d.*")) {
                    showWarning("Error Message", "Part cannot be added.",
                            "Company Name cannot be a number.");
                    return;
                }
                Outsourced part = new Outsourced(id, name, price, inventory, min, max, typeStr);
                Inventory.addingNewPart(part);
            }
        } catch (NumberFormatException ignored) {
            showWarning("Error Message", "Cannot add part.",
                    "Please validate the values entered are correct.");
            return;
        }

        Main_Screen_View(event);
    }
}
