package ch.makery.address.view;
import ch.makery.address.MainApp;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

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
    private Button redoButton;
    @FXML
    private BorderPane Container;

    private GridPane sudokuCellsTextfieldsContainer;

    public static int Dimension=9;//9/16/25
    Boolean listenToChange = false;
    private ContextMenu contextMenu;
    private MainApp mainApp;

    static TextField[][] sudokuCells;//when the
    public static int[][] user; //Reads the Sudoku from the user
    public static Integer[][] computerSolution; //Where computer returns the wrong cells
    public static boolean[][] marketSolution;

    static Integer[][] loadedGameSudoku;

    static ArrayList<Integer[]> history = new ArrayList<>();
    static int undoHistoryMoveNumber = -1;
    static int redoHistoryMoveNumber = 0;
    static String sudokuGame;
    static SudokuGenerator generator = new SudokuGenerator();


    public SudokuOverviewController() {
    }

    @FXML
    private void initialize() {
        // Initialize the  table with the Button.


        initSudokuBlock();

    }

    private void initSudokuBlock() {
        //Sudoku card layout
        BorderPane sudokuCellsContainer = new BorderPane();
        sudokuCellsContainer.setPadding(new Insets(7));
        sudokuCellsContainer.setMaxHeight(500);
        sudokuCellsContainer.setMaxWidth(500);

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
                }


                TextField currentField = sudokuCells[rowCounter][columnCounter];
                int currentFieldRowNumber = rowCounter;
                int currentFieldColumnNumber = columnCounter;

                sudokuCells[rowCounter][columnCounter].setOnKeyPressed((KeyEvent ke) -> {
                    listenToChange = true;
                });





                final int row=rowCounter;
                final int col=columnCounter;
                //Adding listener to validate the Sudoku input
                sudokuCells[rowCounter][columnCounter].textProperty().addListener((observable, oldVal, newVal) -> {

                    if (currentField.getLength() > 1) {
                        currentField.setText(oldVal);

                    } else if (!isInputValid(currentField.getText())) {
                        currentField.setText("");
                    } else //Only save in history if the listenToChange == true
                        if (listenToChange && mainApp.PlayingMode.equals("NEW_GAME_MODE") || listenToChange && mainApp.PlayingMode.equals("LOAD_GAME_MODE")) {
                            //Clearign any history moves if the user made a move and there are redo moves to make
                            user[row][col]=Integer.parseInt(currentField.getText());




                        }

                });
            }

            Container.setCenter(sudokuCellsContainer);
            Container.setAlignment(sudokuCellsContainer, Pos.CENTER);
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
}

