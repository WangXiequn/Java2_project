package matrix.puzzle;

import matrix.Matrix;
import matrix.dataformat.Point;
import utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MagicSquare extends MatrixPuzzleBase{

    private final int sum;
    private final int numOfFixedData;
    private final int sqrtOfNumFixedData;
    private int sig[][];
    private int generation = 0;

    public MagicSquare(int dimension,int m_numOfFixedData) {
        super(dimension);
        sum = getSum(dimension);
        numOfFixedData = m_numOfFixedData;
        sqrtOfNumFixedData = getSqrtOfNumFixedData();
        sig = new int[dimension][dimension];
    }

    public MagicSquare(int dimension, int[][] matrix, int m_numOfFixedData){
        super(dimension, matrix);
        sum = getSum(dimension);
        numOfFixedData = m_numOfFixedData;
        sqrtOfNumFixedData = getSqrtOfNumFixedData();
        sig = new int[dimension][dimension];
    }

    public int getSqrtOfNumFixedData(){
        for(int i=0;i*i<=numOfFixedData;i++) {
            if (i * i == numOfFixedData) {
                return i;
            }
        }
        return 0;
    }


    public int getSum(int n){
        return (n*n+1)*n/2;
    }

    @Override
    public void init() {

    }

    public MagicSquare deepCopy(MagicSquare magicSquare){
        MagicSquare res;
        res = new MagicSquare(magicSquare.dimension, magicSquare.numOfFixedData);
        for(int i=0;i<magicSquare.dimension;i++){
            for(int j=0;j<magicSquare.dimension;j++){
                res.matrix.fillGrid(i,j,magicSquare.matrix.getValue(i,j));
                if(magicSquare.matrix.isFixed(i,j)){
                    res.matrix.fillIsFixed(i,j);
                }
                res.sig[i][j] = magicSquare.sig[i][j];
            }
        }
        res.generation = magicSquare.generation;
        return res;
    }

    @Override
    public void solve() {
        MagicSquare magicSquare = shuffleMagicSquare();
        boolean flag = true;
        double T = 1;
        while(true) {
            int currentScore = magicSquare.evaluateAll();
            int nextGenerationScore = 0;
            int n_row = magicSquare.getRowsNotCorrect();
            int n_column = magicSquare.getColumnsNotCorrect();
            int d1 = magicSquare.getD1();
            int d2 = magicSquare.getD2();

            double p_m = 1.0 / ((d1 + d2) * dimension + (n_column * n_row));

            double sigma = 0.0;
            for (int j = 0; j < magicSquare.dimension; j++) {
                sigma += magicSquare.evaluateColumn(j);
                sigma += magicSquare.evaluateRow(j);
            }
            if(n_row+n_column!=0){
                sigma /= (n_row + n_column);
            }
            else{
                sigma = 0.0;
            }

            double temp = 0.0;
            temp += magicSquare.evaluateLeftLowerDiagonal();
            temp += magicSquare.evaluateLeftUpperDiagonal();
            if(d1+d2!=0){
                temp /= d1 + d2;
            }else{
                temp = 0.0;
            }

            sigma += temp;

            if(flag){
                magicSquare.fillSig(sigma);
                flag = false;
            }

            MagicSquare backup = deepCopy(magicSquare);


            for (int i = 0; i < magicSquare.dimension; i++) {
                for (int j = 0; j < magicSquare.dimension; j++) {
                    if (magicSquare.matrix.isFixed(i, j)) continue;
                    double ran = Math.random();
                    if (ran < p_m) {
                        int a_ij = magicSquare.matrix.getValue(i, j) + RandomUtil.getRandomInt(1, magicSquare.sig[i][j]+1);
                        if (a_ij < 1) a_ij = RandomUtil.getRandomInt(1, dimension + 1);
                        if (a_ij > dimension * dimension)
                            a_ij = dimension * dimension - RandomUtil.getRandomInt(0, dimension + 1);
                        if (a_ij <= numOfFixedData) continue;
                        for (int _i = 0; _i < dimension; _i++) {
                            for (int _j = 0; _j < dimension; _j++) {
                                if (magicSquare.matrix.getValue(_i, _j) == a_ij) {
                                    magicSquare.swap(i, j, _i, _j);
                                }
                            }
                        }
                        magicSquare.updateSig(i, j);
                    }
                }
            }

            magicSquare.generation += 1;

            nextGenerationScore = magicSquare.evaluateAll();
            System.out.println("Next Generation: "+ nextGenerationScore);
            if(nextGenerationScore == 0){
                System.out.println(magicSquare.getMatrixInfo());
                break;
            }
            if(nextGenerationScore > currentScore){
                double rand = Math.random();
                if(rand>T){
                    magicSquare = backup;
                }
            }

            T *= 0.9999;

        }
    }

    public void swap(int ui, int uj, int vi, int vj){
        int temp = matrix.getValue(ui,uj);
        matrix.fillGrid(ui,uj, matrix.getValue(vi,vj));
        matrix.fillGrid(vi,vj,temp);
    }

    public void updateSig(int rowIndex, int columnIndex){
        sig[rowIndex][columnIndex] += RandomUtil.getRandomInt(0,3) - 1;
        if(sig[rowIndex][columnIndex]<1) sig[rowIndex][columnIndex] =1;
    }

    public void fillSig(double sigma){
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                sig[i][j] = RandomUtil.getRandomInt(1,(int)sigma+1);
            }
        }
    }

    public int getD1(){
        if(evaluateLeftLowerDiagonal()!=0) return 1;
        return 0;
    }

    public int getD2(){
        if(evaluateLeftUpperDiagonal()!=0) return 1;
        return 0;
    }

    public int getRowsNotCorrect(){
        int cnt = 0;
        for(int i=0;i<dimension;i++){
            if(evaluateRow(i)!=0) cnt++;
        }
        return cnt;
    }

    public int getColumnsNotCorrect(){
        int cnt = 0;
        for(int i=0;i<dimension;i++){
            if(evaluateColumn(i)!=0) cnt++;
        }
        return cnt;
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
            cnt += matrix.getValue(i,columnIndex);
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
            cnt += matrix.getValue(i,dimension-i-1);
        }
        return cnt;
    }

    public int evaluateAll(){
        int cnt = 0;
        for(int i=0;i<dimension;i++){
            cnt += evaluateColumn(i);
            cnt += evaluateRow(i);
        }
        cnt += evaluateLeftLowerDiagonal();
        cnt += evaluateLeftUpperDiagonal();
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
    public void randomlyGenerateInitialState() {
        int rowIndex = RandomUtil.getRandomInt(0,dimension-sqrtOfNumFixedData);
        int columnIndex = RandomUtil.getRandomInt(0,dimension-sqrtOfNumFixedData);
        int cnt = 0;
        for(int i=rowIndex;i<rowIndex+sqrtOfNumFixedData;i++){
            for(int j=columnIndex;j<columnIndex+sqrtOfNumFixedData;j++){
                matrix.fillGrid(i,j,++cnt);
                matrix.fillIsFixed(i,j);
            }
        }

        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(matrix.getValue(i,j)==0){
                    matrix.fillGrid(i,j,0);
                }
            }
        }
    }

    public MagicSquare shuffleMagicSquare(){
        MagicSquare magicSquare = new MagicSquare(dimension,numOfFixedData);
        boolean[] isVisited = new boolean[dimension*dimension+1];
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(matrix.isFixed(i,j)){
                    isVisited[matrix.getValue(i,j)] = true;
                }
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=1;i<=dimension*dimension;i++){
            if(isVisited[i]) continue;
            list.add(i);
        }
        Collections.shuffle(list);

        int cnt = 0;
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(matrix.isFixed(i,j)){
                    magicSquare.matrix.fillGrid(i,j, matrix.getValue(i,j));
                    magicSquare.matrix.fillIsFixed(i,j);
                    continue;
                }
                magicSquare.matrix.fillGrid(i,j,list.get(cnt++));
            }
        }
        return magicSquare;
    }

}
