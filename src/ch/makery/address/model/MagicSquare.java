package ch.makery.address.model;

import ch.makery.address.MainApp;
import ch.makery.address.view.MagicSquarePaneController;
import ch.makery.address.view.SudokuOverviewController;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Random;

public class MagicSquare {

    public static int Dimension= MagicSquarePaneController.Dimension;
    public static int[][] magicsquare;

    public void MakeMagicsquare(int Level) {
        magicsquare=MagicRandom(Level);

        for (int i=0;i< magicsquare.length;i++){//user
            for (int j=0;j<magicsquare[i].length;j++){
                MagicSquarePaneController.user[i][j]=magicsquare[i][j];
            }
        }

        //solve magic square
//        magicsquare=solve(magicsquare);
        for (int i=0;i< magicsquare.length;i++){//user
            for (int j=0;j<magicsquare[i].length;j++){
                MagicSquarePaneController.reset[i][j]=magicsquare[i][j];
            }
        }

    }

    //challenge
    public void MakeMagicsquare(int Level,int a) {

        magicsquare=new int[Level][Level];
        for (int i=0;i< magicsquare.length;i++){//user
            for (int j=0;j<magicsquare[i].length;j++){
                magicsquare[i][j]=0;
            }
        }

        for (int i=0;i< magicsquare.length;i++){//user
            for (int j=0;j<magicsquare[i].length;j++){
                MagicSquarePaneController.user[i][j]=magicsquare[i][j];
            }
        }

        for (int i=0;i< magicsquare.length;i++){//user
            for (int j=0;j<magicsquare[i].length;j++){
                MagicSquarePaneController.reset[i][j]=magicsquare[i][j];
            }
        }

    }

    // TO solve the magic square
    public static int[][] solve(int[][] temp){
        return temp;
    }

    public static int[][] MagicRandom(int n){
        int [][]target=new int[n][n];
        ArrayList<Integer> t=new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                int accRandom=r.nextInt(n*n)+1;
                if (i==0 && j==0){
                    target[i][j]=accRandom;
                    t.add(accRandom);
                    continue;
                }

                while (t.contains(accRandom)){//除去重复的
                    accRandom=r.nextInt(n*n)+1;
                }
                target[i][j]=accRandom;
                t.add(accRandom);
            }

        }
        return target;
    }
}
