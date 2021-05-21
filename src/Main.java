import matrix.dataformat.Point;
import matrix.puzzle.MagicSquare;
import utils.RandomUtil;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        solveTime(30);

    }
    static void solveTime(int T){
        for (int j = 0; j < T; j++) {
            int dimension = 20;
            int numOfThreads = 6;
            MagicSquare magicSquare = new MagicSquare(dimension);

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

