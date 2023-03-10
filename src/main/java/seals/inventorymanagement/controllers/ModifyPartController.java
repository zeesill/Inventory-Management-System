package seals.inventorymanagement.controllers;

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
import javafx.stage.Stage;
import seals.inventorymanagement.InHouse;
import seals.inventorymanagement.Inventory;
import seals.inventorymanagement.Outsourced;
import seals.inventorymanagement.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/***
 * This class represents modifying a part
 */
public class ModifyPartController implements Initializable {

    //TEXT FIELD VARIABLES//
    @FXML
    private TextField modifyId;
    @FXML
    private TextField modifyName;
    @FXML
    private TextField modifyInv;
    @FXML
    private TextField modifyCost;
    @FXML
    private TextField modifyMax;
    @FXML
    private TextField modifyMin;
    @FXML
    private TextField modifyMachineIdCompanyName;

    //RADIO BUTTONS//
    @FXML private RadioButton inHouseRadioBtn;
    @FXML private RadioButton outsourcedRadioBtn;

    //PART//
    @FXML private Part selectedPartFromMain;

    //CREATE TOGGLE GROUP FOR RADIO BUTTONS//
    @FXML private ToggleGroup modifiedRadioBtnToggle;

    //INITIALIZE STAGE AND SCENE FOR PASSING DATA FROM DIFFERENT SCENES//
    Stage stage;
    Parent scene;

    //CREATE VARIABLE FOR LABEL TO CHANGE//
    @FXML private Label modifyMachineIdCompanyNameLabel;

    //SET TEXT FOR COMPANY NAME OR MACHINE ID DEPENDING ON RADIO BUTTON SELECTED//
    public void inHouseRadioBtnPushed() {
        if (inHouseRadioBtn.isSelected()) {
            modifyMachineIdCompanyNameLabel.setText("Machine ID");
        }
    }
    public void outsourcedRadioBtnPushed() {
        if (outsourcedRadioBtn.isSelected()) {
            modifyMachineIdCompanyNameLabel.setText("Company Name");
        }
    }

    //STORING DATA FROM MAIN SCENE TO MODIFY PART SCENE ON WHETHER IN HOUSE OR OUTSOURCED//
    public void getSelectedPart(Part partModified) {
        selectedPartFromMain = partModified;
        //SETTING FIELDS TO THE SELECTED PART VALUES//
        modifyId.setText(String.valueOf(selectedPartFromMain.getId()));
        modifyName.setText(selectedPartFromMain.getName());
        modifyInv.setText(String.valueOf(selectedPartFromMain.getStock()));
        modifyCost.setText(String.valueOf(selectedPartFromMain.getPrice()));
        modifyMax.setText(String.valueOf(selectedPartFromMain.getMax()));
        modifyMin.setText(String.valueOf(selectedPartFromMain.getMin()));

        //CHECK WHETHER THE SELECTED PART IS AN INSTANCE OF CLASS IN HOUSE OR OUTSOURCED//
        //DETERMINE CORRECT RADIO BUTTON SELECTED AND CHANGE LABEL TO MATCH//
        if (selectedPartFromMain instanceof InHouse) {
            inHouseRadioBtn.setSelected(true);
            modifyMachineIdCompanyNameLabel.setText("Machine ID");
            modifyMachineIdCompanyName.setText(String.valueOf(((InHouse)selectedPartFromMain).getMachineId()));
        }
        if (selectedPartFromMain instanceof Outsourced) {
            outsourcedRadioBtn.setSelected(true);
            modifyMachineIdCompanyNameLabel.setText("Company Name");
            modifyMachineIdCompanyName.setText(((Outsourced) selectedPartFromMain).getCompanyName());
        }
    }





    //CANCEL BUTTON BRINGS USER BACK TO MAIN SCENE//
    public void cancelBtnPushed(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        //THIS LINE GETS THE STAGE INFORMATION//
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addPartScene);
        window.show();
    }

    //SAVE NEW PART AND DELETE OLD ONE UPON CLICKING SAVE BUTTON//
    public void saveModifyBtnPushed(ActionEvent event) throws IOException {
        int modifiedStock = Integer.parseInt(modifyInv.getText());
        int modifiedMin = Integer.parseInt(modifyMin.getText());
        int modifiedMax = Integer.parseInt(modifyMax.getText());

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

        //IF IN HOUSE RADIO BUTTON IS SELECTED CREATE NEW IN HOUSE PART USING MACHINE ID//
        if (modifiedRadioBtnToggle.getSelectedToggle().equals(inHouseRadioBtn)) {
            //NEW IN HOUSE PART//
            Part modifiedParts = new InHouse(Integer.parseInt(modifyId.getText()),
                                             modifyName.getText(),
                                             Double.parseDouble(modifyCost.getText()),
                                             Integer.parseInt(modifyInv.getText()),
                                             Integer.parseInt(modifyMin.getText()),
                                             Integer.parseInt(modifyMax.getText()),
                                             Integer.parseInt(modifyMachineIdCompanyName.getText()));
            //ADD MODIFIED PART AND DELETE PREVIOUSLY SELECTED PART//
            Inventory.addPart(modifiedParts);
            Inventory.deletePart(selectedPartFromMain);
        }

        //IF OUTSOURCED RADIO BUTTON IS SELECTED CREATE NEW OUTSOURCED PART USING COMPANY NAME//
        if (modifiedRadioBtnToggle.getSelectedToggle().equals(outsourcedRadioBtn)) {
            //NEW OUTSOURCED PART//
            Part modifiedParts = new Outsourced(Integer.parseInt(modifyId.getText()),
                                                modifyName.getText(),
                                                Double.parseDouble(modifyCost.getText()),
                                                Integer.parseInt(modifyInv.getText()),
                                                Integer.parseInt(modifyMin.getText()),
                                                Integer.parseInt(modifyMax.getText()),
                                                modifyMachineIdCompanyName.getText());
            //ADD MODIFIED PART AND DELETE PREVIOUSLY SELECTED PART//
            Inventory.addPart(modifiedParts);
            Inventory.deletePart(selectedPartFromMain);
        }
        //RETURN TO MAIN MENU VIEW UPON CLICKING SAVE//
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/seals/inventorymanagement/MainMenuView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }






















    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //SET TOGGLE GROUP FOR RADIO BUTTONS UPON LOADING SCENE//
        modifiedRadioBtnToggle = new ToggleGroup();
        inHouseRadioBtn.setToggleGroup(modifiedRadioBtnToggle);
        outsourcedRadioBtn.setToggleGroup(modifiedRadioBtnToggle);
        //SELECT IN HOUSE RADIO BUTTON ON DEFAULT//
        inHouseRadioBtn.setSelected(true);
    }
}
