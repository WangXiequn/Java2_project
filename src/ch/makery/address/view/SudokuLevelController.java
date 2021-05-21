package ch.makery.address.view;
import ch.makery.address.MainApp;
import ch.makery.address.model.SudokuGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

import static ch.makery.address.view.SudokuOverviewController.*;


public class SudokuLevelController {
    @FXML
    private Button three;
    @FXML
    private Button four;
    @FXML
    private Button five;
    @FXML
    private MenuButton chooseDimension;

    public static int Dimension=3;

    private MainApp mainApp;

    public int category=0;



    public SudokuLevelController() {
    }
    @FXML
    private void initialize() {
        // Initialize the  table with the Button.
        for (int i=3;i<=20;i++){
            MenuItem menuItem=new MenuItem("Dimension: "+i);
            chooseDimension.getItems().add(menuItem);
        }
        StartController.buttonstylesetter(three);
        StartController.buttonstylesetter(four);
        StartController.buttonstylesetter(five);
        if (category==1){//magic Square
            three.setVisible(false);
            four.setVisible(false);
            five.setVisible(false);
        }else {
            chooseDimension.setVisible(false);
        }
    }

    public void Three(){
        Dimension=3;
        SudokuOverviewController.Dimension=Dimension*Dimension;
        SudokuOverviewController.user=new int[9][9];
        SudokuOverviewController.computerSolution=new Integer[9][9];
        SudokuOverviewController.loadedGameSudoku=new Integer[9][9];
        SudokuOverviewController.sudokuCells=new TextField[9][9];

        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            generator.MakeSudoku();
        }else {
            generator.MakeSudoku(SudokuGenerator.EASY);
        }




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
        SudokuOverviewController.Dimension=Dimension*Dimension;
        SudokuOverviewController.user=new int[16][16];
        SudokuOverviewController.computerSolution=new Integer[16][16];
        SudokuOverviewController.loadedGameSudoku=new Integer[16][16];
        SudokuOverviewController.sudokuCells=new TextField[16][16];

        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            generator.MakeSudoku();
        }else {
            generator.MakeSudoku(SudokuGenerator.MEDIUM);
        }


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
        SudokuOverviewController.Dimension=Dimension*Dimension;
        SudokuOverviewController.user=new int[25][25];
        SudokuOverviewController.computerSolution=new Integer[25][25];
//        SudokuOverviewController.markSolution =new Boolean[25][25];
        SudokuOverviewController.loadedGameSudoku=new Integer[25][25];
        SudokuOverviewController.sudokuCells=new TextField[25][25];

        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            generator.MakeSudoku();
        }else {
            generator.MakeSudoku(SudokuGenerator.HARD);
        }


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
