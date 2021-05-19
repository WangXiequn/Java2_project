package ch.makery.address.view;
import ch.makery.address.MainApp;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import ch.makery.address.model.*;

public class SudokuOverviewController {
    @FXML
    private Button return0;
    @FXML
    private Button solve;
    @FXML
    private Button check;
    @FXML
    private Button save;//save button
    @FXML
    private BorderPane Container;
    @FXML
    private BorderPane Root;
    @FXML
    private AnchorPane rightpane;
    @FXML
    private Label dimension;

    private GridPane sudokuCellsTextfieldsContainer;

    public static int Dimension=9;//9/16/25
    Boolean listenToChange = false;
    private MainApp mainApp;

    static TextField[][] sudokuCells;//when the
    public static int[][] user; //Reads the Sudoku from the user
    public static Integer[][] computerSolution; //Where computer returns the wrong cells

    static Integer[][] loadedGameSudoku;

    static SudokuGenerator generator = new SudokuGenerator();


    public SudokuOverviewController() {
    }

    @FXML
    private void initialize() {
        // Initialize the  table with the Button.
        Root.getStylesheets().add("stylesheets/gameSceneStyle.css");
        dimension.setText(Math.sqrt(Dimension)+"x"+Math.sqrt(Dimension));
        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            save.setVisible(false);
        }
        initSudokuBlock();

        buttonstylesetter(return0);
        buttonstylesetter(solve);
        buttonstylesetter(check);
        buttonstylesetter(save);

        rightpane.getStyleClass().add("toolbar");

    }

    private void initSudokuBlock() {
        //Sudoku card layout

        BorderPane sudokuCellsContainer = new BorderPane();
        sudokuCellsContainer.setPadding(new Insets(7));
//        sudokuCellsContainer.setMaxHeight(600);
//        sudokuCellsContainer.setMaxWidth(600);
        sudokuCellsContainer.getStyleClass().add("card");

        //Cells container layout
        sudokuCellsTextfieldsContainer = new GridPane();
        sudokuCellsTextfieldsContainer.getStyleClass().add("cells-container");
        sudokuCellsContainer.setCenter(sudokuCellsTextfieldsContainer);

        //Unreal menu to override default menu
        ContextMenu hiddenMenu = new ContextMenu();
        hiddenMenu.hide();
        int rowCounter, columnCounter;

        //<editor-fold defaultstate="collapsed" desc="Sudoku Cells">
        for (rowCounter = 0; rowCounter < Dimension; rowCounter++) {
            for (columnCounter = 0; columnCounter < Dimension; columnCounter++) {
                //Create cells and positioning hem
                sudokuCells[rowCounter][columnCounter] = new TextField();
                sudokuCellsTextfieldsContainer.setConstraints(sudokuCells[rowCounter][columnCounter], columnCounter, rowCounter);
                sudokuCellsTextfieldsContainer.getChildren().add(sudokuCells[rowCounter][columnCounter]);


                if (user[rowCounter][columnCounter]!=0) {
                    sudokuCells[rowCounter][columnCounter].setText(String.valueOf(user[rowCounter][columnCounter]));
                    sudokuCells[rowCounter][columnCounter].setEditable(false);
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell2");
                }else {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell");
                }

                //划线css style
                //If the cell is No.2 or No.5 on any column it will have right border
                if ((columnCounter+1)%(int)Math.sqrt(Dimension)==0) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-right");
                }

                //If the cell is No.3 or No.6 on any row it will have top border
                if (rowCounter%(int)Math.sqrt(Dimension)==0) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top");
                    //Because the previus line of code override the right border
                    if ((columnCounter+1)%(int)Math.sqrt(Dimension)==0) {
                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top-right");
                    }
                }



                TextField currentField = sudokuCells[rowCounter][columnCounter];
                int currentFieldRowNumber = rowCounter;
                int currentFieldColumnNumber = columnCounter;

                sudokuCells[rowCounter][columnCounter].setOnKeyPressed((KeyEvent ke) -> {
                    listenToChange = true;
                });
                sudokuCells[rowCounter][columnCounter].setPrefSize(1000/Dimension,1000/Dimension);
//                sudokuCells[rowCounter][columnCounter].minWidth(10);





                final int row=rowCounter;
                final int col=columnCounter;
                //Adding listener to validate the Sudoku input
                sudokuCells[rowCounter][columnCounter].textProperty().addListener((observable, oldVal, newVal) -> {
                    if (Dimension==9){
                        if (currentField.getLength() > 1) {
                        currentField.setText(oldVal);

                        } else if (!isInputValid(currentField.getText())) {
                        currentField.setText("");
                        } else if (listenToChange && mainApp.PlayingMode.equals("NEW_GAME_MODE") || listenToChange && mainApp.PlayingMode.equals("LOAD_GAME_MODE")) {
                            //Clearign any history moves if the user made a move and there are redo moves to make
                            user[row][col]=Integer.parseInt(currentField.getText());

                        }else if (listenToChange && mainApp.PlayingMode.equals("CHALLENGE_MODE")){
                            user[row][col]=Integer.parseInt(currentField.getText());
                            sudokuCells[row][col].getStyleClass().clear();
                            sudokuCells[row][col].getStyleClass().add("cell3");
                        }
                    }else {
                        if (currentField.getLength() > 2 ||!isInputValid(currentField.getText())) {
                            currentField.setText(oldVal);

                        } else if (listenToChange && mainApp.PlayingMode.equals("NEW_GAME_MODE") || listenToChange && mainApp.PlayingMode.equals("LOAD_GAME_MODE")) {
                                //Clearign any history moves if the user made a move and there are redo moves to make
                                user[row][col] = Integer.parseInt(currentField.getText());

                        }else if (listenToChange && mainApp.PlayingMode.equals("CHALLENGE_MODE")){
                            user[row][col]=Integer.parseInt(currentField.getText());
                            sudokuCells[row][col].getStyleClass().clear();
                            sudokuCells[row][col].getStyleClass().add("cell3");
                        }
                    }

                });
            }

            Container.setCenter(sudokuCellsContainer);
            BorderPane.setAlignment(sudokuCellsContainer, Pos.CENTER);
            Container.getChildren().addAll();
        }
        //</editor-fold>


    }



    /**
     * Check that the input is integer
     *
     * @param input, Sudoku cells data
     * @return true/false
     */
    private boolean isInputValid(String input) {
        int checkInput;

        try {
            checkInput = Integer.parseInt(input);
            if (checkInput <= 0 || checkInput > Dimension) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }



    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void check(){// checkbutton
        if (isValid(user)){
            System.out.println("Congradulation");
        }else {
            System.out.println("Wrong");
        }
    }

    private static boolean isValid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if(!isValid(i,j,grid))
                    return false;
            }
        }
        return true;
    }

    private static boolean isValid(int i, int j, int[][] grid) {
        for (int col = 0; col < Dimension; col++) {
            if(col != j && grid[i][col]==grid[i][j])
                return false;
        }
        for (int row = 0; row < Dimension; row++) {
            if(row != i&& grid[row][j] == grid[i][j])
                return false;
        }
        int a= (int)Math.sqrt(Dimension);
        for (int row = (i/a)*a; row < (i/a)*a+a; row++) {
            for (int col = (j/a)*a; col < (j/a)*a+a; col++) {
                if (i==row &&j==col){
                    continue;
                }else {
                    if (grid[i][j]==grid[row][col]){
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public void returnTotheMainMenu(){
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
    private void solvetheSudoku(){

        if (mainApp.PlayingMode.equals("CHALLENGE_MODE")){
            int[][] tmp=SudokuGenerator.solver(user);
            for (int i=0;i<Dimension;i++){
                for (int j=0;j<Dimension;j++){
                    sudokuCells[i][j].setText(String.valueOf(tmp[i][j]));
                    computerSolution[i][j]=tmp[i][j];
                    user[i][j]=tmp[i][j];//update the user
                    listenToChange=false;
                }
            }
        }else {//new game mode
            for (int i=0;i<Dimension;i++){
                for (int j=0;j<Dimension;j++){
                    sudokuCells[i][j].setText(String.valueOf(computerSolution[i][j]));
                    user[i][j]=computerSolution[i][j];//update the user
                }
            }
        }

    }

    public static void buttonstylesetter(Button button){
        button.getStyleClass().add("icon-text-button");
        button.getStyleClass().add("button-icon_text");
        button.getStyleClass().add("button-icon_text--transparent");
        button.setAlignment(Pos.CENTER);
    }
}

