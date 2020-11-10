package view_controller;


import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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



/** nearly identical to add product class, except for loading data from selected item into the modify product gui text
 * boxes */
public class ModifyProductsController implements Initializable {
    Inventory inv;
    Product product;

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
    private TextField search;

    @FXML
    private TableView<Part> partSearchTable;

    @FXML
    private TableColumn<?, ?> partIDColumn;

    @FXML
    private TableColumn<?, ?> partNameColumn;

    @FXML
    private TableColumn<?, ?> partCountColumn;

    @FXML
    private Button addProductAddButton;

    @FXML
    private Button removeCurrentPart;

    @FXML
    private Button addProductCancelButton;

    @FXML
    private TableView<Part> assocPartsTable;

    @FXML
    private TableColumn<?, ?> currentPartIDColumn;

    @FXML
    private TableColumn<?, ?> currentPartNameColumn;

    @FXML
    private TableColumn<?, ?> currentPartCountColumn;

    @FXML
    private Button addProductSaveButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addToSearchTable();
        setData();
    }

    public ModifyProductsController(Inventory inv, Product product) {
        this.product = product;
        this.inv = inv;
    }



    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    public ObservableList<Part>  searchIdHelp = FXCollections.observableArrayList();


    private void addToSearchTable() {
        partsInventory.setAll(inv.getAllParts());
        TableColumn<Part, Double> costCol = formatPrice();
        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }


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

    @FXML
    private void deletePart(MouseEvent event) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = confirmationWindow(removePart.getName());
            if (remove) {
                deleted = product.removeAssociatedPart(removePart.getId());
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

    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart == null) {
            return;
        } else {
            int id = addPart.getId();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getId() == id) {
                    AlertMessage.errorProduct(2, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);
            }
            assocPartsTable.setItems(assocPartList);
        }
    }

    @FXML
    private void cancelAddProduct(MouseEvent event) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    @FXML
    private void saveAddProduct(MouseEvent event) {
        boolean end = false;
        TextField[] fieldCount = {productStock, productPrice, productMin, productMax};
        double minCost = 0;
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (productName.getText().trim().isEmpty() || productName.getText().trim().toLowerCase().equals("part name")) {
            AlertMessage.errorProduct(4, productName);
            return;
        }
        for (int i = 0; i < fieldCount.length; i++) {
            boolean valueError = checkValue(fieldCount[i]);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount[i]);
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

        saveProduct();
        mainScreen(event);

    }

    private void saveProduct() {
        Product product = new Product(Integer.parseInt(productId.getText().trim()), productName.getText().trim(), Double.parseDouble(productPrice.getText().trim()),
                Integer.parseInt(productStock.getText().trim()), Integer.parseInt(productMin.getText().trim()), Integer.parseInt(productMax.getText().trim()));
        for (Part part : assocPartList) {
            product.addAssociatedPart(part);
        }

        inv.updateProduct(product);

    }

    private void setData() {
        for (int i = 0; i < 1000; i++) {
            Part part = product.lookupAssociatedPart(i);
            if (part != null) {
                assocPartList.add(part);
            }
        }

        TableColumn<Part, Double> costCol = formatPrice();
        assocPartsTable.getColumns().addAll(costCol);

        assocPartsTable.setItems(assocPartList);

        this.productName.setText(product.getProductName());
        this.productId.setText((Integer.toString(product.getProductId())));
        this.productStock.setText((Integer.toString(product.getProductStock())));
        this.productPrice.setText((Double.toString(product.getProductPrice())));
        this.productMin.setText((Integer.toString(product.getProductMin())));
        this.productMax.setText((Integer.toString(product.getProductMax())));

    }


    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("sure you want to delete: " + name);
        alert.setContentText("ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

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

    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                AlertMessage.errorProduct(1, field);
                return true;
            }
            if (field == productPrice && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorProduct(5, field);
                error = true;
            }
        } catch (Exception e) {
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

    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
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

