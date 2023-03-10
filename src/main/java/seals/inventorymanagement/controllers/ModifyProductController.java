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
import javafx.scene.layout.AnchorPane;
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
 * This class represents modifying a product
 */
public class ModifyProductController implements Initializable {
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
    private TextField addProductId;
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

    @FXML private AnchorPane anchor;

    //HOLD ASSOCIATED PARTS//
    private ObservableList<Part> associatedParts = observableArrayList();

    //TESTING///////
    @FXML private Product selectedProductFromMain;



    //SETTING TEXT OF TEXT FIELDS TO SELECTED PRODUCT FROM MAIN//
    public void getSelectedProduct(Product productModified) {
        selectedProductFromMain = productModified;
        addProductId.setText(String.valueOf(selectedProductFromMain.getId()));
        addProductName.setText(selectedProductFromMain.getName());
        addProductInventory.setText(String.valueOf(selectedProductFromMain.getStock()));
        addProductPrice.setText(String.valueOf(selectedProductFromMain.getPrice()));
        addProductMax.setText(String.valueOf(selectedProductFromMain.getMax()));
        addProductMin.setText(String.valueOf(selectedProductFromMain.getMin()));
        //SET FOCUS ON ANCHOR TO GET RID OF CURSOR//
        anchor.requestFocus();

        associatedParts = productModified.getAllAssociatedParts();
        associatedPartTable.setItems(associatedParts);
    }


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
            return;
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
        String modifiedName = addProductName.getText();
        int modifiedStock = Integer.parseInt(addProductInventory.getText());
        double modifiedPrice = Double.parseDouble(addProductPrice.getText());
        int modifiedMin = Integer.parseInt(addProductMin.getText());
        int modifiedMax = Integer.parseInt(addProductMax.getText());

        //MAX MUST BE LESS THAN MIN ALERT USER//
        if (modifiedMax < modifiedMin) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Minimum must have a value less than maximum");
            alert.showAndWait();
            return;
        }

        //IF STOCK IS GREATER THAN MAX OR LESS THAN MIN ALERT USER//
        if (modifiedStock > modifiedMax || modifiedStock < modifiedMin) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Inv value must be between min and max");
            alert.showAndWait();
            return;
        }

        Product modifiedProduct = new Product(Integer.parseInt(addProductId.getText()),
                modifiedName,
                modifiedPrice,
                modifiedStock,
                modifiedMin,
                modifiedMax);

        Inventory.addProduct(modifiedProduct);
        Inventory.deleteProduct(selectedProductFromMain);

        for (Part part : associatedParts) {
            modifiedProduct.addAssociatedPart(part);
        }

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        modPartTableView.setItems(Inventory.getAllParts());
        modPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        selectedProductFromMain = new Product(0, null, 0.0, 0,0,0);
        associatedParts = selectedProductFromMain.getAllAssociatedParts();

        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartTable.setItems(selectedProductFromMain.getAllAssociatedParts());
    }
}

