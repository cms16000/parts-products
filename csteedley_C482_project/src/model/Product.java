package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Christopher  Steedley
 */

/** Product Class, similar to product except associated parts list(ability to hold list of parts) */

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productId;
    private String productName;
    private Double productPrice;
    private int productStock;
    private int productMin;
    private int productMax;

    public Product(int productId, String productName, Double productPrice, int productStock, int productMin, int productMax) {
        setProductId(productId);
        setProductName(productName);
        setProductPrice(productPrice);
        setProductStock(productStock);
        setProductMin(productMin);
        setProductMax(productMax);

    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public int getProductMin() {
        return productMin;
    }

    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    public int getProductMax() {
        return productMax;
    }

    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }


    /** used to add and delete associated parts */

    public void addAssociatedPart(Part partAdd) {
        associatedParts.add(partAdd);
    }

    public  boolean removeAssociatedPart(int removePart) {
        for (Part p : associatedParts) {
            if (p.getId() == removePart) {
                associatedParts.remove(p);
                return true;
            }
        }
        return false;
    }
    /** lookups and getters/setters for associated parts */
    public ObservableList<Part> getAssociatedParts() { return associatedParts; }

    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public Part lookupAssociatedPart(int partSearch) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == partSearch) {
                return associatedParts.get(i);
            }
        }
        return null;
    }
    public int getPartsListSize() {
        return associatedParts.size();
    }
}
