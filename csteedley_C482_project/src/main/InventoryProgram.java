package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 *
 * @author CHRISTOPHER STEEDLEY
 *
 */


/**starts program loads main gui */
public class InventoryProgram extends Application {

    public static void main(String[] args) {launch(args); }

    @Override
    public void start(Stage stage) throws Exception {
        Inventory inv = new Inventory();
        addTestData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view_controller/main_screen.fxml"));
        view_controller.MainScreenController controller = new view_controller.MainScreenController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    /**data just for testing purposes */

    void addTestData(Inventory inv) {
        //Add InHouse Parts
        Part a1 = new InHouse(13, "Part A1", 4.99, 12, 4, 50, 10);
        Part a2 = new InHouse(3, "Part A2", 2.99, 11, 6, 50, 11);
        Part b1 = new InHouse(2, "Part B1", 3.99, 9, 3, 50, 12);
        Inventory.addPart(a1);
        Inventory.addPart(b1);
        Inventory.addPart(a2);
        Inventory.addPart(new InHouse(4, "Part D1", 6.99, 11, 5, 50, 13));
        Inventory.addPart(new InHouse(5, "Part D2", 9.99, 5, 5, 50, 14));
        //Add OutSourced Parts
        Part b2 = new Outsourced(6, "Part B2", 1.99, 10, 5, 50, "wgu");
        Part c1 = new Outsourced(7, "Part C1", 3.99, 9, 5, 50, "wgu");
        Part c2= new Outsourced(8, "Part C2", 3.99, 10, 5, 50, "construct");
        Inventory.addPart(b2);
        Inventory.addPart(c1);
        Inventory.addPart(c2);
        Inventory.addPart(new Outsourced(9, "Part E1", 0.99, 10, 5, 50, "construct"));
        //Add allProducts
        Product prod1 = new Product(13, "Product 1", 9.99, 40, 5, 50);
        prod1.addAssociatedPart(a1);
        prod1.addAssociatedPart(c1);
        Inventory.addProduct(prod1);
        Product prod2 = new Product(2, "Product 2", 17.99, 29, 5, 50);
        prod2.addAssociatedPart(a2);
        prod2.addAssociatedPart(a1);
        Inventory.addProduct(prod2);
        Product prod3 = new Product(3, "Product 3", 19.99, 50, 5, 50);
        prod3.addAssociatedPart(a2);
        prod3.addAssociatedPart(c2);
        Inventory.addProduct(prod3);
        Product prod4 = new Product(4, "Product 4", 27.99, 10, 5, 50);
        Inventory.addProduct(prod4);
        prod4.addAssociatedPart(b1);
        Inventory.addProduct(new Product(5, "Product 5", 23.99, 7, 5, 50));

    }

}

