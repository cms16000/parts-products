package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.InHouse;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author CHRISTOPHER STEEDLEY
 */

public class ModifyPartController implements Initializable {
    Inventory inv;
    Part part;

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private ToggleGroup addPartToggle;


    @FXML
    private RadioButton outSourcedRadio;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField stock;

    @FXML
    private TextField price;

    @FXML
    private TextField max;

    @FXML
    private TextField machineId;

    @FXML
    private TextField min;

    @FXML
    private Label machineIdLabel;

    @FXML
    private Button addProductSaveButton;

    /** nearly identical to add part class, except for loading data from selected item into the modify part gui text
     * boxes */
    public ModifyPartController(Inventory inv, Part part) {
        this.inv = inv;
        this.part = part;
    }



    @Override
    public void initialize(URL location, ResourceBundle rb) {
        setTables();

    }

    private void setTables() {

        if (part instanceof InHouse) {

            InHouse part1 = (InHouse) part;
            inHouseRadio.setSelected(true);
            machineIdLabel.setText("Machine ID");
            this.name.setText(part1.getName());
            this.id.setText((Integer.toString(part1.getId())));
            this.stock.setText((Integer.toString(part1.getStock())));
            this.price.setText((Double.toString(part1.getPrice())));
            this.min.setText((Integer.toString(part1.getMin())));
            this.max.setText((Integer.toString(part1.getMax())));
            this.machineId.setText((Integer.toString(part1.getMachineId())));

        }

        if (part instanceof Outsourced) {

            Outsourced part2 = (Outsourced) part;
            outSourcedRadio.setSelected(true);
            machineIdLabel.setText("Company Name");
            this.name.setText(part2.getName());
            this.id.setText((Integer.toString(part2.getId())));
            this.stock.setText((Integer.toString(part2.getStock())));
            this.price.setText((Double.toString(part2.getPrice())));
            this.min.setText((Integer.toString(part2.getMin())));
            this.max.setText((Integer.toString(part2.getMax())));
            this.machineId.setText(part2.getCompanyName());
        }
    }


    @FXML
    private void addInHouseRadio(MouseEvent event
    ) {
        machineIdLabel.setText("Machine ID");

    }

    @FXML
    private void addOutSourcedRadio(MouseEvent event
    ) {
        machineIdLabel.setText("Company Name");

    }

    @FXML
    private void idDisabled(MouseEvent event) {
    }

    @FXML
    private void cancelModPart(MouseEvent event
    ) {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(event);
        } else { return;
        }
    }

    @FXML
    private void saveModPart(MouseEvent event
    ) {
        boolean end = false;
        TextField[] fieldCount = {stock, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
            for (TextField textField : fieldCount) {
                boolean valueError = checkValue(textField);
                if (valueError) {
                    end = true;
                    break;
                }
                boolean typeError = checkType(textField);
                if (typeError) {
                    end = true;
                    break;
                }
            }
            if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("name")) {
                AlertMessage.errorPart(4, name);
                return;
            }
            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.errorPart(8, min);
                return;
            }
            if (Integer.parseInt(stock.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                AlertMessage.errorPart(6, stock);
                return;
            }
            if (Integer.parseInt(stock.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.errorPart(7, stock);
                return;
            }

            if (end) {
                return;
            } else if (outSourcedRadio.isSelected() && machineId.getText().trim().isEmpty()) {
                AlertMessage.errorPart(1, machineId);
                return;
            } else if (inHouseRadio.isSelected() && !machineId.getText().matches("[0-9]*")) {
                AlertMessage.errorPart(9, machineId);
                return;

            } else if (inHouseRadio.isSelected() & part instanceof InHouse) {
                updateItemInHouse();

            } else if (inHouseRadio.isSelected() & part instanceof Outsourced) {
                updateItemInHouse();
            } else if (outSourcedRadio.isSelected() & part instanceof Outsourced) {
                updateItemOutSourced();
            } else if (outSourcedRadio.isSelected() & part instanceof InHouse) {
                updateItemOutSourced();
            }

        } else {
            AlertMessage.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }

    private void updateItemInHouse() {
        Inventory.updatePart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), Integer.parseInt(machineId.getText().trim())));
    }

    private void updateItemOutSourced() {
        Inventory.updatePart(new Outsourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), machineId.getText().trim()));
    }


    private void mainScreen(MouseEvent event) {
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
                AlertMessage.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) <= 0.0) {
                AlertMessage.errorPart(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            AlertMessage.errorPart(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorPart(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorPart(3, field);
            return true;
        }
        return false;

    }

    private boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
