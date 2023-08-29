module c482.inventory_project_pa {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens c482.inventory_project_pa to javafx.fxml;
    exports c482.inventory_project_pa;
}