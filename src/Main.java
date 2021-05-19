import matrix.dataformat.Point;
import matrix.puzzle.MagicSquare;
import utils.RandomUtil;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        int dimension = 20;
        int numOfFixedData = 0;
        MagicSquare magicSquare = new MagicSquare(dimension,numOfFixedData);
        magicSquare.randomlyGenerateInitialState();
        magicSquare.solve();
    }
}

