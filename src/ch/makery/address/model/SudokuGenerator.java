package ch.makery.address.model;

import ch.makery.address.view.SudokuOverviewController;
import ch.makery.address.view.SudokuOverviewController.*;


import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class SudokuGenerator {
//<editor-fold defaultstate="collapsed" desc="Level">
    public static int Dimension= SudokuOverviewController.Dimension;
    /**
     * 3*3 : (42 to 51) Cells
     */
    public static final int EASY = 42;
    /**
     * 4*4 : (32 to 41) Cells
     */
    public static final int MEDIUM = 170;
    /**
     * 5*5 : (22 to 31) Cells
     */
    public static final int HARD = 300;
    public static int[][] sudoku;
    public static int[][] easy={
            {7,6,1,9,3,4,8,2,5},
            {3,5,4,6,2,8,1,9,7},
            {9,2,8,1,5,7,6,3,4},
            {2,1,9,5,4,6,3,7,8},
            {4,8,3,2,7,9,5,1,6},
            {5,7,6,3,8,1,9,4,2},
            {1,9,5,7,6,2,4,8,3},
            {8,3,2,4,9,5,7,6,1},
            {6,4,7,8,1,3,2,5,9}};


    public void MakeSudoku(int Level) {
        // Create Complete Sudoku :D
        generateCompleteSudoku();
        //int[][] solve=solver(sudoku); // 解数独
//        for (int i=0;i< solve.length;i++){
//            for (int j=0;j<solve[i].length;j++){
//                SudokuOverviewController.computerSolution[i][j]=solve[i][j];
//            }
//        }
        //模拟上述方法 sudoku
        for (int i=0;i< sudoku.length;i++){
            for (int j=0;j<sudoku[i].length;j++){
                sudoku[i][j]=easy[i][j];
                SudokuOverviewController.computerSolution[i][j]=easy[i][j];
            }
        }
        removecell(Level);
        for (int i=0;i< sudoku.length;i++){
            for (int j=0;j<sudoku[i].length;j++){
                SudokuOverviewController.user[i][j]=sudoku[i][j];
            }
        }



    }

    public void generateCompleteSudoku(){
        sudoku=new int[SudokuOverviewController.Dimension][SudokuOverviewController.Dimension];
        for (int i=0;i<sudoku.length;i++){
            for (int j=0;j< sudoku[i].length;j++){
                sudoku[i][j]=0;
            }
        }
    }
    public void removecell(int Level){
        int RowIndex, ColumnIndex; // To store Cell Location.

        for (int i=0;i<Level;i++){
            RowIndex=(int)(Math.random()*Dimension);
            ColumnIndex=(int)(Math.random()*Dimension);
            if (sudoku[RowIndex][ColumnIndex]!=0){
                sudoku[RowIndex][ColumnIndex]=0;
            }else {
                i--;
            }
        }
    }

    public int[][] solver(int[][] temp){//solve
        return sudoku;
    }


}
