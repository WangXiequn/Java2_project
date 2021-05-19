package matrix;

public class Matrix {
    private final int dimension;
    private final int[][] matrix;
    private final boolean[][] isFixed;


    public Matrix(int dimension) {
        this.dimension = dimension;
        this.matrix = new int[dimension][dimension];
        this.isFixed = new boolean[dimension][dimension];
    }

    public Matrix(int dimension, int[][] matrix){
        this.dimension = dimension;
        this.matrix = matrix;
        this.isFixed = new boolean[dimension][dimension];
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                isFixed[i][j] = matrix[i][j] != 0;
            }
        }
    }

    public void fillGrid(int row, int column, int value){
        matrix[row][column] = value;
    }

    public void fillIsFixed(int row, int column){
        isFixed[row][column] = true;
    }

    public int getValue(int row, int column){
        return matrix[row][column];
    }

    public boolean isFixed(int row, int column){
        return isFixed[row][column];
    }

    public void updateMatrix(int [][] matrix){

    }

    public int[][] getMatrix(){
        return matrix;
    }

    public void loadMatrixFromDatabase(){

    }

    public void saveMatrixToDatabase(){

    }


    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                stringBuilder.append(matrix[i][j]);
                if(j+1 != dimension)
                    stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
