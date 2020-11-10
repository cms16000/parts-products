package view_controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author CHRISTOPHER STEEDLEY
 */


/** FUTURE BUILDS: In the future I might include better search features such as by price, outsourced or in house, and search by stock
 * to see if we need to reorder parts/products. Also a sales table would be good to see which products are selling the most
 * and which parts/products we should keep more of on hand.  */
public class MainScreenController implements Initializable {


    @FXML
    private TextField partSearchBox;

    @FXML
    private TableView<Part> mainPartsTable;

    @FXML
    private TextField productSearchBox;

    @FXML
    private TableView<Product> productsTable;



    Inventory inv;

    public ObservableList<Part> partInventory = FXCollections.observableArrayList();
    public ObservableList<Product> productInventory = FXCollections.observableArrayList();
    public ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    public ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();
    public  ObservableList<Part> searchIdHelp= FXCollections.observableArrayList();
    public  ObservableList<Product> searchPrIdHelp= FXCollections.observableArrayList();


    public MainScreenController(Inventory inv) {
        this.inv = inv;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartsTable();
        generateProductsTable();
    }
    /** loads data to parts table*/
    private void generatePartsTable() {
        partInventory.setAll(inv.getAllParts());

        TableColumn<Part, Double> costCol = formatPricePart();
        mainPartsTable.getColumns().addAll(costCol);

        mainPartsTable.setItems(partInventory);
        mainPartsTable.refresh();
    }
    /**loads data to products table */
    private void generateProductsTable() {
        productInventory.setAll(inv.getAllProducts());

        TableColumn<Product, Double> costCol = formatPriceProduct();
        productsTable.getColumns().addAll(costCol);

        productsTable.setItems(productInventory);
        productsTable.refresh();
    }
    /** as it says exits program */
    @FXML
    private void exitProgramButton(MouseEvent event) {
        Platform.exit();
    }

    /** see parts search function below, this one was challenging*/
    @FXML
    void searchProducts(KeyEvent event) {

            if (event.getCode().toString().equals("ENTER")) {
                if (!productSearchBox.getText().trim().isEmpty()) {
                    productInventorySearch.clear();
                    for (Product p : inv.getAllProducts()) {
                        if (p.getProductName().contains(productSearchBox.getText().trim())) {
                            productInventorySearch.add(p);
                        }
                    }
                   productsTable.setItems(productInventorySearch);
                    productsTable.refresh();
                }

                String IdSearch = productSearchBox.getText();
                if (IdSearch.matches("[0-9]*")) {

                    for (Product p : inv.getAllProducts()) {
                        if(IdSearch.equals("")) break;
                        if (p.getProductId() == Integer.parseInt(IdSearch)) {

                            productInventorySearch.add(p);
                        }

                    }
                }
            }
            if (!productInventorySearch.isEmpty()) {
                searchPrIdHelp.clear();
                for (Product x : productInventorySearch){
                    if (!searchPrIdHelp.contains(x)){
                        searchPrIdHelp.add(x);}
                }
                productsTable.setItems(searchPrIdHelp);
                productsTable.refresh();
            }


            if (productSearchBox.getText().trim().isBlank()) {
                productsTable.setItems(productInventory);

            }


            if (event.getCode().toString().equals("ENTER")) {
                if (Bindings.isEmpty(productsTable.getItems()).get()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Nothing Found");
                    alert.setContentText("try Searching for something else");
                    alert.showAndWait();
                }

            }

        }

    /** Probably put way to much time into this and Im sure there is a better way, but searching for name and Id
     * was difficult. The solution I came up with was to first search by name and then check if search field was numbers
     * using a regular expression. If it was I would run it against the part id list and add it as well, then to prevent
     * duplicates I would create a second list and add them to second list using for loop and checking if new list had it
     * already, then Add the new list to the search table. However due to parsing the SearchBox if enter was pressed while
     * nothing was in search box it would create an error, not crash the program but I got around that by add a
     * if search box = "" then break. Overall this was by far the most difficult to implement for me, but i did learn a
     * lot not taught in course about different commands for future projects*/

    @FXML
    void searchParts(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            if (!partSearchBox.getText().trim().isEmpty()) {
                partsInventorySearch.clear();
                for (Part p : inv.getAllParts()) {
                    if (p.getName().contains(partSearchBox.getText().trim())) {
                        partsInventorySearch.add(p);
                    }
                }
                mainPartsTable.setItems(partsInventorySearch);

            }

            String IdSearch = partSearchBox.getText();
            if (IdSearch.matches("[0-9]*")) {

                for (Part p : Inventory.getAllParts()) {
                    if(IdSearch.equals("")) break;
                    if (p.getId() == Integer.parseInt(IdSearch)){

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
            mainPartsTable.setItems(searchIdHelp);
            mainPartsTable.refresh();
        }


        if (partSearchBox.getText().trim().isBlank()) {
            mainPartsTable.setItems(partInventory);

        }


        if (event.getCode().toString().equals("ENTER")) {
            if (Bindings.isEmpty(mainPartsTable.getItems()).get()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Nothing Found");
                alert.setContentText("try Searching for something else");
                alert.showAndWait();
            }

        }

    }


/** loads the add part gui */

    @FXML
        private void addPart(MouseEvent event
        ) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/Add_part.fxml"));
                AddPartController controller = new AddPartController(inv);

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
/** loads the modify part gui */
        @FXML
        private void modifyPart(MouseEvent event) {
            try {
                Part partSelected = mainPartsTable.getSelectionModel().getSelectedItem();
                if (partInventory.isEmpty()) {
                    errorWindow(1);
                    return;
                }
                if (!productInventory.isEmpty() && partSelected == null) {
                    errorWindow(2);
                    return;

                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/ModifyPart.fxml"));
                    ModifyPartController controller = new ModifyPartController(inv, partSelected);
                    loader.setController(controller);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
            } catch (IOException e) {

            }
        }
    /** deletes a part from table*/
        @FXML
        private void deletePart(MouseEvent event
        ) {
            Part deletePart = mainPartsTable.getSelectionModel().getSelectedItem();
            if (partInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!partInventory.isEmpty() && deletePart == null) {
                errorWindow(2);
            } else {
                boolean confirm = confirmationWindow(deletePart.getName());
                if (!confirm) {
                    return;
                }
                inv.deletePart(deletePart);
                partInventory.remove(deletePart);
                mainPartsTable.refresh();

            }
        }
    /** deletes a product from table*/
        @FXML
        private void deleteProduct(MouseEvent event
        ) {
            boolean deleted = false;
            Product removeProduct = productsTable.getSelectionModel().getSelectedItem();
            if (productInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!productInventory.isEmpty() && removeProduct == null) {
                errorWindow(2);
                return;
            }


            if (removeProduct.getPartsListSize() > 0) {
                errorWindow(3);
                return;
            }

            if (removeProduct.getPartsListSize() == 0) {
                boolean confirm = confirmDelete(removeProduct.getProductName());
                if (!confirm) {
                    return;
                }
            }


            else {
                if (removeProduct != null) {
                    infoWindow(1, removeProduct.getProductName());
                    deleted = true;
                    if (deleted) {
                        return;

                    } else {
                        infoWindow(2, "");
                    }

                }
            }
            Inventory.deleteProduct(removeProduct.getProductId());
            productInventory.remove(removeProduct);
            productsTable.setItems(productInventory);
            productsTable.refresh();
        }
    /** loads modify product gui*/
        @FXML
        private void modifyProduct(MouseEvent event
        ) {
            try {
                Product productSelected = productsTable.getSelectionModel().getSelectedItem();
                if (productInventory.isEmpty()) {
                    errorWindow(1);
                    return;
                }
                if (!productInventory.isEmpty() && productSelected == null) {
                    errorWindow(2);
                    return;

                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/ModifyProduct.fxml"));
                    ModifyProductsController controller = new ModifyProductsController(inv, productSelected);

                    loader.setController(controller);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
            } catch (IOException e) {

            }
        }
    /** loads add product gui */
        @FXML
        private void addProduct(MouseEvent event
        ) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/AddProduct.fxml"));
                AddProductController controller = new AddProductController(inv);

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
    /** next few methods are just for error handling when selecting incorrect type for modify, and cofirm windows for delete */
        private void errorWindow(int code) {
            if (code == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty table");
                alert.setContentText("nothing to select");
                alert.showAndWait();
            }
            if (code == 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("no selection");
                alert.setContentText("You must select an item");
                alert.showAndWait();
            }

            if (code == 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Parts Associated");
                alert.setContentText("must delete parts associated with item");
                alert.showAndWait();
            }

        }

        private boolean confirmationWindow(String name) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete part");
            alert.setHeaderText("Are you sure you want to delete: " + name);
            alert.setContentText("ok to confirm");

            Optional<ButtonType> result = alert.showAndWait();
            return result.get() == ButtonType.OK;
        }

        private boolean confirmDelete(String name) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete product");
            alert.setHeaderText("Are you sure you want to delete: " + name );
            alert.setContentText("ok to confirm");

            Optional<ButtonType> result = alert.showAndWait();
            return result.get() == ButtonType.OK;
        }



        private void infoWindow(int code, String name) {
            if (code != 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmed");
                alert.setHeaderText(null);
                alert.setContentText(name + " has been deleted!");

                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("There was an error!");
            }
        }
    /** creates the price column for both the products table and parts table */
        private <T> TableColumn<T, Double> formatPriceProduct() {
            TableColumn<T, Double> costCol = new TableColumn("Price");
            costCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            costCol.setCellFactory((TableColumn<T, Double> column) -> {
                return new TableCell<T, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        if (!empty) {
                            setText("$" + String.format("%10.2f", item));
                        } else {
                            setText("");
                        }
                    }
                };
            });
            return costCol;
        }

    private <T> TableColumn<T, Double> formatPricePart() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    } else {
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }
    }

