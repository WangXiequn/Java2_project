package matrix.puzzle;

import matrix.Matrix;
import matrix.dataformat.Point;
import utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    public static Long getTimestamp(Date date){
        if (null == date) {
            return (long) 0;
        }
        String timestamp = String.valueOf(date.getTime());
        return Long.valueOf(timestamp);
    }

    @Override
    public void solve() {
        Date start = new Date();
        MagicSquare magicSquare = shuffleMagicSquare();
        int bestScore = magicSquare.evaluateAll();
        ArrayList<Point> setNotFixed = magicSquare.collectSetNotFixed();
        int MAX = 1000000;
        double T = MAX;
        int generation = 0;
        int cnt = 0;
        while(bestScore!=0){
            generation += 1;
            int previousScore = magicSquare.evaluateAll();
            Point u,v;
            int l,r;

            l = RandomUtil.getRandomInt(0,setNotFixed.size());
            u = setNotFixed.get(l);

            r = l;
            while(l==r){
                r = RandomUtil.getRandomInt(0,setNotFixed.size());
            }

            v = setNotFixed.get(r);


            magicSquare.swap(u.rowIndex,u.columnIndex,v.rowIndex,v.columnIndex);

            int currentScore = magicSquare.evaluateAll();
            int delta = currentScore-previousScore;
            if (delta<0){
                bestScore = currentScore;
                cnt = 0;
            }else{
                double rate = Math.exp(-delta/T);
                double rand = Math.random();
                if(rand>rate){
                    magicSquare.swap(u.rowIndex,u.columnIndex,v.rowIndex,v.columnIndex);
                }
            }
            if(++cnt==1000000){
                T = MAX;
                magicSquare = shuffleMagicSquare();
                bestScore = magicSquare.evaluateAll();
            }
            T *= 0.9999;
        }

        Date end = new Date();
        System.out.println((getTimestamp(end)-getTimestamp(start))+" (ms)");
        System.out.println("Generation: "+generation);
        System.out.println(magicSquare.getMatrixInfo());
    }

    public ArrayList<Point> collectSetNotFixed(){
        ArrayList<Point> res = new ArrayList<>();
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(matrix.isFixed(i,j)) continue;
                res.add(new Point(i,j));
            }
        }
        return res;
    }

    public HashMap<Integer,Point> collectS1(ArrayList<Integer> joinList){
        HashMap<Integer,Point> res = new HashMap<>();
        for(int i=0;i<dimension;i++){
            for (int columnIndex : joinList) {
                if (matrix.isFixed(i, columnIndex)) continue;
                res.put(matrix.getValue(i, columnIndex), new Point(i, columnIndex));
            }
        }
        for(int rowIndex:joinList){
            for(int j=0;j<dimension;j++){
                if (matrix.isFixed(rowIndex,j)) continue;
                res.put(matrix.getValue(rowIndex,j),new Point(rowIndex,j));
            }
        }
        return res;
    }

    public HashMap<Integer,Point> collectS2(ArrayList<Integer> rowList, ArrayList<Integer> columnList){
        HashMap<Integer,Point> res = new HashMap<>();
        for(int i=0;i<dimension;i++){
            for (int columnIndex : columnList) {
                if (matrix.isFixed(i, columnIndex)) continue;
                res.put(matrix.getValue(i, columnIndex), new Point(i, columnIndex));
            }
        }
        for(int rowIndex:rowList){
            for(int j=0;j<dimension;j++){
                if (matrix.isFixed(rowIndex,j)) continue;
                res.put(matrix.getValue(rowIndex,j),new Point(rowIndex,j));
            }
        }
        return res;
    }

    public ArrayList<Integer> collectColumnsNotCorrect(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<dimension;i++){
            if(evaluateColumn(i)!=sum){
                list.add(i);
            }
        }
        return list;
    }

    public ArrayList<Integer> collectRowsNotCorrect(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<dimension;i++){
            if(evaluateRow(i)!=sum){
                list.add(i);
            }
        }
        return list;
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

    public int evaluateRowsAndColumns(){
        int cnt = 0;
        for(int i=0;i<dimension;i++){
            cnt += evaluateColumn(i);
            cnt += evaluateRow(i);
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
