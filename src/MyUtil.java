import ch.makery.address.MainApp;
import ch.makery.address.view.SudokuOverviewController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Manswer0(int k) return int[][]生成一个n*n magic square 的 solution  only 适用于奇数阶幻方 n>=3
 * Manswer1(int k) return int[][] 生成一个n*n magic square 的 solution  only 适用于偶数数阶幻方 (不能被4整除) ex:6,10
 * Manswer2(int k) return int[][] 生成一个n*n magic square 的 solution  only 适用于偶数数阶幻方 (能被4整除) ex:4,8
 * MagicSum(int n) return int 返回 n 阶 sum of row=sum(col)=sum(对角线)
 * Error(int[][]a,int[][]b) return int 返回 a 和 b 不同的个数
 * int[][] MagicRandom(int n) 返回n*n 乱序的matrices
 * printSquare(int[][]) 便于检测
 */
public class MyUtil {
    public static int[][] sudoku = {//test 数独用例
            {8, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 3, 6, 0, 0, 0, 0, 0},
            {0, 7, 0, 0, 9, 0, 2, 0, 0},
            {0, 5, 0, 0, 0, 7, 0, 0, 0},
            {0, 0, 0, 0, 4, 5, 7, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 3, 0},
            {0, 0, 1, 0, 0, 0, 0, 6, 8},
            {0, 0, 8, 5, 0, 0, 0, 1, 0},
            {0, 9, 0, 0, 0, 0, 4, 0, 0}};
    public  int[][] matrix;// result matrix

    public static void main(String[] args) {

    }

    //reference:https://blog.csdn.net/cgsyck/article/details/89384465
    //生成一个n*n magic square 的 solution  only 适用于奇数阶幻方 n>=3
    public static int[][]Manswer0(int k){
        int[][] res = new int[k][k];
        int x = 0;
        int y = k/2;
        int total = k*k;
        for (int i = 1; i <= total; i++) {
            res[x][y] = i;
            int m = (x-1+k)%k;
            int n = (y+1)%k;
            if (res[m][n]>0) {
                x = (x+1)%k;
            }else{
                x= m;
                y= n;
            }
        }

        return res;

    }

    //生成一个n*n magic square 的 solution  only 适用于偶数数阶幻方 (不能被4整除) ex:6,10
    public static int[][]Manswer1(int k){
        int[][] res = new int[k][k];
        int x = 0;
        int y = k/2-1;
        int total = k*k/4;
        for (int i = 1; i <= total; i++) {
            int type = 1;
            if ((x== k/2+1 && y!=k/2-1) || (x==k/2-1 && y==k/2-1)) {
                type = 0;
            }

            if (x>= k/2+3) {
                type = -1;
            }
//填充
            switch (type) {
                case 1:
                    res[x][y] = 4*i;
                    res[x][y+1] = 4*i-3;
                    res[x+1][y] = 4*i-2;
                    res[x+1][y+1] = 4*i-1;
                    break;
                case 0:
                    res[x][y] = 4*i-3;
                    res[x][y+1] = 4*i;
                    res[x+1][y] = 4*i-2;
                    res[x+1][y+1] = 4*i-1;
                    break;
                default:
                    res[x][y] = 4*i-3;
                    res[x][y+1] = 4*i;
                    res[x+1][y] = 4*i-1;
                    res[x+1][y+1] = 4*i-2;
                    break;
            }
//定位
            int m = (x-2+k)%k;
            int n = (y+2)%k;
            if (res[m][n]>0) {
                x = (x+2)%k;
            }else{
                x= m;
                y= n;
            }
        }
        return res;
    }

    //生成一个n*n magic square 的 solution  only 适用于偶数数阶幻方 (能被4整除) ex:4,8
    public static int[][]Manswer2(int k){
        int[][] res = new int[k][k];
//分割数据临时数组
        int[] temp = new int[k*k/2];
        int index = 0;
//预填充
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
//符合分割条件
                int m = i%4;
                int n = j%4;
                if (m!=n && m+n!=3) {
                    temp[index++] = i*k+j+1;
                    res[i][j]= 0;
                }else{
                    res[i][j] = i*k+j+1;
                }
            }
        }
//分割出的数据排序
        Arrays.sort(temp);
        int tempIndex = temp.length-1;
//再填充
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (res[i][j]==0) {
                    res[i][j] = temp[tempIndex--];
                }
            }
        }
        return res;
    }

    // n is the dimension of the square
    public static int MagicSum(int n){
        return (n*(n*n+1)/2);
    }


    //print square
    public static void printSquare(int[][] array){
        for(int i=0;i<array.length;i++){
            for (int j=0;j<array[i].length;j++){
                System.out.print(array[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }
    public static int Error(int[][]a,int[][]b){
        int count=0;
        for(int i=0;i<a.length;i++){
            for (int j=0;j<a[i].length;j++){
                if (a[i][j] !=b[i][j]){
                    count+=1;
                }
            }
        }
        return count;
    }

    public static int[][] MagicRandom(int n){
        int [][]target=new int[n][n];
        ArrayList<Integer> t=new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                int accRandom=r.nextInt(n*n)+1;
                if (i==0 && j==0){
                    target[i][j]=accRandom;
                    t.add(accRandom);
                    continue;
                }

                while (t.contains(accRandom)){//除去重复的
                    accRandom=r.nextInt(n*n)+1;
                }
                target[i][j]=accRandom;
                t.add(accRandom);
            }

        }
        return target;
    }



}
