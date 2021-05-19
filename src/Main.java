import matrix.dataformat.Point;
import matrix.puzzle.MagicSquare;
import utils.RandomUtil;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        int dimension = 5;
        int numOfFixedData = 9;
        MagicSquare magicSquare = new MagicSquare(dimension,numOfFixedData);
        magicSquare.randomlyGenerateInitialState();
        System.out.println(magicSquare.getMatrixInfo());
        magicSquare.solve();
    }
}

