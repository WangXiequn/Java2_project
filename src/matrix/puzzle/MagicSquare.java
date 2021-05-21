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
    private long reheatGeneration = 1000000;
    public int[][] answer;

    public MagicSquare(int dimension,int m_numOfFixedData) {
        super(dimension);
        sum = getSum(dimension);
        numOfFixedData = m_numOfFixedData;
        sqrtOfNumFixedData = getSqrtOfNumFixedData();
        sig = new int[dimension][dimension];
        answer = new int[dimension][dimension];
    }

    public MagicSquare(int dimension, int[][] matrix, int m_numOfFixedData){
        super(dimension, matrix);
        sum = getSum(dimension);
        numOfFixedData = m_numOfFixedData;
        sqrtOfNumFixedData = getSqrtOfNumFixedData();
        sig = new int[dimension][dimension];
        answer = new int[dimension][dimension];
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

    public static int abs(int x){
        return x>0?x:-x;
    }
    @Override
    public void solve() {
        Date start = new Date();
        MagicSquare magicSquare = shuffleMagicSquare();
        MagicSquare root = deepCopy(magicSquare);
        int bestScore = magicSquare.evaluateAll();
        int n = magicSquare.dimension;
        int[] columnScore = new int[n];
        int[] rowScore = new int[n];
        int leftUpperDiagonal;
        int leftLowerDiagonal;
        if(dimension<30){
            reheatGeneration = 1000000;
        } else {
            reheatGeneration = 100000000;
        }

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
        Point u,v;
        int l,r;

        while(bestScore!=0){
            if(flag) return;
            generation += 1;
            int rowsize = 0,columnsize=0;
            if(bestScore<401&& bestScore>-401) {
                for (int i = 0; i < dimension; i++) {
                    if (rowScore[i] != 0) {
                        rowsize++;
                    }
                    if (columnScore[i] != 0) {
                        columnsize++;
                    }
                }
                if (rowsize < 3 && columnsize < 3) {
                    //row
                    ArrayList<Integer> row = rowNotEqual(rowScore);
                    ArrayList<Integer> column = rowNotEqual(columnScore);
                    int[] rtn = SearchOrderOne(row, true, rowScore, columnScore);
                    if (rtn[0] != -1&&!matrix.isFixed(rtn[0],rtn[1])&&!matrix.isFixed(rtn[2],rtn[3])) {
                        swap(rtn[0], rtn[1], rtn[2], rtn[3]);
                        int newFitness = currentScore;
                        int difference = magicSquare.matrix.getValue(rtn[0], rtn[2]) - magicSquare.matrix.getValue(rtn[2], rtn[3]);
                        if (rtn[0] != rtn[2]) {
                            newFitness = newFitness - abs(rowScore[rtn[0]]) + abs(rowScore[rtn[0]] - difference);
                            newFitness = newFitness - abs(rowScore[rtn[2]]) + abs(rowScore[rtn[2]] + difference);
                        }

                        if (rtn[1] != rtn[3]) {
                            newFitness = newFitness - abs(columnScore[rtn[2]]) + abs(columnScore[rtn[2]] - difference);
                            newFitness = newFitness - abs(columnScore[rtn[3]]) + abs(columnScore[rtn[3]] + difference);
                        }
                        int newLeftUpper = leftUpperDiagonal;
                        int newLeftLower = leftLowerDiagonal;

                        if (!(rtn[0] == rtn[1] && rtn[2] == rtn[3])) {
                            if (rtn[0] == rtn[1]) {
                                newLeftUpper = leftUpperDiagonal - difference;
                                newFitness = newFitness - abs(leftUpperDiagonal) + abs(newLeftUpper);
                            }

                            if (rtn[2] == rtn[3]) {
                                newLeftUpper = leftUpperDiagonal + difference;
                                newFitness = newFitness - abs(leftUpperDiagonal) + abs(newLeftUpper);
                            }
                        }

                        if (!((rtn[0] + rtn[1] == n - 1) && (rtn[2] + rtn[3] == n - 1))) {
                            if (rtn[0] + rtn[1] == n - 1) {
                                newLeftLower = leftLowerDiagonal - difference;
                                newFitness = newFitness - abs(leftLowerDiagonal) + abs(newLeftLower);
                            }

                            if (rtn[2] + rtn[3] == n - 1) {
                                newLeftLower = leftLowerDiagonal + difference;
                                newFitness = newFitness - abs(leftLowerDiagonal) + abs(newLeftLower);
                            }
                        }
                        bestScore = newFitness;
                        rowScore[rtn[0]] -= difference;
                        rowScore[rtn[2]] += difference;

                        columnScore[rtn[1]] -= difference;
                        columnScore[rtn[3]] += difference;

                        leftUpperDiagonal = newLeftUpper;
                        leftLowerDiagonal = newLeftLower;
                        continue;
                    }
                    //column
                    rtn = SearchOrderOne(column, false, rowScore, columnScore);
                    if (rtn[0] != -1&&!matrix.isFixed(rtn[0],rtn[1])&&!matrix.isFixed(rtn[2],rtn[3])) {
                        swap(rtn[0], rtn[1], rtn[2], rtn[3]);
                        //update bestscore
                        int newFitness = currentScore;
                        int difference = magicSquare.matrix.getValue(rtn[0], rtn[2]) - magicSquare.matrix.getValue(rtn[2], rtn[3]);
                        if (rtn[0] != rtn[2]) {
                            newFitness = newFitness - abs(rowScore[rtn[0]]) + abs(rowScore[rtn[0]] - difference);
                            newFitness = newFitness - abs(rowScore[rtn[2]]) + abs(rowScore[rtn[2]] + difference);
                        }

                        if (rtn[1] != rtn[3]) {
                            newFitness = newFitness - abs(columnScore[rtn[2]]) + abs(columnScore[rtn[2]] - difference);
                            newFitness = newFitness - abs(columnScore[rtn[3]]) + abs(columnScore[rtn[3]] + difference);
                        }
                        int newLeftUpper = leftUpperDiagonal;
                        int newLeftLower = leftLowerDiagonal;

                        if (!(rtn[0] == rtn[1] && rtn[2] == rtn[3])) {
                            if (rtn[0] == rtn[1]) {
                                newLeftUpper = leftUpperDiagonal - difference;
                                newFitness = newFitness - abs(leftUpperDiagonal) + abs(newLeftUpper);
                            }

                            if (rtn[2] == rtn[3]) {
                                newLeftUpper = leftUpperDiagonal + difference;
                                newFitness = newFitness - abs(leftUpperDiagonal) + abs(newLeftUpper);
                            }
                        }

                        if (!((rtn[0] + rtn[1] == n - 1) && (rtn[2] + rtn[3] == n - 1))) {
                            if (rtn[0] + rtn[1] == n - 1) {
                                newLeftLower = leftLowerDiagonal - difference;
                                newFitness = newFitness - abs(leftLowerDiagonal) + abs(newLeftLower);
                            }

                            if (rtn[2] + rtn[3] == n - 1) {
                                newLeftLower = leftLowerDiagonal + difference;
                                newFitness = newFitness - abs(leftLowerDiagonal) + abs(newLeftLower);
                            }
                        }
                        bestScore = newFitness;
                        rowScore[rtn[0]] -= difference;
                        rowScore[rtn[2]] += difference;

                        columnScore[rtn[1]] -= difference;
                        columnScore[rtn[3]] += difference;

                        leftUpperDiagonal = newLeftUpper;
                        leftLowerDiagonal = newLeftLower;
                        continue;
                    }
                }
            }


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
                newFitness = newFitness-abs(rowScore[u.rowIndex])+abs(rowScore[u.rowIndex]-difference);
                newFitness = newFitness-abs(rowScore[v.rowIndex])+abs(rowScore[v.rowIndex]+difference);
            }

            if(u.columnIndex!=v.columnIndex){
                newFitness = newFitness-abs(columnScore[u.columnIndex]) + abs(columnScore[u.columnIndex]-difference);
                newFitness = newFitness-abs(columnScore[v.columnIndex]) + abs(columnScore[v.columnIndex]+difference);
            }
            int newLeftUpper = leftUpperDiagonal;
            int newLeftLower = leftLowerDiagonal;

            if(!(u.rowIndex==u.columnIndex&&v.rowIndex==v.columnIndex)){
                if(u.rowIndex==u.columnIndex){
                    newLeftUpper = leftUpperDiagonal-difference;
                    newFitness = newFitness - abs(leftUpperDiagonal) + abs(newLeftUpper);
                }

                if(v.rowIndex==v.columnIndex){
                    newLeftUpper = leftUpperDiagonal+difference;
                    newFitness = newFitness - abs(leftUpperDiagonal) + abs(newLeftUpper);
                }
            }

            if(!((u.rowIndex+u.columnIndex==n-1)&&(v.rowIndex+v.columnIndex==n-1))){
                if(u.rowIndex+u.columnIndex==n-1){
                    newLeftLower = leftLowerDiagonal-difference;
                    newFitness = newFitness - abs(leftLowerDiagonal) + abs(newLeftLower);
                }

                if(v.rowIndex+v.columnIndex==n-1){
                    newLeftLower = leftLowerDiagonal+difference;
                    newFitness = newFitness - abs(leftLowerDiagonal) + abs(newLeftLower);
                }
            }

            int delta = newFitness-currentScore;
            if (delta<0){
                currentScore = newFitness;
                if(currentScore<bestScore){
                    bestScore = currentScore;
//                    if(bestScore==2){
//                        System.out.println(generation);
//                        return;
//                    }
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
            if(++cnt==reheatGeneration){//40: 100000000 20: 1000000
                T = MAX;
                magicSquare = shuffleMagicSquare();
                for(int i=0;i<n;i++){
                    rowScore[i] = magicSquare.getSumOfRow(i)- magicSquare.sum;
                }

                for(int i=0;i<n;i++){
                    columnScore[i] = magicSquare.getSumOfColumn(i)- magicSquare.sum;
                }

                leftLowerDiagonal = magicSquare.getSumOfLeftLowerDiagonal()- magicSquare.sum;
                leftUpperDiagonal = magicSquare.getSumOfLeftUpperDiagonal()- magicSquare.sum;

                bestScore = magicSquare.evaluateAll();

                currentScore = bestScore;
                setNotFixed = magicSquare.collectSetNotFixed();
            }
            T *= 0.9995;
        }

        Date end = new Date();
        flag = true;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                answer[i][j] =magicSquare.matrix.getValue(i,j);
            }
        }
        //System.out.println("Generation: "+generation);
/*        System.out.println(magicSquare.getMatrixInfo());*/
    }
    // calculate row or column which is not equal
    public ArrayList<Integer> rowNotEqual(int[] rowOrColumn){
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            if(rowOrColumn[i]!=0){
                res.add(i);
            }
        }
        return res;
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

    //TODO
    public int[] SearchOrderOne(ArrayList<Integer> arrayList, boolean row, int[] rowScore,int[] columnScore){
        int c2 = 2*getSum(dimension);
        if(row){
            for (int i = 0; i < arrayList.size()-1; i++) {
                for (int j = i+1; j < arrayList.size(); j++) {
                    if(rowScore[i]+rowScore[j]==c2){
                        int res= searchRowORColumn(arrayList.get(i),arrayList.get(j),true,rowScore[i] -c2/2);
                        if(res!=-1){
                            return new int[]{arrayList.get(i),res,arrayList.get(j),res};
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < arrayList.size()-1; i++) {
                for (int j = i+1; j < arrayList.size(); j++) {
                    if(columnScore[i]+columnScore[j]==c2&&c2/2-columnScore[i]!=0){
                        int res= searchRowORColumn(arrayList.get(i),arrayList.get(j),false,columnScore[i] -c2/2);
                        if(res!=-1){
                            return new int[]{res,arrayList.get(i),res,arrayList.get(j)};
                        }
                    }
                }
            }
        }
        return new int[]{-1,0,0,0};
    }

    public int searchRowORColumn(int line1, int line2,boolean row ,int value){
        if(row){
            for (int s = 0; s < dimension; s++) {
                if(matrix.getValue(line1,s)-matrix.getValue(line2,s)==value){
                    return s;
                }
            }
        } else {
            for (int s = 0; s < dimension; s++) {
                if(matrix.getValue(s,line1)-matrix.getValue(s,line2)==value){
                    return s;
                }
            }
        }
        return -1;
    }

    public int evaluateColumn(int columnIndex){
        return abs(sum-getSumOfColumn(columnIndex));
    }

    public int evaluateRow(int rowIndex){
        return abs(sum-getSumOfRow(rowIndex));
    }

    public int evaluateLeftUpperDiagonal(){
        return abs(sum-getSumOfLeftUpperDiagonal());
    }

    public int evaluateLeftLowerDiagonal(){
        return abs(sum-getSumOfLeftLowerDiagonal());
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
