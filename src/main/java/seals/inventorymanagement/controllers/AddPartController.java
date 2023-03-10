package seals.inventorymanagement.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seals.inventorymanagement.InHouse;
import seals.inventorymanagement.Inventory;
import seals.inventorymanagement.Outsourced;
import seals.inventorymanagement.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/***
 * This class represents adding a new part
 */
public class AddPartController implements Initializable {
    //ADD PART TEXT FIELD VARIABLES//
    @FXML private TextField partIdTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partInvTextField;
    @FXML private TextField partPriceTextField;
    @FXML private TextField partMaxTextField;
    @FXML private TextField partMinTextField;


    //LOGIC MAY BE ADDED SHARING TEXT FIELD FOR MACHINE ID AND COMPANY NAME//
    @FXML private TextField machineIDCompanyNameTextField;
    @FXML private Label machineIDCompanyNameLabel;


    //OUTSOURCED RADIO BUTTON && IN-HOUSE RADIO BUTTON//
    //TOGGLEABLE GROUP IS WITHIN INITIALIZE METHOD//
    @FXML private RadioButton inHouseRadioBtn;
    @FXML private RadioButton outSourcedRadioBtn;

    //INITIALIZE
    Parent scene;
    Stage stage;


    //CHANGE LABEL WHEN SPECIFIC RADIO BUTTON IS SELECTED//
    @FXML
    private void selectInHouse() {
        if(inHouseRadioBtn.isSelected()) {
            machineIDCompanyNameLabel.setText("Machine ID");
        }
    }

    @FXML
    private void selectOutSourced() {
        if(outSourcedRadioBtn.isSelected()) {
            machineIDCompanyNameLabel.setText("Company Name");
        }
    }

//ADD PART BUTTON//
    public void addPartSaveBtnPushed(ActionEvent event) throws IOException {
        //ID HAS BEEN DISABLED FOR THE USER - INCREMENT ID AUTOMATICALLY WHEN CREATING NEW INSTANCES OF PART//
        int id = 0;
        for(Part ignored : Inventory.getAllParts()){
            id++;
        }

        //UPON SELECTING THE ADD PART BUTTON, "TRY" WILL BE USED TO TEST THE CODE FOR ANY ERRORS AND ALERT THE USER//
        try {
            //SETTING VARIABLES FROM RECEIVED INPUT - USING THESE VARIABLES FOR ERROR CONTROL//
            int stock = Integer.parseInt(partInvTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            int machineId = Integer.parseInt(machineIDCompanyNameTextField.getText());

            //IF MACHINE ID IS A NEGATIVE NUMBER ALERT THE USER//
            if (machineId < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("You must enter a positive number for the Machine ID");
                alert.showAndWait();
                return;
            }

            //IF MAX IS LESS THAN MIN ALERT THE USER//
            if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("The Minimum Value must be less than the Maximum Value");
                alert.showAndWait();
                return;
            }

            //IF STOCK IS GREATER THAN MAX OR LESS THAT MINIMUM ALERT THE USER//
            if(stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Inventory Values must be between the maximum and minimum numbers specified");
                alert.showAndWait();
                return;
            }

            //DIFFERENTIATE BETWEEN "IN HOUSE" OR "OUTSOURCED" PARTS BEING ADDED//
            Part newPart; //CREATE NEW INSTANCE OF CLASS PART//

            //IN HOUSE//
            if(inHouseRadioBtn.isSelected()) {
                newPart = new InHouse(id, "", 0.0, 0, 0, 0, 0);
                newPart.setName(partNameTextField.getText());
                newPart.setPrice(Double.parseDouble(partPriceTextField.getText()));
                newPart.setStock(Integer.parseInt(partInvTextField.getText()));
                newPart.setMin(Integer.parseInt(partMinTextField.getText()));
                newPart.setMax(Integer.parseInt(partMaxTextField.getText()));
                ((InHouse) newPart).setMachineId(Integer.parseInt(machineIDCompanyNameTextField.getText()));

                Inventory.addPart(newPart);
            }
            else {
                //OUTSOURCED//
                newPart = new Outsourced(id, "", 0.0, 0, 0, 0, "");
                newPart.setName(partNameTextField.getText());
                newPart.setPrice(Double.parseDouble(partPriceTextField.getText()));
                newPart.setStock(Integer.parseInt(partInvTextField.getText()));
                newPart.setMin(Integer.parseInt(partMinTextField.getText()));
                newPart.setMax(Integer.parseInt(partMaxTextField.getText()));
                ((Outsourced) newPart).setCompanyName(machineIDCompanyNameTextField.getText());

                Inventory.addPart(newPart);
            }

            //IF NO ERRORS ARE DETECTED SWITCH THE SCENE TO MAIN MENU VIEW//
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (NumberFormatException empty) { //IF TEXT FIELDS ARE LEFT EMPTY OR CONTAINS AN INCORRECT DATA TYPE ALERT USER//
            empty.printStackTrace();
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setTitle("Alert");
            alert.setContentText("Please make sure text fields are not empty or have correct values");
            alert.showAndWait();
        }
    }

    //THIS METHOD WHEN CALLED WILL ALLOW THE CANCEL BUTTON IN THE ADD PART SCENE TO GO BACK TO THE MAIN SCENE//
public void fromAddPartToMainBtnPushed(ActionEvent event) throws IOException {
    Parent addPartParent = FXMLLoader.load(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
    Scene addPartScene = new Scene(addPartParent);
    //THIS LINE GETS THE STAGE INFORMATION//
    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
    window.setScene(addPartScene);
    window.show();
}




























    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TOGGLEABLE GROUP FOR OUTSOURCED && IN HOUSE RADIO BUTTONS//
        ToggleGroup inHouseOutSourcedBtns = new ToggleGroup();
        this.inHouseRadioBtn.setToggleGroup(inHouseOutSourcedBtns);
        this.outSourcedRadioBtn.setToggleGroup(inHouseOutSourcedBtns);
        //SET IN-HOUSE RADIO BTN PRE SELECTED//
        this.inHouseRadioBtn.setSelected(true);








    }
}
