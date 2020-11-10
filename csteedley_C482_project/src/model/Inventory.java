package model;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 *
 * @author Christopher Steedley
 */

/** Inventory class, all parts/products are part of inventory*/

public class Inventory {
    private static  ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** add a Part */
    public static void addPart(Part pAdd) {
        if (pAdd != null) {
            allParts.add(pAdd);
        }
    }
    /** add a product */
    public static void addProduct(Product prAdd) {
        if (prAdd != null) {
            allProducts.add(prAdd);
        }
    }
    /** LookUp methods to help with searching */
    public static Part lookUpPart(int partSearch) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partSearch) {
                    return allParts.get(i);
                }
            }

        }
        return null;
    }

    public static ObservableList<Part> lookUpPart(String partNameSearch) {
        if (!allParts.isEmpty()) {
            ObservableList searchParts = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getName().contains(partNameSearch)) {
                    searchParts.add(p);
                }
            }
            return searchParts;
        }
        return null;
    }


    public static Product lookUpProduct(int productSearch) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductId() == productSearch) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }

    public static ObservableList<Product> lookUpProduct(String productNameSearch) {
        return null;
    }

    /** used to modify Part*/

    public static void updatePart(Part pUpdate) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == pUpdate.id) {
                allParts.set(i, pUpdate);
                break;
            }
        }
    }
    /** used to modify products */
    public static void updateProduct(Product prUpdate) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductId() == prUpdate.getProductId()) {
                allProducts.set(i, prUpdate);
                break;
            }
        }
    }
    /** deletes a part  */
    public static boolean deletePart(Part partDelete) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partDelete.getId()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }



    /** deletes a product  */
    public static boolean deleteProduct(int productDelete) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductId() == productDelete) {
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    /** list sizes handy for loops */
    public int partListSize() {
        return allParts.size();
    }

    public int productListSize() {
        return allProducts.size();
    }

    /** Gets all products/Parts  in a list */

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

}