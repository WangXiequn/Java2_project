package matrix.puzzle;

import matrix.Matrix;
import utils.RandomUtil;

public class MagicSquare extends MatrixPuzzleBase{

    private final int sum;

    public MagicSquare(int dimension) {
        super(dimension);
        sum = getSum(dimension);
    }

    public MagicSquare(int dimension, int[][] matrix){
        super(dimension, matrix);
        sum = getSum(dimension);
    }

    public int getSum(int n){
        return (n*n+1)*n/2;
    }

    @Override
    public void init() {

    }

    @Override
    public void solve() {

    }

    @Override
    public void updatePlayerMove(String matrixInfo) {

    }

    public int getSumOfRow(int rowIndex){
        int cnt = 0;
        for(int i=0;i<dimension;i++){
            cnt += matrix.getValue(rowIndex,i);
        }
        return cnt;
    }

    public int getSumOfColumn(int columnIndex){
        int cnt = 0;
        for(int i=0;i<dimension;i++){
            cnt += matrix.getValue(columnIndex,i);
        }
        return cnt;
    }

    public int getSumOfLeftUpperDiagonal(){
        int cnt =0;
        for(int i = 0;i<dimension;i++){
            cnt += matrix.getValue(i,i);
        }
        return cnt;
    }

    public int getSumOfLeftLowerDiagonal(){
        int cnt = 0;
        for(int i = 0;i<dimension;i++){
            cnt += matrix.getValue(i,dimension-i);
        }
        return cnt;
    }

    public int evaluateColumn(int columnIndex){
        return Math.abs(sum-getSumOfColumn(columnIndex));
    }

    public int evaluateRow(int rowIndex){
        return Math.abs(sum-getSumOfRow(rowIndex));
    }

    public int evaluateLeftUpperDiagonal(){
        return Math.abs(sum-getSumOfLeftUpperDiagonal());
    }

    public int evaluateLeftLowerDiagonal(){
        return Math.abs(sum-getSumOfLeftLowerDiagonal());
    }



     

    @Override
    public String checkValidity() {
        boolean success = true;
        StringBuilder stringBuilder = new StringBuilder();


        return null;
    }

    @Override
    public String getProfileConfig() {
        return null;
    }

    @Override
    public void randomlyGenerateInitialState(int n) {
        int rowIndex = RandomUtil.getRandomInt(0,dimension-n);
        int columnIndex = RandomUtil.getRandomInt(0,dimension-n);
        int cnt = 0;
        for(int i=rowIndex;i<rowIndex+n;i++){
            for(int j=columnIndex;j<columnIndex+n;j++){
                matrix.fillGrid(i,j,++cnt);
                matrix.fillIsFixed(i,j);
            }
        }

        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(matrix.getValue(i,j)==0){
                    matrix.fillGrid(i,j,++cnt);
                }
            }
        }
    }

}
