package c482.inventory_project_pa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class contains the method to start our Inventory Management System application and a method to input test data. */
public class Main extends Application {

    /**
     * This method is to start the Inventory Management System application. */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main_View.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * This method contains test data for our application to ensure it works as intended. */
    private static void addTestData() {

        Outsourced lights = new Outsourced(Inventory.createPartID(),"Lights", 55.99, 8, 4, 80, "S&S Cycle");
        Inventory.addingNewPart(lights);

        Outsourced exhaust = new Outsourced(Inventory.createPartID(),"Exhaust", 780.99, 14, 2, 75, "Freedom Performance");
        Inventory.addingNewPart(exhaust);


        InHouse tires = new InHouse(Inventory.createPartID(),"Tires", 185.99, 20, 4, 60, 1000);
        Inventory.addingNewPart(tires);

        InHouse engine = new InHouse(Inventory.createPartID(),"Engine", 2400.99, 20, 5, 50, 1001);
        Inventory.addingNewPart(engine);

        InHouse saddlebags = new InHouse(Inventory.createPartID(),"Saddlebags", 1100.99, 30, 10, 100, 1002);
        Inventory.addingNewPart(saddlebags);

        Product cruiserMotorcycle = new Product("Cruiser Motorcycle", 18500.99, 12, 3, 18);
        Inventory.addingNewProduct(cruiserMotorcycle);
        cruiserMotorcycle.addAssociatedPart(exhaust);

        Product touringMotorcycle = new Product("Touring Motorcycle", 23000.99, 10, 3, 18);
        Inventory.addingNewProduct(touringMotorcycle);
        touringMotorcycle.addAssociatedPart(engine);

        Product adventureMotorcycle = new Product("Adventure Motorcycle", 18500.99, 12, 3, 18);
        Inventory.addingNewProduct(adventureMotorcycle);
        adventureMotorcycle.addAssociatedPart(tires);
        adventureMotorcycle.addAssociatedPart(lights);
        adventureMotorcycle.addAssociatedPart(saddlebags);

        Product sportMotorcycle = new Product("Sport Motorcycle", 23000.99, 10, 3, 18);
        Inventory.addingNewProduct(sportMotorcycle);
    }

    /**
     * This method launches the program and runs the adds the test data into the program. */
    public static void main(String[] args) {
        addTestData();
        launch(args);
    }

}