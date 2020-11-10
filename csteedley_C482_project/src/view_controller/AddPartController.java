package view_controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *
 * @author CHRISTOPHER STEEDLEY
 */

public class AddPartController implements Initializable {
        Inventory  inv;



        public AddPartController(Inventory inv) {
                this.inv = inv;
        }

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
        private Label machineIdLabel;

        @FXML
        private TextField min;

        @FXML
        private Button cancelAddPartButton;

        @FXML
        private Button saveButton;



        @Override
        public void initialize(URL url, ResourceBundle rb) {
                generateId();
        }

/** generates random number to be part id, if id already equal to another part id it randomly picks another one,
 * if this was dealing with a lot of parts it would be better to do an incremental id to ensure it doesn't run many times
 * to assign an ID */
        private void generateId() {
                boolean match;
                Random randomNum = new Random();
                Integer num = randomNum.nextInt(1000);

                if (inv.partListSize() == 0) {
                        id.setText(num.toString());

                }
                if (inv.partListSize() == 1000) {
                        AlertMessage.errorPart(3, null);
                } else {
                        match = verifyIfTaken(num);

                        if (!match) {
                                id.setText(num.toString());
                        } else {
                                generateId();
                        }
                }
        }
/**  verifies if id taken */
        private boolean verifyIfTaken(Integer num) {
                Part match = inv.lookUpPart(num);
                return match != null;
        }

        /** set in house label to machine id */
        @FXML
        private void addInHouseRadio(MouseEvent event) {
                machineIdLabel.setText("Machine ID");

        }
        /** set outsourced label to company name*/
        @FXML
        private void addOutSourcedRadio(MouseEvent event) {
                machineIdLabel.setText("Company Name");

        }
        /** disable mouse on id textfields*/
        @FXML
        private void idDisabled(MouseEvent event) {
        }
        /**cancel button runs method main screen to return to main screen */
        @FXML
        private void cancelAddPart(MouseEvent event) {
                boolean cancel = AlertMessage.cancel();
                if (cancel) {
                        mainScreen(event);
                }
        }

        /** mainly for verifying that requirements are met before saving part*/
        @FXML
        private void saveAddPart(MouseEvent event) {
                boolean end = false;
                TextField[] fieldCount = {stock, price, min, max};
                if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
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
                        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
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
                        } else if (machineId.getText().trim().isEmpty() || machineId.getText().trim().toLowerCase().equals("company name")) {
                                AlertMessage.errorPart(3, machineId);
                                return;

                        } else if (inHouseRadio.isSelected() && !machineId.getText().trim().matches("[0-9]*")) {
                                AlertMessage.errorPart(9, machineId);
                                return;
                        } else if (inHouseRadio.isSelected()) {
                                addInHouse();

                        } else if (outSourcedRadio.isSelected()) {
                                addOutSourced();

                        }

                } else {
                        AlertMessage.errorPart(2, null);
                        return;

                }
                mainScreen(event);
        }
        /** saves new in house part*/
        private void addInHouse() {
                inv.addPart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                        Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                        Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), (Integer.parseInt(machineId.getText().trim()))));

        }
        /** saves new outsourced part*/
        private void addOutSourced() {
                inv.addPart(new Outsourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                        Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                        Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), machineId.getText().trim()));

        }

        /**return to main screen method */
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
        /** next two methods check values and types to make sure correct data in correct field */
        private boolean checkValue(TextField field) {
                boolean error = false;
                try {
                        if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                                AlertMessage.errorPart(1, field);
                                return true;
                        }
                        if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
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




}