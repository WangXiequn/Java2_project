package matrix.puzzle;

import matrix.dataformat.Point;
import utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class MagicSquare extends MatrixPuzzleBase implements Runnable{

    private volatile boolean flag = false;
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
        int n = magicSquare.dimension;
        int[] columnScore = new int[n];
        int[] rowScore = new int[n];
        int leftUpperDiagonal;
        int leftLowerDiagonal;

        for(int i=0;i<n;i++){
            rowScore[i] = magicSquare.getSumOfRow(i)- magicSquare.sum;
        }

        for(int i=0;i<n;i++){
            columnScore[i] = magicSquare.getSumOfColumn(i)- magicSquare.sum;
        }

        leftLowerDiagonal = magicSquare.getSumOfLeftLowerDiagonal()- magicSquare.sum;
        leftUpperDiagonal = magicSquare.getSumOfLeftUpperDiagonal()- magicSquare.sum;

        int currentScore = bestScore;

        ArrayList<Point> setNotFixed = magicSquare.collectSetNotFixed();
        int MAX = 1000000;
        double T = MAX;
        int generation = 0;
        int cnt = 0;

        while(bestScore!=0){
            if(flag) return;
            generation += 1;
            Point u,v;
            int l,r;

            l = RandomUtil.getRandomInt(0,setNotFixed.size());
            u = setNotFixed.get(l);

            r = l;
            while(l==r){
                r = RandomUtil.getRandomInt(0,setNotFixed.size());
            }

            v = setNotFixed.get(r);

            int newFitness = currentScore;
            int difference = magicSquare.matrix.getValue(u.rowIndex,u.columnIndex)- magicSquare.matrix.getValue(v.rowIndex,v.columnIndex);
            if(u.rowIndex!=v.rowIndex){
                newFitness = newFitness-Math.abs(rowScore[u.rowIndex])+Math.abs(rowScore[u.rowIndex]-difference);
                newFitness = newFitness-Math.abs(rowScore[v.rowIndex])+Math.abs(rowScore[v.rowIndex]+difference);
            }

            if(u.columnIndex!=v.columnIndex){
                newFitness = newFitness-Math.abs(columnScore[u.columnIndex]) + Math.abs(columnScore[u.columnIndex]-difference);
                newFitness = newFitness-Math.abs(columnScore[v.columnIndex]) + Math.abs(columnScore[v.columnIndex]+difference);
            }
            int newLeftUpper = leftUpperDiagonal;
            int newLeftLower = leftLowerDiagonal;

            if(!(u.rowIndex==u.columnIndex&&v.rowIndex==v.columnIndex)){
                if(u.rowIndex==u.columnIndex){
                    newLeftUpper = leftUpperDiagonal-difference;
                    newFitness = newFitness - Math.abs(leftUpperDiagonal) + Math.abs(newLeftUpper);
                }

                if(v.rowIndex==v.columnIndex){
                    newLeftUpper = leftUpperDiagonal+difference;
                    newFitness = newFitness - Math.abs(leftUpperDiagonal) + Math.abs(newLeftUpper);
                }
            }

            if(!((u.rowIndex+u.columnIndex==n-1)&&(v.rowIndex+v.columnIndex==n-1))){
                if(u.rowIndex+u.columnIndex==n-1){
                    newLeftLower = leftLowerDiagonal-difference;
                    newFitness = newFitness - Math.abs(leftLowerDiagonal) + Math.abs(newLeftLower);
                }

                if(v.rowIndex+v.columnIndex==n-1){
                    newLeftLower = leftLowerDiagonal+difference;
                    newFitness = newFitness - Math.abs(leftLowerDiagonal) + Math.abs(newLeftLower);
                }
            }

            int delta = newFitness-currentScore;
            if (delta<0){
                currentScore = newFitness;
                if(currentScore<bestScore){
                    bestScore = currentScore;
                }
                rowScore[u.rowIndex] -= difference;
                rowScore[v.rowIndex] += difference;

                columnScore[u.columnIndex] -= difference;
                columnScore[v.columnIndex] += difference;

                leftUpperDiagonal = newLeftUpper;
                leftLowerDiagonal = newLeftLower;

                magicSquare.swap(u.rowIndex,u.columnIndex,v.rowIndex,v.columnIndex);

                cnt = 0;
            }else{
                double rate = Math.exp(-delta/T);
                double rand = Math.random();
                if(rand<rate){
                    currentScore = newFitness;
                    rowScore[u.rowIndex] -= difference;
                    rowScore[v.rowIndex] += difference;

                    columnScore[u.columnIndex] -= difference;
                    columnScore[v.columnIndex] += difference;

                    leftUpperDiagonal = newLeftUpper;
                    leftLowerDiagonal = newLeftLower;

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
        flag = true;
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


    public void swap(int ui, int uj, int vi, int vj){
        int temp = matrix.getValue(ui,uj);
        matrix.fillGrid(ui,uj, matrix.getValue(vi,vj));
        matrix.fillGrid(vi,vj,temp);
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

    @Override
    public void run() {
        solve();
    }
}