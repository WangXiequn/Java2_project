import matrix.dataformat.Point;
import matrix.puzzle.MagicSquare;
import utils.RandomUtil;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int dimension = 20;
        int numOfThreads = 6;
        int numOfFixedData = 0;
        MagicSquare magicSquare = new MagicSquare(dimension,numOfFixedData);
        int executionTimes = 10;


        for(int i=0;i<numOfThreads;i++){
            Thread thread = new Thread(magicSquare);
            thread.start();
        }


    }
}

