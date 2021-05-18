package ch.makery.address.view;
import ch.makery.address.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class StartController {
    @FXML
    private Button NewSukudo;
    @FXML
    private Button NewMagicSquare;
    @FXML
    private Button LoadGame;
    @FXML
    private Button Exit;
    @FXML
    private ImageView logo;

    @FXML
    private BorderPane startpane;

    private MainApp mainApp;

    public StartController() {
    }
    @FXML
    private void initialize() {
        // Initialize the  table with the Button.
        Image logo1 = new Image("ch/makery/address/view/icons/logo.png");
        logo.setImage(logo1);
    }

    public void GotoLevelpane(){
        startpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            mainApp.PlayingMode="NEW_GAME_MODE";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SudokuLevel.fxml"));
            AnchorPane sudokulevel = (AnchorPane) loader.load();
            SudokuLevelController controller=loader.getController();
            controller.setMainApp(mainApp);
            // Show the scene containing the root layout.
            startpane.setCenter(sudokulevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
