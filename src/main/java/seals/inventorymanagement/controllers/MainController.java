package seals.inventorymanagement.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import seals.inventorymanagement.Inventory;
import seals.inventorymanagement.Part;
import seals.inventorymanagement.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static seals.inventorymanagement.Inventory.getAllParts;

/***
 * This is the class representing the main menu view
 */
public class MainController implements Initializable {

    //PARTS TABLE COLUMN VARIABLES//
    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryLevelColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TextField searchPartField;


    //PRODUCTS TABLE COLUMN VARIABLES//
    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryLevelColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;

///////////////////////////////////////////
    //TESTING//
    @FXML private TextField searchProductField;


    //EXIT BUTTON METHOD TO CLOSE APPLICATION//
    public void exitButtonPushed() {
        Platform.exit();
    }

    //WHEN THE ADD BUTTON IS PUSHED, IT WILL CHANGE THE SCENE TO THE ADD PARTS SCENE//
    public void toAddPartSceneBtnPushed(ActionEvent event) throws IOException {
        //LOAD ADD PART VIEW FXML//
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/seals/inventorymanagement/AddPartView.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        //THIS LINE GETS THE STAGE INFORMATION//
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addPartScene);
        window.show();
    }

    //WHEN MODIFY BUTTON IS PUSHED, IT WILL CHANGE THE SCENE TO THE MODIFY PART SCENE//
    //AND POPULATE THE MODIFY SCENE WITH SELECTED ITEM FROM MAIN SCENE PART TABLE//
    public void toModifyPartSceneBtnPushed(ActionEvent event) throws IOException {
        //GET SELECTED ROW//
        Part selectedItems = partTableView.getSelectionModel().getSelectedItem();

        //ALERT THE USER IF A ROW IS NOT SELECTED//
        if (selectedItems == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a row within the Part Table to Modify");
            alert.showAndWait();
            return;
        }
        //GET MODIFY PART VIEW SCENE//
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //LOAD MODIFY PART VIEW FXML//
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/seals/inventorymanagement/ModifyPartView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //LOAD MODIFY PART CONTROLLER//
        ModifyPartController controller = loader.getController();
        //GET SELECTED PART METHOD FROM MODIFY PART CONTROLLER//
        controller.getSelectedPart(partTableView.getSelectionModel().getSelectedItem()); //LOAD PART USER SELECTS INTO MODIFY PART SCENE//
        stage.show();
    }






    //WHEN THIS METHOD IS CALLED IT WILL DELETE THE ROW THAT IS SELECTED IN PART TABLE//
    public void deletePartRow() {
        Part selectedItems = partTableView.getSelectionModel().getSelectedItem();
        if (selectedItems == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Part within the Part Table to Delete");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to remove this Part?");
        Optional<ButtonType> result = alert.showAndWait();
        //DELETE ONLY IF THE USER PRESSES OK//
        if (result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedItems);
            //SET TABLE VIEW PLACEHOLDER BACK TO ORIGINAL TEXT//
            partTableView.setPlaceholder(new Label("No content in table"));
        }
    }


    //SEARCH FOR A ROW WITHIN PART TABLE USING TEXT FIELD//

    /**
     * ENHANCEMENT
     * This code could be updated to instead of showing an empty table when a part is not found,
     * alert the user that this part does not exist, and if you would like to add said part -
     * redirecting the user to add part
     */

    //FIXED - INSTEAD OF ALERTING THE USER VIA THE TABLE UPDATING IF A PART DOES NOT EXIST, WE GIVE THE USER AN ERROR
    @FXML public void searchPartsEnter(){
        //GET TEXT USER INPUTS AND STORE IN LIST OF PARTS & POINT OUTSIDE METHOD//
        ObservableList<Part> listOfParts = listOfParts(searchPartField.getText());
        //SET THE ITEMS WITHIN THE PART TABLE VIEW OF WHAT USER INPUTS//
        partTableView.setItems(listOfParts);
        //RESET SEARCH PART FIELD//
        searchPartField.setText("");
    }

        //HOLD PART//
        ObservableList<Part> listOfParts(String piece) {
        //HOLDS COLLECTION//
        ObservableList<Part> nameOfParts = FXCollections.observableArrayList();
        //HOLD INVENTORY PARTS//
        ObservableList<Part> allParts = Inventory.getAllParts();

        //IF ID SEARCHED MATCHES ID IN PART || IF NAME MATCHES NAME IN PART - ADD PART//
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchPartField.getText()) || part.getName().contains(piece)) {
                nameOfParts.add(part);
            }
        }
        //ELSE THROW ERROR//
        if (nameOfParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setContentText("Part Is Not Valid");
            alert.showAndWait();
            //NOTIFY USER TO REFRESH TABLE UPON ENTERING AN INCORRECT PART//
            partTableView.setPlaceholder(new Label("Press Enter OR Add Part to try again"));
        }
        //RETURN LIST//
        return nameOfParts;
    }


///////////////////////////////////////PRODUCT TABLE//////////////////////////////////////////////////

    //WHEN ADD PRODUCT BUTTON IS PUSHED - CHANGE THE SCENE TO ADD PRODUCT VIEW SCENE//
    public void addProductBtnPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //LOAD ADD PRODUCT VIEW//
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/seals/inventorymanagement/AddProductView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //SEARCH PRODUCT TABLE//

    /**
     * RUNTIME ERROR
     * I was receiving a NumberFormatException error whenever a string was being entered into the search field
     * I then changed the code to instead of updating the table and giving an error message that way, to give an alert instead.
     */

    @FXML public void searchProductsEnter(){
        //GET TEXT USER INPUTS AND STORE IN LIST OF PRODUCTS & POINT OUTSIDE METHOD//
        ObservableList<Product> listOfProducts = listOfProducts(searchProductField.getText());
        //SET THE ITEMS WITHIN THE PART TABLE VIEW OF WHAT USER INPUTS//
        productTableView.setItems(listOfProducts);
        //RESET SEARCH PART FIELD//
        searchProductField.setText("");
    }

    //HOLD PRODUCT//
    ObservableList<Product> listOfProducts(String piece) {
        //HOLDS COLLECTION//
        ObservableList<Product> nameOfProducts = FXCollections.observableArrayList();
        //HOLD INVENTORY PRODUCTS//
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        //IF ID SEARCHED MATCHES ID IN PRODUCT || IF NAME MATCHES NAME IN PART - ADD PART//
        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchProductField.getText()) || product.getName().contains(piece)) {
                nameOfProducts.add(product);
            }
        }
        //ELSE THROW ERROR//
        if (nameOfProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setContentText("Product Is Not Valid");
            alert.showAndWait();
            //NOTIFY USER TO REFRESH TABLE UPON ENTERING AN INCORRECT PRODUCT//
            productTableView.setPlaceholder(new Label("Press Enter OR Add Product to try again"));
        }
        //RETURN LIST//
        return nameOfProducts;
    }

    //DELETE PRODUCT WITHIN PRODUCT TABLE//
    public void deleteBtnPushed() {
        Product selectedItems = productTableView.getSelectionModel().getSelectedItem();
        if (selectedItems == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Product within the Product Table to Delete");
            alert.showAndWait();
            return;
        }

        if (selectedItems.getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedItems);
                productTableView.setPlaceholder(new Label("No content in table"));
            }
        } else  {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This product has associated parts");
            alert.showAndWait();
        }
    }

    public void modifyProductBtnPushed(ActionEvent event) throws IOException {
        //GET SELECTED PRODUCT AND STORE WITHIN SELECTED ITEMS//
        Product selectedItems = productTableView.getSelectionModel().getSelectedItem();

        //ALERT THE USER IF A ROW IS NOT SELECTED//
        if (selectedItems == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a row within the Product Table to Modify");
            alert.showAndWait();
            return;
        }
        //GET MODIFY PART VIEW SCENE//
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //LOAD MODIFY PART VIEW FXML//
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/seals/inventorymanagement/ModifyProductView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //LOAD MODIFY PART CONTROLLER//
        ModifyProductController controller = loader.getController();
        //GET SELECTED PART METHOD FROM MODIFY PART CONTROLLER//
        controller.getSelectedProduct(productTableView.getSelectionModel().getSelectedItem()); //LOAD PART USER SELECTS INTO MODIFY PART SCENE//
        stage.show();
    }








    //INITIALIZE ITEMS UPON LOADING APPLICATION//
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //SET SELECTION MODE FOR ROWS OF TABLE TO SINGLE//
        TableView.TableViewSelectionModel<Part> selectionModel = partTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        //PARTS//
        partTableView.setItems(getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //PRODUCTS//
        productTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


}