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
        MagicSquare root = shuffleMagicSquare();
        MagicSquare nextGeneration = deepCopy(root);
        while(true) {
            int iterationTimes = 10;
            root = deepCopy(nextGeneration);
            int bestStage1 = root.evaluateRowsAndColumns();

            while(iterationTimes!=0){
                iterationTimes -= 1;
                MagicSquare magicSquare = deepCopy(root);
                ArrayList<Integer> rowList = magicSquare.collectRowsNotCorrect();
                ArrayList<Integer> columnList = magicSquare.collectColumnsNotCorrect();
                int n_row, n_col;
                n_row = rowList.size();
                n_col = columnList.size();
                if(n_row+n_col!=0){
                    /* phase 1 */
                    int currentScore = magicSquare.evaluateRowsAndColumns();
                    if(currentScore < 50 * magicSquare.dimension){
                        /* same row different columns */
                        for(int i=0;i<magicSquare.dimension;i++){
                            for(int j=0;j<magicSquare.dimension;j++){
                                if(i==j) continue;
                                for(int k=0;k<dimension;k++){
                                    if(magicSquare.matrix.isFixed(i,k)||magicSquare.matrix.isFixed(j,k)) continue;
                                    if(magicSquare.getSumOfRow(i)-sum==sum-magicSquare.getSumOfRow(j)
                                            &&sum-magicSquare.getSumOfRow(j)==magicSquare.matrix.getValue(i,k)-magicSquare.matrix.getValue(j,k)){
                                        magicSquare.swap(i,k,j,k);
                                    }
                                }
                            }
                        }
                        /* same column different row */
                        for(int i=0;i<magicSquare.dimension;i++){
                            for(int j=0;j<magicSquare.dimension;j++){
                                if(i==j) continue;
                                for(int k=0;k<dimension;k++){
                                    if(magicSquare.matrix.isFixed(k,i)||magicSquare.matrix.isFixed(k,j)) continue;
                                    if(magicSquare.getSumOfColumn(i)-sum==sum-magicSquare.getSumOfColumn(j)
                                            &&sum-magicSquare.getSumOfColumn(j)==magicSquare.matrix.getValue(k,i)-magicSquare.matrix.getValue(k,j)){
                                        magicSquare.swap(k,i,k,j);
                                    }
                                }
                            }
                        }
                        /* 2 Tuple same row different columns */
                        for(int k=0;k<magicSquare.dimension;k++){
                            for(int l=0;l<magicSquare.dimension;l++){
                                if(k==l) continue;
                                for(int s=0;s<magicSquare.dimension;s++){
                                    for(int t=0;t<magicSquare.dimension;t++){
                                        if(s==t) continue;
                                        if(magicSquare.matrix.isFixed(k,s)||magicSquare.matrix.isFixed(k,t)||
                                                magicSquare.matrix.isFixed(l,s)||magicSquare.matrix.isFixed(l,t)) continue;
                                        if(magicSquare.getSumOfRow(k)-sum==sum-magicSquare.getSumOfRow(l)
                                                &&sum-magicSquare.getSumOfRow(l)==magicSquare.matrix.getValue(k,s)+magicSquare.matrix.getValue(k,t)
                                        -magicSquare.matrix.getValue(l,s)-magicSquare.matrix.getValue(l,t)){
                                            magicSquare.swap(k,s,l,s);
                                            magicSquare.swap(k,t,l,t);
                                        }
                                    }
                                }
                            }
                        }

                        /* 2 Tuple same column different rows */
                        for(int k=0;k<magicSquare.dimension;k++){
                            for(int l=0;l<magicSquare.dimension;l++){
                                if(k==l) continue;
                                for(int s=0;s<magicSquare.dimension;s++){
                                    for(int t=0;t<magicSquare.dimension;t++){
                                        if(s==t) continue;
                                        if(magicSquare.matrix.isFixed(s,k)||magicSquare.matrix.isFixed(t,k)||
                                                magicSquare.matrix.isFixed(s,l)||magicSquare.matrix.isFixed(t,l)) continue;
                                        if(magicSquare.getSumOfColumn(k)-sum==sum-magicSquare.getSumOfColumn(l)
                                                &&sum-magicSquare.getSumOfColumn(l)==magicSquare.matrix.getValue(s,k)+magicSquare.matrix.getValue(t,k)
                                                -magicSquare.matrix.getValue(s,l)-magicSquare.matrix.getValue(t,l)){
                                            magicSquare.swap(s,k,s,l);
                                            magicSquare.swap(t,k,t,l);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    int rand = RandomUtil.getRandomInt(0,3);
                    switch (rand){
                        case 0:{
                            ArrayList<Integer> joinList = magicSquare.collectRowsNotCorrect();
                            joinList.retainAll(columnList);
                            if(joinList.size()==0) break;
                            HashMap<Integer,Point> s1 = magicSquare.collectS1(joinList);
                            HashMap<Integer,Point> s2 = magicSquare.collectS2(rowList,columnList);
                            double pm = 1.0/((n_row)*(n_col));
                            double sigma = 1.0*(magicSquare.evaluateRowsAndColumns())/(n_row+n_col);
                            for(int i=0;i< magicSquare.dimension;i++){
                                for(int j=0;j<magicSquare.dimension;j++){
                                    magicSquare.sig[i][j] = RandomUtil.getRandomInt(1,
                                            Math.max(2,(int)sigma+1));
                                }
                            }
                            for(Integer value: s1.keySet()){
                                double ran = Math.random();
                                if(ran<pm){
                                    /* mutation */
                                    int i = s1.get(value).rowIndex;
                                    int j = s1.get(value).columnIndex;
                                    int a_ij = value + RandomUtil.getRandomInt(0,
                                            2*magicSquare.sig[i][j]+1)-magicSquare.sig[i][j];
                                    if(a_ij<1) a_ij = RandomUtil.getRandomInt(1,dimension+1);
                                    if(a_ij>dimension*dimension) a_ij = dimension*dimension-RandomUtil.getRandomInt(0,dimension+1);
                                    Point p = null;
                                    int diff = 99999999;
                                    for(Integer v2: s2.keySet()){
                                        if(Math.abs(a_ij-v2)<diff){
                                            diff = Math.abs(a_ij-v2);
                                            p = s2.get(v2);
                                        }
                                    }
                                    assert p != null;
                                    magicSquare.swap(i,j,p.rowIndex,p.columnIndex);
                                    magicSquare.sig[i][j] += RandomUtil.getRandomInt(0,3)-1;
                                }
                            }
                            break;
                        }
                        case 1:{
                            HashMap<Integer,Point> s2 = magicSquare.collectS2(rowList,columnList);
                            double sigma = 1.0*(magicSquare.evaluateRowsAndColumns())/(n_row+n_col);
                            for(int i=0;i< magicSquare.dimension;i++){
                                for(int j=0;j<magicSquare.dimension;j++){
                                    magicSquare.sig[i][j] = RandomUtil.getRandomInt(1,
                                            Math.max(2,(int)sigma+1));
                                }
                            }
                            if(n_row!=0){
                                double p_row = 1.0/(2.0* magicSquare.dimension*n_row);
                                for(int rowIndex: rowList){
                                    for(int j=0;j< magicSquare.dimension;j++){
                                        if(magicSquare.matrix.isFixed(rowIndex,j)) continue;
                                        double ran = Math.random();
                                        if(ran<p_row){
                                            int a_ij = magicSquare.matrix.getValue(rowIndex,j) + RandomUtil.getRandomInt(0,
                                                    2*magicSquare.sig[rowIndex][j]+1)-magicSquare.sig[rowIndex][j];
                                            if(a_ij<1) a_ij = RandomUtil.getRandomInt(1,dimension+1);
                                            if(a_ij>dimension*dimension) a_ij = dimension*dimension-RandomUtil.getRandomInt(0,dimension+1);
                                            Point p = null;
                                            int diff = 99999999;
                                            for(Integer v2: s2.keySet()){
                                                if(Math.abs(a_ij-v2)<diff){
                                                    diff = Math.abs(a_ij-v2);
                                                    p = s2.get(v2);
                                                }
                                            }
                                            assert p != null;
                                            magicSquare.swap(rowIndex,j,p.rowIndex,p.columnIndex);
                                            magicSquare.sig[rowIndex][j] += RandomUtil.getRandomInt(0,3)-1;
                                        }
                                    }
                                }
                            }
                            if(n_col!=0){
                                double p_col = 1.0/(2.0* magicSquare.dimension*n_col);
                                for(int i=0;i<magicSquare.dimension;i++){
                                    for(int columnIndex:columnList){
                                        if(magicSquare.matrix.isFixed(i,columnIndex)) continue;
                                        double ran = Math.random();
                                        if(ran<p_col){
                                            int a_ij = magicSquare.matrix.getValue(i,columnIndex) + RandomUtil.getRandomInt(0,
                                                    2*magicSquare.sig[i][columnIndex]+1)-magicSquare.sig[i][columnIndex];
                                            if(a_ij<1) a_ij = RandomUtil.getRandomInt(1,dimension+1);
                                            if(a_ij>dimension*dimension) a_ij = dimension*dimension-RandomUtil.getRandomInt(0,dimension+1);
                                            Point p = null;
                                            int diff = 99999999;
                                            for(Integer v2: s2.keySet()){
                                                if(Math.abs(a_ij-v2)<diff){
                                                    diff = Math.abs(a_ij-v2);
                                                    p = s2.get(v2);
                                                }
                                            }
                                            assert p != null;
                                            magicSquare.swap(i,columnIndex,p.rowIndex,p.columnIndex);
                                            magicSquare.sig[i][columnIndex] += RandomUtil.getRandomInt(0,3)-1;
                                        }
                                    }

                                }
                            }
                            break;
                        }
                        case 2:{
                            HashMap<Integer,Point> s3 = magicSquare.collectSetNotFixed();
                            double sigma = 1.0*(magicSquare.evaluateRowsAndColumns())/(n_row+n_col);
                            for(int i=0;i< magicSquare.dimension;i++){
                                for(int j=0;j<magicSquare.dimension;j++){
                                    magicSquare.sig[i][j] = RandomUtil.getRandomInt(1,
                                            Math.max(2,(int)sigma+1));
                                }
                            }
                            if(n_row!=0){
                                double p_row = 1.0/(2.0* magicSquare.dimension*n_row);
                                for(int rowIndex: rowList){
                                    for(int j=0;j< magicSquare.dimension;j++){
                                        if(magicSquare.matrix.isFixed(rowIndex,j)) continue;
                                        double ran = Math.random();
                                        if(ran<p_row){
                                            int a_ij = magicSquare.matrix.getValue(rowIndex,j) + RandomUtil.getRandomInt(0,
                                                    2*magicSquare.sig[rowIndex][j]+1)-magicSquare.sig[rowIndex][j];
                                            if(a_ij<1) a_ij = RandomUtil.getRandomInt(1,dimension+1);
                                            if(a_ij>dimension*dimension) a_ij = dimension*dimension-RandomUtil.getRandomInt(0,dimension+1);
                                            Point p = null;
                                            int diff = 99999999;
                                            for(Integer v2: s3.keySet()){
                                                if(Math.abs(a_ij-v2)<diff){
                                                    diff = Math.abs(a_ij-v2);
                                                    p = s3.get(v2);
                                                }
                                            }
                                            assert p != null;
                                            magicSquare.swap(rowIndex,j,p.rowIndex,p.columnIndex);
                                            magicSquare.sig[rowIndex][j] += RandomUtil.getRandomInt(0,3)-1;
                                        }
                                    }
                                }
                            }
                            if(n_col!=0){
                                double p_col = 1.0/(2.0* magicSquare.dimension*n_col);
                                for(int i=0;i<magicSquare.dimension;i++){
                                    for(int columnIndex:columnList){
                                        if(magicSquare.matrix.isFixed(i,columnIndex)) continue;
                                        double ran = Math.random();
                                        if(ran<p_col){
                                            int a_ij = magicSquare.matrix.getValue(i,columnIndex) + RandomUtil.getRandomInt(0,
                                                    2*magicSquare.sig[i][columnIndex]+1)-magicSquare.sig[i][columnIndex];
                                            if(a_ij<1) a_ij = RandomUtil.getRandomInt(1,dimension+1);
                                            if(a_ij>dimension*dimension) a_ij = dimension*dimension-RandomUtil.getRandomInt(0,dimension+1);
                                            Point p = null;
                                            int diff = 99999999;
                                            for(Integer v2: s3.keySet()){
                                                if(Math.abs(a_ij-v2)<diff){
                                                    diff = Math.abs(a_ij-v2);
                                                    p = s3.get(v2);
                                                }
                                            }
                                            assert p != null;
                                            magicSquare.swap(i,columnIndex,p.rowIndex,p.columnIndex);
                                            magicSquare.sig[i][columnIndex] += RandomUtil.getRandomInt(0,3)-1;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        default:{
                            break;
                        }
                    }

                    currentScore = magicSquare.evaluateRowsAndColumns();
                    if(currentScore<bestStage1){
                        System.out.println("Best Score: "+currentScore);
                        bestStage1 = currentScore;
                        nextGeneration = deepCopy(magicSquare);
                        if(bestStage1==0){
                            System.out.println(magicSquare.getMatrixInfo());
                            return;
                        }
                    }

                }else{
                    /* phase 2*/
                    int currentScore = magicSquare.evaluateLeftLowerDiagonal()+magicSquare.evaluateLeftUpperDiagonal();

                }
            }

        }

    }

    public HashMap<Integer,Point> collectSetNotFixed(){
        HashMap<Integer,Point> res = new HashMap<>();
        for(int i=0;i<dimension;i++){
            for(int j=0;j<dimension;j++){
                if(matrix.isFixed(i,j)) continue;
                res.put(matrix.getValue(i,j),new Point(i,j));
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
