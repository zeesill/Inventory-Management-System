package seals.inventorymanagement;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/***
 * @author Zachary Seals
 * Software 1 - WGU
 * ENHANCEMENT & RUNTIME ERROR CAN BE FOUND in MainController.java
 */


/***
 * This class represents the main - calling launch and allowing for the application to begin
 */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenuView.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root, 900, 365));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}