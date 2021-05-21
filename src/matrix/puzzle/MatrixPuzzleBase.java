package matrix.puzzle;

import matrix.Matrix;

public abstract class MatrixPuzzleBase {

    public Matrix matrix;

    protected int dimension;

    public abstract void init();

    public abstract void solve();

    public abstract void updatePlayerMove(String matrixInfo);

    public abstract String checkValidity();

    public abstract String getProfileConfig();

    public abstract void randomlyGenerateInitialState();

    public MatrixPuzzleBase(int dimension){
        this.dimension = dimension;
        this.matrix = new Matrix(dimension);
    }

    public MatrixPuzzleBase(int dimension, int[][] matrix){
        this.dimension = dimension;
        this.matrix = new Matrix(dimension, matrix);
    }

    public String getMatrixInfo(){
        return this.matrix.toString();
    }

}
