import matrix.puzzle.MagicSquare;

public class Main {

    public static void main(String[] args){
        int n = 20;
        MagicSquare magicSquare = new MagicSquare(20);
        magicSquare.randomlyGenerateInitialState(3);

        System.out.println(magicSquare.getMatrixInfo());
    }
}

