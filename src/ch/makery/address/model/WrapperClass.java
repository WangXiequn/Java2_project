package ch.makery.address.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(
        name = "Game"
)
public class WrapperClass {
    int[][] dataBefore;
    int[][] dataSolution;
    int[][] dataUser;
    boolean isMagicSquare;
/*    boolean[][] isFixed;*/
    String time;

    public WrapperClass(){

    }

    public void setMagicSquareWrapperClass( int[][] fixeddata, int[][] magicSquare,String time){
        isMagicSquare = true;
        dataBefore = fixeddata;
        this.time = time;
        dataUser = magicSquare;
        //
        dataSolution = new int[][]{{0}};
    }

    public void SetSudokuWrapperClass(int[][] dataBefore,int[][] dataUser,Integer[][] computerSolution,String time){
        this.isMagicSquare = false;
        this.dataBefore = dataBefore;
        this.dataUser = dataUser;
        this.time = time;
        this.dataSolution = new int[computerSolution.length][computerSolution.length];
        for (int i = 0; i < computerSolution.length; i++) {
            for (int j = 0; j < computerSolution.length; j++) {
                dataSolution[i][j] = computerSolution[i][j];
            }
        }

    }
    @XmlElement(
            name = "numberData"
    )
    public int[][] getDataBefore(){return this.dataBefore;}
    @XmlElement()
    public int[][] getDataSolution() {
        return dataSolution;
    }
    @XmlElement
    public int[][] getDataUser() {
        return dataUser;
    }
    @XmlElement
    public String getTime() {
        return time;
    }
    @XmlElement
    boolean getIsMagicSquare(){
        return isMagicSquare;
    }

   /* @XmlElement(
            name ="isFixed"
    )
    public boolean[][] getIsFixed(){ return this.isFixed;}*/
}
