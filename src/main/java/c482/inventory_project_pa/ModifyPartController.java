package c482.inventory_project_pa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class controller for initializing the available parts data when the ModifyPart screen loads. */
public class ModifyPartController implements Initializable {

    @FXML private RadioButton selectInHouse, selectOutsourced;
    @FXML private ToggleGroup InHouse_Or_Outsourced;
    @FXML private Label labelPart;
    @FXML private TextField fieldPartName, fieldPartType, fieldPartInventory, fieldPartPrice, fieldPartMin, fieldPartMax;

    private Part selectedPart;
    /**
     * Initialization method to allow the user to toggle between in-house or outsourced and display the appropriate ID or company name to the user. */
    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedPart = MainController.getChangeThePart();

        if (selectedPart instanceof Outsourced) {
            InHouse_Or_Outsourced.selectToggle(selectOutsourced);
            togglePartType();
            fieldPartType.setText(((Outsourced) selectedPart).getCompanyName());
        } else if (selectedPart instanceof InHouse){
            InHouse_Or_Outsourced.selectToggle(selectInHouse);
            togglePartType();
            fieldPartType.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        setFields();
        setupNumericTextFormatter(fieldPartPrice);
        setupNumericTextFormatter(fieldPartMin);
        setupNumericTextFormatter(fieldPartMax);
        setupNumericTextFormatter(fieldPartInventory);
        setupStringTextFormatter(fieldPartName);

    }

    /**
     * This method sets up validation checks for any strings entered on the ModifyPart screen. It allows upper and lower case letters.
     * The method also allows white space between words a user may input for a part name. */
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
     * This method sets up validation checks for any numerical values entered on the ModifyPart screen.
     * It will allow only numeric values that can be a whole number or one with a decimal place such as part price. */
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
     * This method is used as a template for error messages. */
    private void showWarning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This is a method for getting the values for "Company Name" for an outsourced part or "Machine ID" if the part is in-house.
     * */
    public void togglePartType() {
        if (selectInHouse.isSelected()) {
            labelPart.setText("Machine ID");
        } else if (selectOutsourced.isSelected()) {
            labelPart.setText("Company Name");
        }
    }
    /**
     * This method is used as a template for setting the fields for an existing part. */
    private void setFields() {
        fieldPartName.setText(selectedPart.getName());
        fieldPartInventory.setText(String.valueOf(selectedPart.getStock()));
        fieldPartPrice.setText(String.valueOf(selectedPart.getPrice()));
        fieldPartMin.setText(String.valueOf(selectedPart.getMin()));
        fieldPartMax.setText(String.valueOf(selectedPart.getMax()));
    }

    /**
     * This is a method for saving changes to the existing part and throws an exception if fields are not filled out correctly.
     * */
    public void savePartChange(ActionEvent event) throws IOException {
        String name = fieldPartName.getText().trim();
        String partTypeLabelText = labelPart.getText();
        String typeStr = fieldPartType.getText();

        try {
            if (name.isEmpty()) {
                showWarning("Error Message:", "Name cannot be empty.",
                        "Please provide a value for the part name.");
                return;
            }

            if (partTypeLabelText.equals("Machine ID") && typeStr.isBlank()) {
                showWarning("Error Message", "Part cannot be changed.",
                        "Machine ID field cannot be empty.");
                return;
            }

            if (partTypeLabelText.equals("Company Name") && typeStr.isBlank()) {
                showWarning("Error Message", "Part cannot be changed.",
                        "Company Name field cannot be empty.");
                return;
            }

            int min = Integer.parseInt(fieldPartMin.getText());
            int max = Integer.parseInt(fieldPartMax.getText());
            int inventory = Integer.parseInt(fieldPartInventory.getText());

            if (fieldPartPrice.getText().isEmpty()){
                setFields();
                showWarning("Error Message:", "Price cannot be empty.",
                        "Please provide a value for the price.");
                return;
            }

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
                showWarning("Error Message", "Part cannot be changed.",
                        "Please ensure that the inventory value is greater than or equal to the minimum value and less than or equal to the maximum value.");
                return;
            }

            selectedPart.setName(fieldPartName.getText());
            selectedPart.setPrice(Double.parseDouble(fieldPartPrice.getText()));
            selectedPart.setStock(Integer.parseInt(fieldPartInventory.getText()));
            selectedPart.setMin(Integer.parseInt(fieldPartMin.getText()));
            selectedPart.setMax(Integer.parseInt(fieldPartMax.getText()));

            // Continuing the original savePartChange logic from here:
            int id = selectedPart.getId();
            double price = Double.parseDouble(fieldPartPrice.getText());
            int stock = Integer.parseInt(fieldPartInventory.getText());
            Part newPart;

            if (selectInHouse.isSelected() && selectedPart instanceof Outsourced) {
                Inventory.deletePart(selectedPart);
                int machineId = Integer.parseInt(fieldPartType.getText());
                Inventory.addingNewPart(new InHouse(id, name, price, stock, min, max, machineId));

            } else if (selectOutsourced.isSelected() && selectedPart instanceof InHouse) {
                Inventory.deletePart(selectedPart);
                String companyName = fieldPartType.getText();
                Inventory.addingNewPart(new Outsourced(id, name, price, stock, min, max, companyName));

            } else if (selectInHouse.isSelected()) {
                int machineId = Integer.parseInt(fieldPartType.getText());
                newPart = new InHouse(id, name, price, stock, min, max, machineId);
                Inventory.updatePart(id, newPart);
            } else if (selectOutsourced.isSelected()) {
                String companyName = fieldPartType.getText();
                if(companyName.isEmpty()) {
                    showWarning("Error:", "Part cannot be saved.",
                            "Please make sure all the fields are filled correctly.");
                    return;
                }
                newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.updatePart(id, newPart);
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (NumberFormatException ignore) {
            showWarning("Error:", "Part cannot be modified.",
                    "Please validate the values entered are correct.");
        } catch (IllegalArgumentException e) {
            showWarning("Error:", "Part cannot be saved.",
                    "Please make sure all the fields are filled correctly.");
        }
    }
    /**
     * This method is used to handle the event when the user clicks on the cancel button on the ModifyPart screen and returns the user to the Main_View screen.
     * */
    public void cancelPartChange(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
