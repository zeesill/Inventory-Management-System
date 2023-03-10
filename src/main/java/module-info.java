module seals.inventorymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens seals.inventorymanagement to javafx.fxml;
    exports seals.inventorymanagement;
    exports seals.inventorymanagement.controllers;
    opens seals.inventorymanagement.controllers to javafx.fxml;
}