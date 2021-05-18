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

    static int Dimension=9;//9/16/25
    Boolean listenToChange = false;
    private ContextMenu contextMenu;
    private MainApp mainApp;

    static TextField[][] sudokuCells;
    static Integer[][] user; //Reads the Sudoku from the user
    static Integer[][] computerSolution; //Where computer returns the wrong cells
    static Boolean[][] markSolution; //Where computer returns the wrong cells
    static Integer[][] loadedGameSudoku;

    static ArrayList<Integer[]> history = new ArrayList<>();
    static int undoHistoryMoveNumber = -1;
    static int redoHistoryMoveNumber = 0;


    public SudokuOverviewController() {
    }

    @FXML
    private void initialize() {
        // Initialize the  table with the Button.
        Dimension=SudokuLevelController.Dimension*SudokuLevelController.Dimension;
        sudokuCells=new TextField[Dimension][Dimension];
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

//                sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell");
                sudokuCells[rowCounter][columnCounter].setContextMenu(contextMenu);

//                //If the cell is No.2 or No.5 on any column it will have right border
//                if (columnCounter == 2 || columnCounter == 5) {
//                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-right");
//                }
//
//                //If the cell is No.3 or No.6 on any row it will have top border
//                if (rowCounter == 3 || rowCounter == 6) {
//                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top");
//                    //Because the previus line of code override the right border
//                    if (columnCounter == 2 || columnCounter == 5) {
//                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top-right");
//                    }
//                }

                sudokuCells[rowCounter][columnCounter].setContextMenu(hiddenMenu);

                TextField currentField = sudokuCells[rowCounter][columnCounter];
                int currentFieldRowNumber = rowCounter;
                int currentFieldColumnNumber = columnCounter;

                sudokuCells[rowCounter][columnCounter].setOnKeyPressed((KeyEvent ke) -> {
                    listenToChange = true;
                });





                //Adding listener to validate the Sudoku input
                sudokuCells[rowCounter][columnCounter].textProperty().addListener((observable, oldVal, newVal) -> {
                    if (currentField.getLength() > 1) {
                        currentField.setText(oldVal);
                    } else if (!isInputValid(currentField.getText())) {
                        currentField.setText("");
                    } else //Only save in history if the listenToChange == true
                        if (listenToChange && mainApp.PlayingMode.equals("NEW_GAME_MODE") || listenToChange && mainApp.PlayingMode.equals("LOAD_GAME_MODE")) {
                            //Clearign any history moves if the user made a move and there are redo moves to make
                            if (redoHistoryMoveNumber != history.size()) {
                                redoButton.setDisable(true);

                                for (int counter = history.size() - 1; counter >= redoHistoryMoveNumber; counter--) {
                                    history.remove(counter);
                                }
                            }

                            //Saving current move into an arraylist
                            history.add(new Integer[]{currentFieldRowNumber, currentFieldColumnNumber, Integer.parseInt("".equals(oldVal) ? "0" : oldVal), Integer.parseInt("".equals(newVal) ? "0" : newVal)});
                            undoHistoryMoveNumber++;
                            redoHistoryMoveNumber++;
                            //undoButton.setDisable(false);


                        }
                    currentField.getStyleClass().remove("cell-danger");
                });
            }

            Container.setCenter(sudokuCellsContainer);
            Container.setAlignment(sudokuCellsContainer, Pos.CENTER);
            Container.getChildren().addAll();
        }
        //</editor-fold>


    }

    static Boolean sudokuOperation(int opType) {
        for (int rowCounter = 0; rowCounter < Dimension; rowCounter++) {
            for (int columnCounter = 0; columnCounter < Dimension; columnCounter++) {
                switch (opType) {
                    //Read Sudoku
                    case 1:
                        user[rowCounter][columnCounter] = Integer.parseInt("".equals(sudokuCells[rowCounter][columnCounter].getText()) ? "0" : sudokuCells[rowCounter][columnCounter].getText());
                        break;
                    //Print Sudoku
                    case 2:
                        if (computerSolution[rowCounter][columnCounter] != 0) {
                            if (MainApp.PlayingMode.equals("NEW_GAME_MODE")) {
                                sudokuCells[rowCounter][columnCounter].setEditable(false);
                            }
                            if (MainApp.PlayingMode.equals("LOAD_GAME_MODE")) {
                                if (markSolution[rowCounter][columnCounter]) {
                                    sudokuCells[rowCounter][columnCounter].setEditable(false);
                                }
                            }
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                        }
                        break;
                    //Clear Sudoku fields and array
                    case 3:
                        sudokuCells[rowCounter][columnCounter].setText("");
                        sudokuCells[rowCounter][columnCounter].setEditable(true);


                        user[rowCounter][columnCounter] = 0;
                        computerSolution[rowCounter][columnCounter] = 0;
                        markSolution[rowCounter][columnCounter] = Boolean.FALSE;
                        break;
                    //Check if the Sudoku cells are filled
                    case 4:
                        if ("".equals(sudokuCells[rowCounter][columnCounter].getText())) {
                            return false;
                        }
                    default:
                        break;
                }
            }
        }
        return true;
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
}

