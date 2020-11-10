package view_controller;

import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author CHRISTOPHER STEEDLEY
 */

public class AddProductController implements Initializable {

    Inventory inv;

    @FXML
    private TextField productId;

    @FXML
    private TextField productName;

    @FXML
    private TextField productStock;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField productMax;

    @FXML
    private TextField productMin;

    @FXML
    private TextField partSearchBox;

    @FXML
    private TableView<Part> partSearchTable;

    @FXML
    private TableColumn<?, ?> partIDColumn;

    @FXML
    private TableColumn<?, ?> partNameColumn;

    @FXML
    private TableColumn<?, ?> partCountColumn;

    @FXML
    private Button removeCurrentPart;

    @FXML
    private TextField search;

    @FXML
    private Button addProductAddButton;

    @FXML
    private Button addProductDeleteButton;

    @FXML
    private Button addProductCancelButton;

    @FXML
    private Button addProductSaveButton;

    @FXML
    private TableView<Part> assocPartsTable;

    @FXML
    private TableColumn<?, ?> currentPartIDColumn;

    @FXML
    private TableColumn<?, ?> currentPartNameColumn;

    @FXML
    private TableColumn<?, ?> currentPartCountColumn;

    public ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    public ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    public ObservableList<Part> assocPartList = FXCollections.observableArrayList();
    public ObservableList<Part> searchIdHelp = FXCollections.observableArrayList();

    @Override

    public void  initialize(URL url, ResourceBundle rb) {
        generateProductID();
        addToSearchTable();
    }


    public  AddProductController(Inventory inv) {
        this.inv = inv;
    }


    /** see parts Id, is the same almost  */

   private void generateProductID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);


        if (inv.productListSize() == 0) {
            productId.setText(num.toString());

        }
        if (inv.productListSize() == 1000) {
            AlertMessage.errorProduct(3, null);
        } else {
            match = generateNum(num);

            if (!match) {
                productId.setText(num.toString());
            } else {
                generateProductID();
            }
        }

        productId.setText(num.toString());
    }
    private boolean generateNum(Integer num) {
        Part match = inv.lookUpPart (num);
        return match != null;
    }

    /** loads parts to the search parts table */
   private void addToSearchTable() {
        partsInventory.setAll(inv.getAllParts());
        TableColumn<Part, Double> costCol = formatPrice();
        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }
    /** searches Parts see main_screen search parts*/
    @FXML
    void searchParts(KeyEvent event) {

        if (event.getCode().toString().equals("ENTER")) {
            if (!search.getText().trim().isEmpty()) {
                partsInventorySearch.clear();
                for (Part p : inv.getAllParts()) {
                    if (p.getName().contains(search.getText().trim())) {
                        partsInventorySearch.add(p);
                    }
                }
                partSearchTable.setItems(partsInventorySearch);
                partSearchTable.refresh();
            }

            String IdSearch = search.getText();
            if (IdSearch.matches("[0-9]*")) {

                for (Part p : inv.getAllParts()) {
                    if(IdSearch.equals("")) break;
                    if (p.getId() == Integer.parseInt(IdSearch)) {

                        partsInventorySearch.add(p);
                    }

                }
            }
        }
        if (!partsInventorySearch.isEmpty()) {
            searchIdHelp.clear();
            for (Part x : partsInventorySearch){
                if (!searchIdHelp.contains(x)){
                    searchIdHelp.add(x);}
            }
            partSearchTable.setItems(searchIdHelp);
            partSearchTable.refresh();
        }


        if (search.getText().trim().isBlank()) {
            partSearchTable.setItems(partsInventory);

        }


        if (event.getCode().toString().equals("ENTER")) {
            if (Bindings.isEmpty(partSearchTable.getItems()).get()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Nothing Found");
                alert.setContentText("try Searching for something else");
                alert.showAndWait();
            }

        }

    }
    /** add associated parts, checks if it already exists as well*/
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeat = false;

        if (addPart != null) {
            int id = addPart.getId();
            for (Part part : assocPartList) {
                if (part.getId() == id) {
                    AlertMessage.errorProduct(2, null);
                    repeat = true;
                }
            }

            if (!repeat) {
                assocPartList.add(addPart);

            }

            TableColumn<Part, Double> costCol = formatPrice();
            assocPartsTable.getColumns().addAll(costCol);

            assocPartsTable.setItems(assocPartList);
        }
    }
    /** deletes a part from being associated with product*/
    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = AlertMessage.confirmationWindow(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            AlertMessage.infoWindow(1, removePart.getName());
        } else {
            AlertMessage.infoWindow(2, "");
        }

    }

    /** cancel add product, return to main screen*/
    @FXML
    private void cancelAddProduct(MouseEvent event
    ) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }
    /** verifies if product meets conditions and saves it and returns to main screen*/
    @FXML
    private void saveAddProduct(MouseEvent event
    ) {
        boolean end = false;
        TextField[] fieldCount = {productStock, productPrice, productMin, productMax};
        double minCost = 0;
        for (Part part : assocPartList) {
            minCost += part.getPrice();
        }
        if (productName.getText().trim().isEmpty() || productName.getText().trim().toLowerCase().equals("part name")) {
            AlertMessage.errorProduct(4, productName);
            return;
        }
        for (TextField fieldCount1 : fieldCount) {
            boolean valueError = checkValue(fieldCount1);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount1);
            if (typeError) {
                end = true;
                break;
            }
        }
        if (Integer.parseInt(productMin.getText().trim()) > Integer.parseInt(productMax.getText().trim())) {
            AlertMessage.errorProduct(10, productMin);
            return;
        }
        if (Integer.parseInt(productStock.getText().trim()) < Integer.parseInt(productMin.getText().trim())) {
            AlertMessage.errorProduct(8, productStock);
            return;
        }
        if (Integer.parseInt(productStock.getText().trim()) > Integer.parseInt(productMax.getText().trim())) {
            AlertMessage.errorProduct(9, productStock);
            return;
        }
        if (Double.parseDouble(productPrice.getText().trim()) < minCost) {
            AlertMessage.errorProduct(6, productPrice);
            return;
        }
        if (assocPartList.isEmpty()) {
            AlertMessage.errorProduct(7, null);
            return;
        }

        saveProduct();
        mainScreen(event);

    }

    /** does the actual saving in method above*/
    private void saveProduct() {
        Product product = new Product(Integer.parseInt(productId.getText().trim()), productName.getText().trim(), Double.parseDouble(productPrice.getText().trim()),
                Integer.parseInt(productStock.getText().trim()), Integer.parseInt(productMin.getText().trim()), Integer.parseInt(productMax.getText().trim()));
        for (Part part : assocPartList) {
            product.addAssociatedPart(part);
        }

        inv.addProduct(product);

    }

    /** returns to main screen*/
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/main_screen.fxml"));
            MainScreenController controller = new MainScreenController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }
    /** next two methods check value and type of text fields */
    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                AlertMessage.errorProduct(1, field);
                return true;
            }
            if (field == productPrice && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorProduct(5, field);
                error = true;
            }
        } catch (NumberFormatException e) {
            error = true;
            AlertMessage.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == productPrice & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        if (field != productPrice & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        return false;

    }
    /**  creates prices column */
    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    }
                    else{
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

}


