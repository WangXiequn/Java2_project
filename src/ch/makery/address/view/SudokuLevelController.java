package ch.makery.address.view;
import ch.makery.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SudokuLevelController {
    @FXML
    private Button three;
    @FXML
    private Button four;
    @FXML
    private Button five;

    public static int Dimension=3;

    private MainApp mainApp;


    public SudokuLevelController() {
    }
    @FXML
    private void initialize() {
        // Initialize the  table with the Button.

    }

    public void Three(){
        Dimension=3;
        mainApp.mainpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SudokuOverview.fxml"));
            BorderPane SudokuOverview = (BorderPane) loader.load();
            SudokuOverviewController controller=loader.getController();
            controller.setMainApp(mainApp);
            // Show the scene containing the root layout.
            mainApp.mainpane.setCenter(SudokuOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Four(){
        Dimension=4;
        mainApp.mainpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SudokuOverview.fxml"));
            BorderPane SudokuOverview = (BorderPane) loader.load();
            SudokuOverviewController controller=loader.getController();
            controller.setMainApp(mainApp);
            // Show the scene containing the root layout.
            mainApp.mainpane.setCenter(SudokuOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Five(){
        Dimension=5;
        mainApp.mainpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SudokuOverview.fxml"));
            BorderPane SudokuOverview = (BorderPane) loader.load();
            SudokuOverviewController controller=loader.getController();
            controller.setMainApp(mainApp);
            // Show the scene containing the root layout.
            mainApp.mainpane.setCenter(SudokuOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
