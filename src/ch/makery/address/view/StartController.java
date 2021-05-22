package ch.makery.address.view;
import ch.makery.address.MainApp;
import ch.makery.address.model.MagicSquare;
import ch.makery.address.model.SudokuGenerator;
import ch.makery.address.model.WrapperClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

public class StartController {
    @FXML
    private Button NewSukudo;
    @FXML
    private Button NewMagicSquare;
    @FXML
    private Button LoadGame;
    @FXML
    private Button ChallengeSudoku;
    @FXML
    private Button ChallengeMagicSquare;
    @FXML
    private Button Exit;
    @FXML
    private ImageView logo;
    @FXML
    private Label logoText;

    @FXML
    private BorderPane startpane;
    @FXML
    private AnchorPane leftPartContainer;

    private MainApp mainApp;

    public StartController() {
    }
    @FXML
    private void initialize() {
        // Initialize the  table with the Button.
        Image logo1 = new Image("ch/makery/address/icons/logo.png");
        logo.setImage(logo1);

        //css
        startpane.getStylesheets().add("/stylesheets/mainMenuSceneStyle.css");
        leftPartContainer.getStyleClass().add("left-part");
        logoText.getStyleClass().add("logo-text");
        logo.getStyleClass().add("logo");

        buttonstylesetter(NewSukudo);
        Image newGameButtonIcon = new Image("ch/makery/address/icons/new-game.png");
        ImageView newGameButtonIconView = new ImageView(newGameButtonIcon);
        NewSukudo.setGraphic(newGameButtonIconView);

        buttonstylesetter(NewMagicSquare);
        Image newmagicsquareIcon = new Image("ch/makery/address/icons/new-magicsquare.png");
        ImageView newmagicsquareIconView = new ImageView(newmagicsquareIcon);
        NewMagicSquare.setGraphic(newmagicsquareIconView);

        buttonstylesetter(LoadGame);
        Image loadGameButtonIcon = new Image("ch/makery/address/icons/load-game.png");
        ImageView laodGameIconView = new ImageView(loadGameButtonIcon);
        LoadGame.setGraphic(laodGameIconView);

        buttonstylesetter(ChallengeSudoku);
        Image checkSudokuIcon = new Image("ch/makery/address/icons/challengeMagicSquare.png");
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        ChallengeSudoku.setGraphic(checkSudokuIconView);

        buttonstylesetter(ChallengeMagicSquare);
        Image ChallengeMagicSquareIcon = new Image("ch/makery/address/icons/challengeMagicSquare.png");
        ImageView ChallengeMagicSquareIconView = new ImageView(ChallengeMagicSquareIcon);
        ChallengeMagicSquare.setGraphic(ChallengeMagicSquareIconView);

        buttonstylesetter(Exit);
        Image exitButtonIcon = new Image("ch/makery/address/icons/exit.png");
        ImageView exitButtonIconView = new ImageView(exitButtonIcon);
        Exit.setGraphic(exitButtonIconView);
    }

    public void GotoLevelpane(){
        startpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            mainApp.PlayingMode="NEW_GAME_MODE";
            SudokuLevelController.category=0;
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
        mainApp.PlayingMode="NEW_GAME_MODE";
        SudokuLevelController.category=1;
//        TextInputDialog dialog = new TextInputDialog(null);
//        dialog.setTitle("Immediate Dialog");
//        dialog.setHeaderText("Welcome to the MagicSquare, you could only enter Number between 3 and 20");
//        dialog.setContentText("Please enter your Dimension:");
//
//// Traditional way to get the response value.
//        Optional<String> result= dialog.showAndWait();;
//        if (result.isPresent() && validInput(result.get())){
////            System.out.println(result.get().length());
////            System.out.println("Dimension: " + result);
//            MagicSquare.Dimension=Integer.parseInt(result.get());
//            MagicSquarePaneController.Dimension=Integer.parseInt(result.get());
//            MagicSquarePaneController.user=new int[MagicSquare.Dimension][MagicSquare.Dimension];
//            MagicSquarePaneController.computerSolution=new Integer[MagicSquare.Dimension][MagicSquare.Dimension];
//            MagicSquarePaneController.loadedGameSudoku=new Integer[MagicSquare.Dimension][MagicSquare.Dimension];
//            MagicSquarePaneController.MagicSquareCells=new TextField[MagicSquare.Dimension+2][MagicSquare.Dimension+2];
//            generator.MakeMagicsquare(MagicSquare.Dimension);
//
//            mainApp.mainpane.setCenter(null);
//            try {
//                // Load root layout from fxml file.
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(MainApp.class.getResource("view/MagicSquarePane.fxml"));
//                BorderPane MagicSquarePane = (BorderPane) loader.load();
//                MagicSquarePaneController controller=loader.getController();
//                controller.setMainApp(mainApp);
//                // Show the scene containing the root layout.
//                mainApp.mainpane.setCenter(MagicSquarePane);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }else {
//            dialog.close();
//        }
        startpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            //mainApp.PlayingMode="NEW_GAME_MODE";

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

    private boolean validInput(String s){
        int a= Integer.parseInt(s);
        if (a<3 || a>20){
            return false;
        }
        return true;
    }

    @FXML
    private void ChallengeSudoku(){
        startpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            mainApp.PlayingMode="CHALLENGE_MODE";
            SudokuLevelController.category=0;
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

    public void ChallengeMagicSquare(){
        mainApp.PlayingMode="CHALLENGE_MODE";
        SudokuLevelController.category=1;
//        TextInputDialog dialog = new TextInputDialog(null);
//        dialog.setTitle("Immediate Dialog");
//        dialog.setHeaderText("Welcome to the MagicSquare, you could only enter Number between 3 and 20");
//        dialog.setContentText("Please enter your Dimension:");
//
//// Traditional way to get the response value.
//        Optional<String> result= dialog.showAndWait();;
//        if (result.isPresent() && validInput(result.get())){
//
//            MagicSquare.Dimension=Integer.parseInt(result.get());
//            MagicSquarePaneController.Dimension=Integer.parseInt(result.get());
//            MagicSquarePaneController.user=new int[MagicSquare.Dimension][MagicSquare.Dimension];
//            MagicSquarePaneController.computerSolution=new Integer[MagicSquare.Dimension][MagicSquare.Dimension];
//            MagicSquarePaneController.loadedGameSudoku=new Integer[MagicSquare.Dimension][MagicSquare.Dimension];
//            MagicSquarePaneController.MagicSquareCells=new TextField[MagicSquare.Dimension+2][MagicSquare.Dimension+2];
//            generator.MakeMagicsquare(MagicSquare.Dimension,0);
//
//            mainApp.mainpane.setCenter(null);
//            try {
//                // Load root layout from fxml file.
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(MainApp.class.getResource("view/MagicSquarePane.fxml"));
//                BorderPane MagicSquarePane = (BorderPane) loader.load();
//                MagicSquarePaneController controller=loader.getController();
//                controller.setMainApp(mainApp);
//                // Show the scene containing the root layout.
//                mainApp.mainpane.setCenter(MagicSquarePane);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }else {
//            dialog.close();
//        }
        startpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            //mainApp.PlayingMode="NEW_GAME_MODE";
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

    public static void buttonstylesetter(Button button){
        button.getStyleClass().add("icon-text-button");
        button.getStyleClass().add("button-icon_text");
        button.getStyleClass().add("button-icon_text--white");
        button.setAlignment(Pos.CENTER_LEFT);
    }


    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.primaryStage);

        if (file != null) {
            loadGameDataFromFile(file);
        }
    }

    public void loadGameDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(WrapperClass.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            WrapperClass wrapper = (WrapperClass) um.unmarshal(file);
            if(wrapper.getIsMagicSquare()){//magicsquare

                MagicSquare.Dimension=wrapper.getDataBefore().length;
                MagicSquarePaneController.Dimension=wrapper.getDataBefore().length;
                MagicSquarePaneController.user=new int[MagicSquare.Dimension][MagicSquare.Dimension];
                MagicSquarePaneController.reset=new int[MagicSquare.Dimension][MagicSquare.Dimension];
                MagicSquarePaneController.MagicSquareCells=new TextField[MagicSquare.Dimension+2][MagicSquare.Dimension+2];

                MagicSquarePaneController.setUser(wrapper.getDataUser());
                MagicSquarePaneController.setReset(wrapper.getDataBefore());
                MagicSquare.setMagicsquare(wrapper.getDataBefore());
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
            } else {//sudoku
                mainApp.PlayingMode="NEW_GAME_MODE";
                startpane.setCenter(null);
                SudokuOverviewController.Dimension = wrapper.getDataBefore().length;
                SudokuOverviewController.setParameters(wrapper.getDataBefore(),wrapper.getDataSolution(),wrapper.getDataBefore().length);

                SudokuGenerator.setSudoku(wrapper.getDataBefore());
                try {
                    //Load root layout from fxml file.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/SudokuOverview.fxml"));
                    BorderPane SudokuOverview = (BorderPane) loader.load();
                    SudokuOverviewController controller=loader.getController();
                    controller.setMainApp(mainApp);
//                    SudokuOverviewController.Dimension = wrapper.getDataBefore().length;
//                    SudokuOverviewController.setParameters(wrapper.getDataBefore(),wrapper.getDataSolution(),wrapper.getDataBefore().length);

                    SudokuGenerator.setSudoku(wrapper.getDataBefore());
                    // Show the scene containing the root layout.
                    mainApp.mainpane.setCenter(SudokuOverview);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < wrapper.getDataBefore().length; i++) {
                    for (int j=0;j<wrapper.getDataBefore().length;j++){
                        SudokuOverviewController.user[i][j]=wrapper.getDataUser()[i][j];
                        SudokuOverviewController.sudokuCells[i][j].setText(String.valueOf(wrapper.getDataUser()[i][j]));;
                    }
                }



            }

        } catch (Exception e) { // catches ANY exception
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    @FXML
    private void exit(){
        System.exit(0);
    }
}
