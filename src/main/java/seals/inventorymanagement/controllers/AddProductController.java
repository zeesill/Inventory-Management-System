package seals.inventorymanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import static javafx.collections.FXCollections.observableArrayList;

/***
 * This class represents adding a new product
 */
public class AddProductController implements Initializable {

    //PART TABLE VARIABLES//
    @FXML
    private TableView<Part> modPartTableView;
    @FXML
    private TableColumn<Part, Integer> modPartIdColumn;
    @FXML
    private TableColumn<Part, String> modPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> modPartInventoryLevelColumn;
    @FXML
    private TableColumn<Part, Double> modPartPriceColumn;

    //SEARCH PARTS ON MODIFIED PRODUCT SCENE//
    @FXML
    private TextField modPartTextField;


    //ASSOCIATED PART TABLE VARIABLES//
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartIdColumn;
    @FXML
    private TableColumn<Part, String> associatedPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryLevelColumn;
    @FXML
    private TableColumn<Part, Double> associatedPartPriceColumn;

    //ADD PRODUCT TEXT FIELDS//
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInventory;
    @FXML
    private TextField addProductPrice;
    @FXML
    private TextField addProductMax;
    @FXML
    private TextField addProductMin;


    //HOLD ASSOCIATED PARTS//
    private ObservableList<Part> associatedParts = observableArrayList();


    //WHEN CANCEL BUTTON IS PUSHED GO BACK TO MAIN MENU VIEW//
    public void cancelBtnPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        //LOAD MAIN MENU VIEW//
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    //SEARCH TEXT FIELD FOR MODIFY PART ON PRODUCT VIEW//
    @FXML
    public void modSearchPartsEnter() {
        //GET TEXT USER INPUTS AND STORE IN LIST OF PARTS & POINT OUTSIDE METHOD//
        ObservableList<Part> listOfParts = listOfParts(modPartTextField.getText());
        //SET THE ITEMS WITHIN THE PART TABLE VIEW OF WHAT USER INPUTS//
        modPartTableView.setItems(listOfParts);
        //RESET SEARCH PART FIELD//
        modPartTextField.setText("");
    }
    //HOLD PART//
    ObservableList<Part> listOfParts(String piece) {
        //HOLDS COLLECTION//
        ObservableList<Part> nameOfParts = FXCollections.observableArrayList();
        //HOLD INVENTORY PARTS//
        ObservableList<Part> allParts = Inventory.getAllParts();

        //IF ID SEARCHED MATCHES ID IN PART || IF NAME MATCHES NAME IN PART - ADD PART//
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(modPartTextField.getText()) || part.getName().contains(piece)) {
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
            modPartTableView.setPlaceholder(new Label("Press Enter OR Add Part to try again"));
        }
        //RETURN LIST//
        return nameOfParts;
    }







    //ADD PART TO ASSOCIATED PART TABLE//
    @FXML
    public void addAssociatedPartBtnPushed(ActionEvent event) {
        //GET SELECTED PART FROM MODIFIED TABLE VIEW//
        Part part = modPartTableView.getSelectionModel().getSelectedItem();
        //IF USER DOES NOT SELECT A PART TO ADD TO ASSOCIATED PART TABLE//
        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a part to add to associate table");
            alert.showAndWait();
            //ELSE IF USER DOES SELECT A PART, ADD IT TO ASSOCIATED PART TABLE//
        } else {
            associatedParts.add(part);
            associatedPartTable.setItems(associatedParts);
        }
    }

    @FXML
    public void removeAssociatedPartBtnPushed(ActionEvent event) {
        //GET SELECTED PART FROM ASSOCIATED TABLE//
        Part part = associatedPartTable.getSelectionModel().getSelectedItem();
        //IF USER DOES NOT SELECT A PART, ALERT//
        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();
            //ELSE USER DID SELECT A PART//
        } else {
            //GET CONFIRMATION FROM USER//
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setContentText("Delete this associated part?");
            Optional<ButtonType> result = alert2.showAndWait();

            if (result.get() == ButtonType.OK) {
                associatedPartTable.getItems().remove(part);
            }
        }
    }

    //ADD PRODUCT TO PRODUCT TABLE ON MAIN SCENE WHEN SAVE BUTTON IS PUSHED//
    @FXML
    public void saveAddPartBtnPushed(ActionEvent event) throws IOException {
        //START ID AT 0 WHEN ADDING PRODUCT//
        int id = 0;
        //INCREMENT ID BY 1 FOR EACH PRODUCT IN INVENTORY//
        for (Product ignored : Inventory.getAllProducts()) {
            id++;
        }
        //TRY TO GET TEXT FROM TEXT FIELDS, IF EMPTY ALERT USER TO ENTER TEXT IN TEXT FIELDS VIA CATCH//
        try {
            //GET TEXT ENTERED IN TEXT FIELDS//
            String name = addProductName.getText();
            int stock = Integer.parseInt(addProductInventory.getText());
            double price = Double.parseDouble(addProductPrice.getText());
            int max = Integer.parseInt(addProductMax.getText());
            int min = Integer.parseInt(addProductMin.getText());

            //MAX MUST BE GREATER THEN MIN, ALERT USER OTHERWISE//
            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Maximum must have a higher value then minimum");
                alert.showAndWait();
                return;
            }

            //VALUE MUST BE BETWEEN MAX AND MIN, ALERT USER OTHERWISE//
            if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The value must be between maximum and minimum");
                alert.showAndWait();
                return;
            }

            //CREATE EMPTY PRODUCT//
            Product newAddedProduct = new Product(id, " ", 0.0, 0, 0, 0);
            //SET VALUES FOR EACH ARGUMENT FROM TEXT USER ENTERED WITHIN TEXT FIELDS//
            newAddedProduct.setName(addProductName.getText());
            newAddedProduct.setPrice(Double.valueOf(addProductPrice.getText()));//TEST//
            newAddedProduct.setStock(Integer.parseInt(addProductInventory.getText()));
            newAddedProduct.setMin(Integer.parseInt(addProductMin.getText()));
            newAddedProduct.setMax(Integer.parseInt(addProductMax.getText()));
            //ADD THE PRODUCT TO INVENTORY//
            Inventory.addProduct(newAddedProduct);

            //CONNECT NEWLY ADDED PRODUCT TO ASSOCIATED PARTS//
            for (Part part : associatedParts) {
                newAddedProduct.addAssociatedPart(part);
            }
            //GET STAGE INFORMATION//
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            //LOAD MAIN MENU VIEW FOR WHEN SAVE BUTTON IS PRESSED//
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        //INSTEAD OF THROWING EXCEPTION, ALERT USER TO CHECK THAT ALL TEXT FIELDS ARE NOT EMPTY//
        } catch (Exception e) {
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setContentText("Please make sure text fields are not empty");
            alert.showAndWait();
        }
    }

        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            modPartTableView.setItems(Inventory.getAllParts());
            modPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            modPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            modPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            modPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            associatedPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }
