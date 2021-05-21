package ch.makery.address.view;

import ch.makery.address.MainApp;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ch.makery.address.model.*;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class MagicSquarePaneController {
    @FXML
    private Button return0;
    @FXML
    private Button solve;
    @FXML
    private Button pause;
    @FXML
    private Button check;
    @FXML
    private Button save;
    @FXML
    private BorderPane Container;
    @FXML
    private BorderPane Root;
    @FXML
    private AnchorPane rightpane;
    @FXML
    private Label dimension;
    @FXML
    private Label MagicSum;
    @FXML
    private Label Gerneration;//generation
    @FXML
    private Label timerLabel;
    @FXML
    private Label solveTime;
    @FXML
    private Label title;
    @FXML
    private Label dimensionLabel;
    @FXML
    private Label MagicSumLabel;
    @FXML
    private Label GenerationLabel;
    @FXML
    private Label TimerTitleLabel;
    @FXML
    private Label SolveTimeLabel;

    static timer gameTime = new timer();
    public boolean finished;

    private GridPane MagicSquareCellsTextfieldsContainer;
    Task<Integer> task;
    public static int Dimension=9;//9/16/25
    private static Set inputset;
    private static int magicSum;
    public static int gerneration=0;
    boolean listenToChange = false;
    private MainApp mainApp;
    boolean isPause;
    private boolean issolve=false;
    static TextField[][] MagicSquareCells;//when the
    public static int[][] user; //Reads the Sudoku from the user
    public static Integer[][] computerSolution; //Where computer returns the wrong cells

    static Integer[][] loadedGameSudoku;

    static MagicSquare generator = new MagicSquare();
    public matrix.puzzle.MagicSquare bestMagicSquare;

    //swap
    int swap=1;
    String temp="1";
    int x=0;//row
    int y=0;//col

    public MagicSquarePaneController() {
    }

    @FXML
    private void initialize() {
        // Initialize the  table with the Button.
        gameTime.setTimer(timerLabel, 0);
        inputset=new HashSet();// make sure that in the challenge mode the use couldn't input repeat number
        for (int i=0;i<Dimension*Dimension;i++){
            inputset.add(i+1);
        }
        Root.getStylesheets().add("stylesheets/gameSceneStyle.css");
        dimension.setText(Dimension+"x"+Dimension);
        magicSum=(Dimension*(Dimension*Dimension+1)/2);
        MagicSum.setText(String.valueOf(magicSum));
        initMagicSquareBlock();

        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            save.setVisible(false);
        }

        //ccs
        buttonstylesetter(return0);
        buttonstylesetter(solve);
        buttonstylesetter(check);
        buttonstylesetter(save);
        title.getStyleClass().add("text");
        title.getStyleClass().add("text--headline");
        dimension.getStyleClass().add("text");
        dimension.getStyleClass().add("text--headline");
        MagicSum.getStyleClass().add("text");
        MagicSum.getStyleClass().add("text--headline");
        Gerneration.getStyleClass().add("text");
        Gerneration.getStyleClass().add("text--headline");
        timerLabel.getStyleClass().add("text");
        timerLabel.getStyleClass().add("text--headline");
        solveTime.getStyleClass().add("text");
        solveTime.getStyleClass().add("text--headline");
        dimensionLabel.getStyleClass().add("text");
        dimensionLabel.getStyleClass().add("text--headline");
        MagicSumLabel.getStyleClass().add("text");
        MagicSumLabel.getStyleClass().add("text--headline");
        GenerationLabel.getStyleClass().add("text");
        GenerationLabel.getStyleClass().add("text--headline");
        TimerTitleLabel.getStyleClass().add("text");
        TimerTitleLabel.getStyleClass().add("text--headline");
        SolveTimeLabel.getStyleClass().add("text");
        SolveTimeLabel.getStyleClass().add("text--headline");

        rightpane.getStyleClass().add("toolbar");
        gameTime.start();
    }
    @FXML
    public void setPause(){
       if (issolve){
           isPause = !isPause;
           if (isPause){
               task.cancel();
           }
           }
       }

    private void initMagicSquareBlock() {
        //Sudoku card layout
        BorderPane MagicSquareCellsContainer = new BorderPane();
        MagicSquareCellsContainer.setPadding(new Insets(7));
        MagicSquareCellsContainer.getStyleClass().add("card");

        //Cells container layout
        MagicSquareCellsTextfieldsContainer = new GridPane();
        MagicSquareCellsTextfieldsContainer.getStyleClass().add("cells-container");
        MagicSquareCellsContainer.setCenter(MagicSquareCellsTextfieldsContainer);

        //Unreal menu to override default menu
        ContextMenu hiddenMenu = new ContextMenu();
        hiddenMenu.hide();
        int rowCounter, columnCounter;

        //<editor-fold defaultstate="collapsed" desc="Sudoku Cells">
        for (rowCounter = 0; rowCounter < Dimension+2; rowCounter++) {
            for (columnCounter = 0; columnCounter < Dimension+2; columnCounter++) {
                //Create cells and positioning hem
                MagicSquareCells[rowCounter][columnCounter] = new TextField();

                MagicSquareCellsTextfieldsContainer.setConstraints(MagicSquareCells[rowCounter][columnCounter], columnCounter, rowCounter);
                MagicSquareCellsTextfieldsContainer.getChildren().add(MagicSquareCells[rowCounter][columnCounter]);
                MagicSquareCells[rowCounter][columnCounter].getStyleClass().add("cell");


                final int row=rowCounter;
                final int col=columnCounter;

                if (columnCounter==0||columnCounter==Dimension+1||rowCounter==0||rowCounter==1+Dimension){
                    MagicSquareCells[rowCounter][columnCounter].setText(String.valueOf(magicSum));
                    MagicSquareCells[rowCounter][columnCounter].setEditable(false);
                    //css style
                    MagicSquareCells[rowCounter][columnCounter].getStyleClass().add("cell3");
                }else {



                    if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
                        TextField currentField=MagicSquareCells[rowCounter][columnCounter];
                        MagicSquareCells[rowCounter][columnCounter].textProperty().addListener((observable, oldVal, newVal) -> {
                            try {
                                int value=Integer.parseInt(currentField.getText());
                                if (false){//value<1 || value>Dimension*Dimension
                                    currentField.setText(oldVal);
                                }else if(false){//! inputset.contains(value)
                                    currentField.setText("");
                                }else {
                                    inputset.remove(value);
                                    user[row-1][col-1]=Integer.parseInt(newVal);//update the user

                                    if (!issolve){
                                        currentField.getStyleClass().add("cell2");
                                    }

                                }
                            }catch (NumberFormatException e){
                                currentField.setText("");
                            }


                        });
                    }else {
                        MagicSquareCells[rowCounter][columnCounter].setText(String.valueOf(user[rowCounter-1][columnCounter-1]));
                        MagicSquareCells[rowCounter][columnCounter].setEditable(false);
                        MagicSquareCells[rowCounter][columnCounter].setText(String.valueOf(user[rowCounter-1][columnCounter-1]));
                        MagicSquareCells[rowCounter][columnCounter].setOnMouseClicked(event -> {
                            if (swap==1){
                                swap=2;
                                x=row;
                                y=col;
                                temp=MagicSquareCells[row][col].getText();
                                MagicSquareCells[row][col].getStyleClass().add("cell2");
                            }else {
                                swap=1;
                                MagicSquareCells[x][y].setText(MagicSquareCells[row][col].getText());
                                user[x-1][y-1]=Integer.parseInt(MagicSquareCells[row][col].getText());
                                MagicSquareCells[row][col].setText(temp);
                                user[row-1][col-1]=Integer.parseInt(temp);
                                MagicSquareCells[x][y].getStyleClass().clear();
                                MagicSquareCells[x][y].getStyleClass().add("cell");

                                //generation
                                gerneration++;
                                Gerneration.setText(String.valueOf(gerneration));
                            }
                        });
                    }

                }



                TextField currentField = MagicSquareCells[rowCounter][columnCounter];


                MagicSquareCells[rowCounter][columnCounter].setOnKeyPressed((KeyEvent ke) -> {
                    listenToChange = true;
                });
                MagicSquareCells[rowCounter][columnCounter].setPrefSize(1000/Dimension,1000/Dimension);


            }

            Container.setCenter(MagicSquareCellsContainer);
            BorderPane.setAlignment(MagicSquareCellsContainer, Pos.CENTER);
            Container.getChildren().addAll();
        }
        //</editor-fold>


    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void returnTotheMainMenu(){
        gameTime.pause();
        mainApp.mainpane.setCenter(null);
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Start.fxml"));
            BorderPane StartLayout = (BorderPane) loader.load();
            StartController controller = loader.getController();
            controller.setMainApp(mainApp);
            // Show the scene containing the root layout.
            mainApp.mainpane.setCenter(StartLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void solvetheAnswer(){
        issolve=true;
        long time=0;
        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            user=MagicSquare.solve(user);
        }else{
            for(int i=0;i<Dimension;i++){
                for(int j=0;j<Dimension;j++){
                    user[i][j] = 0;
                }
            }
        }



        int numOfThreads = 1;


        ArrayList<matrix.puzzle.MagicSquare> magicSquares = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int _i = 0; _i < numOfThreads; _i++) {
            matrix.puzzle.MagicSquare magicSquare = new matrix.puzzle.MagicSquare(Dimension);
            for(int i=0;i<Dimension;i++){
                for(int j=0;j<Dimension;j++){
                    if(user[i][j]!=0){
                        magicSquare.matrix.fillIsFixed(i,j);
                    }
                    magicSquare.matrix.fillGrid(i,j,user[i][j]);
                }
            }
            Thread thread = new Thread(magicSquare);
            threads.add(thread);
            magicSquares.add(magicSquare);
            thread.start();
        }

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                int iterations = 0;
                for (iterations = 0; iterations < 1000; iterations++) {
                    if (isCancelled()) {
                        updateMessage("Cancelled");
                        break;
                    }

                    Platform.runLater(() -> {
                        int currentBestError = Integer.MAX_VALUE;
//                        System.out.println(magicSquares.size());
                          for(matrix.puzzle.MagicSquare magicSquare: magicSquares){
                              int score = magicSquare.bestScore;
//                              System.out.println("----------------------------------------------------");
//                              System.out.println("Score:"+score);
//                              System.out.println("----------------------------------------------------");
                              if(score<currentBestError){
                                  currentBestError = score;
                                  bestMagicSquare = magicSquare;
                              }
                          }

                        for (int i = 0; i < Dimension; i++) {
                            for (int j = 0; j < Dimension; j++) {
                              MagicSquareCells[i + 1][j + 1].setText(String.valueOf(bestMagicSquare.currentState[i][j]));
                            }
                        }
                        if(currentBestError == 0){
                            finished=true;
                            solveTime.setText(bestMagicSquare.executionTime + " (ms)");
                            Gerneration.setText(String.valueOf(bestMagicSquare.generation));
                        }

                    });
                    if (finished){
                        break;
                    }
                    // Now block the thread for a short time, but be sure
                    // to check the interrupted exception for cancellation!
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Cancelled");
                            break;
                        }
                    }
                }


                return iterations;
            }

        };
        this.task = task;
        new Thread(task).start();






    }

    @FXML
    private void checktheAnswer(){
        gameTime.pause();
        Boolean answer=false;//是否正确
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check");
        alert.setHeaderText("Checke the Answer");
        alert.setContentText("Answer is "+answer);
        alert.showAndWait();
        //is wrong gameTime.start
    }

    @FXML
    private void handleSave(){
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.primaryStage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            saveSudokuToFile(file);
        }
    }

    void saveSudokuToFile(File file){
        try {
            JAXBContext context = JAXBContext
                    .newInstance(WrapperClass.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            WrapperClass wrapper = new WrapperClass();
            wrapper.setMagicSquareWrapperClass(MagicSquare.magicsquare,user,"1");

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

        } catch (Exception e) { // catches ANY exception
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public static void buttonstylesetter(Button button){
        button.getStyleClass().add("icon-text-button");
        button.getStyleClass().add("button-icon_text");
        button.getStyleClass().add("button-icon_text--transparent");
        button.setAlignment(Pos.CENTER);
    }

}
