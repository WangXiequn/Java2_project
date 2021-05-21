import matrix.puzzle.MagicSquare;

import java.util.Scanner;
import java.util.StringTokenizer;

public class TestForMagicSquare {
    public static void main(String[] args) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        int looptime = 30;
        int dimension = 20;
        solveTime(looptime,dimension);
        long sum = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(out.toString());
        for (int i = 0; i < looptime; i++) {
            sum += Long.parseLong(stringTokenizer.nextToken());
        }
        System.err.println(out.toString()+"average: "+sum/looptime);
    }
    static void solveTime(int T,int dimension){
        for (int j = 0; j < T; j++) {
            //int dimension = 20;
            int numOfThreads = 8;
            int numOfFixedData = 0;
            MagicSquare magicSquare = new MagicSquare(dimension,numOfFixedData);

            Thread thread = new Thread(magicSquare);
            for(int i=0;i<numOfThreads-1;i++){
                thread = new Thread(magicSquare);
                thread.start();
            }
            while (thread.isAlive()){
            }
        }
    }
}
