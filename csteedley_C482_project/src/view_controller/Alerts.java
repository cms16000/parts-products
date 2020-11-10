package view_controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 *
 * @author CHRISTOPHER STEEDLEY
 */

/** class contains error messages for incorrect data in fields and confirmation for save, cancel, delete.*/
class AlertMessage {

    public static void errorPart(int code, TextField field) {


        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding part");
        alert.setHeaderText("Can't add part");
        switch (code) {
            case 1: {
                alert.setContentText("Text box empty");
                break;
            }
            case 2: {
                alert.setContentText("select In House or Out Sourced");
                break;
            }
            case 3: {
                alert.setContentText("Invalid format");
                break;
            }
            case 4: {
                alert.setContentText("Name Invalid");
                break;
            }
            case 5: {
                alert.setContentText("Value must be greater than 0");
                break;
            }
            case 6: {
                alert.setContentText("Inventory cannot be below min");
                break;
            }
            case 7: {
                alert.setContentText("Inventory cannot above max");
                break;
            }
            case 8: {
                alert.setContentText("Min must be lower than max");
                break;
            }
            case 9: {
                alert.setContentText("machine Id must be number");
                break;
            }
            default: {
                alert.setContentText("Error, Unknown");
                break;
            }
        }
        alert.showAndWait();
    }

    public static void errorProduct(int code, TextField field) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding product");
        alert.setHeaderText("Can't add product");
        switch (code) {
            case 1: {
                alert.setContentText("Text Box Empty");
                break;
            }
            case 2: {
                alert.setContentText("part already associated with product");
                break;
            }
            case 3: {
                alert.setContentText("Invalid format");
                break;
            }
            case 4: {
                alert.setContentText("Name invalid");
                break;
            }
            case 5: {
                alert.setContentText("Value can't be negative");
                break;
            }
            case 6: {
                alert.setContentText("Product cost must be higher than part cost");
                break;
            }
            case 7: {
                alert.setContentText("This product has parts associated, modify and remove those first");
                break;
            }
            case 8: {
                alert.setContentText("Inventor can't be below min");
                break;
            }
            case 9: {
                alert.setContentText("Inventory cant be above max");
                break;
            }
            case 10: {
                alert.setContentText("Min must be lower than max");
                break;
            }
            default: {
                alert.setContentText("error, unknown");
                break;
            }
        }
        alert.showAndWait();
    }


    public static boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Delete:  " + name + "?");
        alert.setContentText("Ok to confirm");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel?");
        alert.setContentText("Ok to confirm");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void infoWindow(int code, String name) {
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

}
