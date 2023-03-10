package seals.inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/***
 * This class represents the inventory and includes parts/products methods
 */
public class Inventory {

    //OBSERVABLE LISTS TO HOLD ALL PARTS AND PRODUCTS//
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //METHODS//
    public static void addPart(Part newPart) {
        if (newPart != null) allParts.add(newPart);
    }

    public static void addProduct(Product newProduct) {
        if(newProduct != null) allProducts.add(newProduct);
    }

    public static int numberOfParts() {
        return allParts.size();
    }

    public static int numberOfProducts() {
        return allProducts.size();
    }

    public static Part lookUpPart(int partId) {
        //IF NOT EMPTY ITERATE THROUGH LIST AND RETURN PART ID//
        if(!allParts.isEmpty()) {
            for(int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partId) return allParts.get(i);
            }
        }
        return null;
    }

    public static Product lookUpProduct(int productId) {
        //IF NOT EMPTY ITERATE THROUGH LIST AND RETURN PRODUCT ID//
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == productId) return allProducts.get(i);
            }
        }
        return null;
    }

    public static ObservableList<Part> lookUpPart(String partName) {
        if (!allParts.isEmpty()) {
            ObservableList searchListOfParts = FXCollections.observableArrayList();
            for (Part part : getAllParts()) {
                if (part.getName().contains(partName)) {
                    searchListOfParts.add(part);
                }
            }
            return searchListOfParts;
        }
        return null;
    }

    public static ObservableList<Product> lookUpProduct(String productName) {
        if (!allProducts.isEmpty()) {
            ObservableList searchListOfProducts = FXCollections.observableArrayList();
            for (Product product : getAllProducts()) {
                if (product.getName().contains(productName)) {
                    searchListOfProducts.add(product);
                }
            }
            return searchListOfProducts;
        }
        return null;
    }

    public static void updatePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                allParts.set(i, selectedPart);
                break;
            }
        }
    }

    public static void updateProduct(Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.set(i, selectedProduct);
            }
        }
    }

    public static boolean deletePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteProduct(Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    //RETURN ALL PARTS OR PRODUCTS//
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
