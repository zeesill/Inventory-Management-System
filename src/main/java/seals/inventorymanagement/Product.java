package seals.inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;


/***
 * This class represents creating a product
 */
public class Product {
    @FXML private ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
    @FXML private int id;
    @FXML private String name;
    @FXML private Double price;
    @FXML private int stock;
    @FXML private int min;
    @FXML private int max;

    //CONSTRUCTOR FOR PRODUCT//
    public Product(int id, String name, Double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    //GETTERS && SETTERS//

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }


    //METHODS OUTSIDE OF GETTERS && SETTERS//
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(int selectedAssociatedPart) {
        for(int i = 0; i < associatedParts.size(); i++) {
            if(associatedParts.get(i).getId() == selectedAssociatedPart) {
                associatedParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }


}
