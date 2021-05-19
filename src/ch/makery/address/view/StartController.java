package ch.makery.address.view;
import ch.makery.address.MainApp;
import ch.makery.address.model.MagicSquare;
import ch.makery.address.model.SudokuGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

import static ch.makery.address.view.MagicSquarePaneController.generator;

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

    public void GoToMagicSquare(){
        TextInputDialog dialog = new TextInputDialog(null);
        dialog.setTitle("Immediate Dialog");
        dialog.setHeaderText("Welcome to the MagicSquare, you could only enter Number between 3 and 20");
        dialog.setContentText("Please enter your Dimension:");

// Traditional way to get the response value.
        Optional<String> result= dialog.showAndWait();;
        if (result.isPresent() && validInput(result.get())){
//            System.out.println(result.get().length());
//            System.out.println("Dimension: " + result);
            MagicSquare.Dimension=Integer.parseInt(result.get());
            MagicSquarePaneController.Dimension=Integer.parseInt(result.get());
            MagicSquarePaneController.user=new int[MagicSquare.Dimension][MagicSquare.Dimension];
            MagicSquarePaneController.computerSolution=new Integer[MagicSquare.Dimension][MagicSquare.Dimension];
            MagicSquarePaneController.loadedGameSudoku=new Integer[MagicSquare.Dimension][MagicSquare.Dimension];
            MagicSquarePaneController.MagicSquareCells=new TextField[MagicSquare.Dimension+2][MagicSquare.Dimension+2];
            generator.MakeMagicsquare(MagicSquare.Dimension);

            mainApp.mainpane.setCenter(null);
            try {
                // Load root layout from fxml file.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/MagicSquarePane.fxml"));
                BorderPane MagicSquarePane = (BorderPane) loader.load();
                MagicSquarePaneController controller=loader.getController();
                controller.setMainApp(mainApp);
                // Show the scene containing the root layout.
                mainApp.mainpane.setCenter(MagicSquarePane);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            dialog.close();
        }

    }

    private boolean validInput(String s){
        int a= Integer.parseInt(s);
        if (a<3 || a>20){
            return false;
        }
        return true;
    }

}
