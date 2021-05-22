package ch.makery.address.model;

import ch.makery.address.view.SudokuOverviewController;
import ch.makery.address.view.SudokuOverviewController.*;


import java.util.*;
import java.util.stream.IntStream;
public class SudokuGenerator {
    //<editor-fold defaultstate="collapsed" desc="Level">
    public static int Dimension= SudokuOverviewController.Dimension;
    /**
     * 3*3 : (42 to 51) Cells
     */
    public static final int EASY = 42;
    /**
     * 4*4 : (32 to 41) Cells
     */
    public static final int MEDIUM = 150;
    /**
     * 5*5 : (22 to 31) Cells
     */
    public static final int HARD = 400;
    public static int[][] sudoku;

    public static void setSudoku(int[][] arr){
        sudoku = new int[arr.length][arr.length];
        for (int i = 0; i <arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                sudoku[i][j] = arr[i][j];
            }
        }
    }

    public void MakeSudoku(int Level) {
        // Create Complete Sudoku :D
        generateCompleteSudoku();

        sudoku=solver(sudoku); // 解数独
        RegeneratedSudoku(sudoku, sudoku.length);
        for (int i=0;i< sudoku.length;i++){
            for (int j=0;j<sudoku[i].length;j++){
                SudokuOverviewController.computerSolution[i][j]=sudoku[i][j];
            }
        }

        removecell(Level);
        for (int i=0;i< sudoku.length;i++){
            for (int j=0;j<sudoku[i].length;j++){
                SudokuOverviewController.user[i][j]=sudoku[i][j];
            }
        }



    }
    public static void RegeneratedSudoku(int [][] matrix,int size){
        int [] randomList = new int[matrix.length];
        for (int i = 0; i <matrix.length ; i++) {
            randomList[i] = i+1;
        }
        Random random = new Random();
        for (int i = 0; i < randomList.length; i++) {
            int index = random.nextInt(size);
            int temp = randomList[index];
            randomList[index] = randomList[0];
            randomList[0] = temp;

        }
        HashMap<Integer,Integer> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            map.put(randomList[i],i+1);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = map.get(matrix[i][j]);
            }
        }
    }

    //challengemode overide
    public void MakeSudoku() {
        // Create Complete Sudoku :D
        generateCompleteSudoku();

        //solver(sudoku); // 解数独
        for (int i=0;i< sudoku.length;i++){
            for (int j=0;j<sudoku[i].length;j++){
                SudokuOverviewController.computerSolution[i][j]=sudoku[i][j];
            }
        }

        //removecell(Level);
        for (int i=0;i< sudoku.length;i++){
            for (int j=0;j<sudoku[i].length;j++){
                SudokuOverviewController.user[i][j]=sudoku[i][j];
            }
        }

    }


    public void generateCompleteSudoku(){

        sudoku=new int[SudokuOverviewController.Dimension][SudokuOverviewController.Dimension];
        for (int i=0;i<sudoku.length;i++){
            for (int j=0;j< sudoku[i].length;j++){
                sudoku[i][j]=0;
            }
        }
    }
    public void removecell(int Level){
        int RowIndex, ColumnIndex; // To store Cell Location.
        int [] arr = new int[sudoku.length* sudoku.length];
        for (int i = 0; i < sudoku.length ; i++) {
            for (int j = 0; j < sudoku.length; j++) {
                arr[i* sudoku.length+j]=sudoku[i][j];
            }
        }
        Random rm=new Random();
        Set<Integer> hashSet = new HashSet<Integer>();
        while (hashSet.size() != Level) {
            hashSet.add(rm.nextInt(sudoku.length* sudoku.length));
        }

        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            arr[(int)it.next()]=0;
        }

//        for (int i=0;i<Level;i++){
//            RowIndex=(int)(Math.random()*(Dimension*Dimension));
//            while (arr[RowIndex]==0){
//                if (RowIndex==Dimension*Dimension-1){
//                    RowIndex=0;
//                }else {
//                    RowIndex++;
//                }
//            }
//            arr[RowIndex]=0;
//        }

        for (int i = 0; i < sudoku.length ; i++) {
            for (int j = 0; j < sudoku.length; j++) {
                sudoku[i][j]= arr[i* sudoku.length+j];
            }
        }

    }

    public static int[][] solver(int[][] temp){//solve
        int size = temp.length;
        size = (int)Math.sqrt(size);
        int [] arr = new int[temp.length* temp.length];
        for (int i = 0; i < temp.length ; i++) {
            for (int j = 0; j < temp.length; j++) {
                arr[i* temp.length+j]=temp[i][j];
            }
        }
        Sudoku sudoku1 = new Sudoku(size,arr);

        return sudoku1.DancingLink();


    }


}
class Node{
    int index;
    int left;
    int right;
    int up;
    int down;
    int rowNum;
    int colNum;
}
class Sudoku {
    public int[] org;
    public int size;
    public boolean[][] s;
    public boolean []chose,cover;
    public int[] cnt;
    HashMap<Integer,Node> index;
    int count;
    int colLen;
    int gridSize;
    boolean [] judge;
    Sudoku(int size,int[] org){
        this.size=size;
        this.org=org;
        int temp = size*size;
        this.gridSize=size*size;
        s = new boolean[gridSize*gridSize*gridSize+1][gridSize*gridSize*4+1];
        index = new HashMap<>();
        this.colLen=temp*temp*4;
        chose = new boolean[gridSize*gridSize*gridSize+1];
        cover = new boolean[colLen+1];
        judge = new boolean[colLen+1];
        cnt = new int[colLen+1];
        build();
    }
    public void build(){
        int count = 1;
        for (int i = 0; i < org.length; i++) {
            int rowID=i/(gridSize);
            int colID=i%(gridSize);
            int blockID=(rowID/size)*size+(colID/size);
            if (org[i]!=0){
                int rowNum =gridSize*gridSize+rowID*gridSize+org[i];
                int colNum = gridSize*gridSize*2+colID*gridSize+org[i];
                int gridNum = gridSize*gridSize*3+blockID*gridSize+org[i];
                s[count][i+1] = true;
                s[count][rowNum]=true;
                s[count][colNum]=true;
                s[count][gridNum]=true;
                judge[rowNum]=true;
                judge[colNum]=true;
                judge[gridNum]=true;
                count++;
            }
        }
        for (int i = 0; i < org.length; i++) {
            int rowID =i/(gridSize);
            int colID =i%(gridSize);
            int blockID=(rowID/size)*size+(colID/size);

            if (org[i]==0){
                for (int j = 0; j <gridSize ; j++) {
                    int rowNum =gridSize*gridSize+rowID*gridSize+j+1;
                    int colNum = gridSize*gridSize*2+colID*gridSize+j+1;
                    int gridNum = gridSize*gridSize*3+blockID*gridSize+j+1;
                    if (!judge[rowNum]&&!judge[colNum]&&!judge[gridNum]){
                        s[count][i+1] = true;
                        s[count][gridSize*gridSize+rowID*gridSize+j+1]=true;
                        s[count][gridSize*gridSize*2+colID*gridSize+j+1]=true;
                        s[count][gridSize*gridSize*3+blockID*gridSize+j+1]=true;
                        count++;
                    }
                }
            }
        }
        this.count=count;
    }
    public void buildLink(){
        int temp = colLen;
        for (int i = 0; i <= temp; i++) {
            Node node = new Node();
            node.rowNum=0;
            node.colNum =i;
            node.index=i;
            node.up=i;
            node.down=i;
            node.left = (i > 0) ? (i - 1) : temp;
            node.right = (i<temp)?(i+1) : 0;
            index.put(i,node);
            s[0][i]=true;
        }
        temp++;
        for (int i = 1; i < count; i++) {
            for (int j = 1; j < temp; j++) {
                if (s[i][j]){
                    Node node = new Node();
                    node.rowNum=i;
                    node.colNum=j;
                    node.index=i*temp+j;
                    cnt[j]++;
                    for (int k = (i+1>=count)?0:i+1;; k = (k + 1 >= count) ? 0 : k + 1) {
                        if (s[k][j]){
                            node.down=k*temp+j;
                            if (k==0){
                                index.get(j).up=i*temp+j;
                            }
                            break;
                        }
                    }
                    for (int k = i-1;; k = (k - 1 < 0) ? count-1: k - 1) {
                        if (s[k][j]){
                            node.up=k*temp+j;
                            if (k==0){
                                index.get(j).down=i*temp+j;
                            }
                            break;
                        }
                    }
                    for (int k = (j+1>=temp)?0:j+1;; k = (k + 1 >= temp) ? 0: k+1) {
                        if (s[i][k]){
                            node.right=i*temp+k;
                            break;
                        }
                    }
                    for (int k = (j-1<1)?temp-1:j-1;; k = (k - 1 < 0) ? temp-1 : k - 1) {
                        if (s[i][k]){
                            node.left=i*temp+k;
                            break;
                        }
                    }
                    index.put(i*temp+j,node);
                }
            }
        }
    }
    public int getSmallest(){
        int a =1;
        int answer = Integer.MAX_VALUE;
        for (int i=1; i <cnt.length ; i++) {
            if (!cover[i]){
                if (answer>cnt[i]){
                    answer=cnt[i];
                    a = i;
                }
            }
        }
        return a;
    }
    public boolean Dance(int x){
        if (index.get(0).left==0){
            return true;
        }
        remove(x);
        for (int p = index.get(x).down; p != x; p = index.get(p).down) {

            chose[index.get(p).rowNum] = true;
            for (int q = index.get(p).right; q != p; q = index.get(q).right) {
                remove(index.get(q).colNum);
            }

            //下一个元素x+1
            if (Dance(getSmallest())) {
                return true;
            }
            chose[index.get(p).rowNum] = false;
            for (int q = index.get(p).left; q != p; q = index.get(q).left) {
                resume(index.get(q).colNum);
            }
        }
        resume(x);
        return false;
    }
    public void resume(int i) {
        //在头节点的一行恢复节点i
        index.get(index.get(i).right).left=i;
        index.get(index.get(i).left).right=i;

        cover[index.get(i).colNum]=false;
        //所有包含i的子集
        for (int p = index.get(i).up; p != i; p = index.get(p).up) {
            //在列上恢复子集上除了i的其他节点
            for (int q = index.get(p).right; q != p; q = index.get(q).right) {
                cnt[index.get(q).colNum]++;
                index.get(index.get(q).down).up=q;
                index.get(index.get(q).up).down=q;
            }
        }
    }

    public void remove(int i){
        index.get(index.get(i).right).left=index.get(i).left;
        index.get(index.get(i).left).right=index.get(i).right;

        cover[i]=true;
        for (int p = index.get(i).down; p != i; p = index.get(p).down) {
            for (int q = index.get(p).right; q != p; q = index.get(q).right) {
                cnt[index.get(q).colNum]--;
                index.get(index.get(q).down).up=index.get(q).up;
                index.get(index.get(q).up).down=index.get(q).down;

            }
        }
    }


    public int [][] DancingLink(){
        buildLink();
        boolean dance = Dance(1);
        System.out.println(dance);
        int [][] answer = new int[gridSize][gridSize];
        for (int i = 0; i < chose.length; i++) {
            if (chose[i]){
                int t1=0,t2=0;
                for (int j = 1; j < s[i].length; j++) {
                    if (j<=gridSize*gridSize&&s[i][j]){
                        t1=j;
                    }
                    if (j>gridSize*gridSize&&s[i][j]){
                        t2=j-gridSize*gridSize;
                    }
                }
                int row = (t1-1)/(gridSize);
                int col = (t1-1)%(gridSize);
                t2%=(gridSize);
                if (t2==0){
                    t2=gridSize;
                }
                answer[(int)row][(int)col]=(int)t2;
            }
        }
        return answer;
    }
}