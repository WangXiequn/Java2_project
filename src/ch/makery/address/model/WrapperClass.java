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
    int M;

    public WrapperClass(){

    }

    public void setMagicSquareWrapperClass( int[][] dataBefore, int[][] magicSquare){
        M = 1;
        this.dataBefore = dataBefore;
        dataUser = magicSquare;

        dataSolution = new int[][]{{0}};
    }

    public void SetSudokuWrapperClass(int[][] dataBefore,int[][] dataUser,Integer[][] computerSolution){
        this.M = 0;
        this.dataBefore = dataBefore;
        this.dataUser = dataUser;
        this.dataSolution = new int[computerSolution.length][computerSolution.length];
        for (int i = 0; i < computerSolution.length; i++) {
            for (int j = 0; j < computerSolution.length; j++) {
                dataSolution[i][j] = computerSolution[i][j];
            }
        }

    }
    @XmlElement(
            name = "dataBefore"
    )
    public int[][] getDataBefore(){return dataBefore;}
    @XmlElement(
            name = "dataSolution"
    )
    public int[][] getDataSolution() {
        return dataSolution;
    }
    @XmlElement(
            name = "dataUser"
    )
    public int[][] getDataUser() {
        return dataUser;
    }
    @XmlElement(
            name = "M"
    )
    public int getM(){
        return M;
    }

    public void setDataBefore(int[][] dataBefore) {
        this.dataBefore = new int[dataBefore.length][dataBefore.length];
        for (int i = 0; i < dataBefore.length; i++) {
            for (int j = 0; j < dataBefore[0].length; j++) {
                this.dataBefore[i][j] = dataBefore[i][j];
            }
        }
    }

    public void setDataSolution(int[][] dataSolution){
        this.dataSolution = new int[dataSolution.length][dataSolution.length];
        for (int i = 0; i < dataSolution.length; i++) {
            for (int j = 0; j < dataSolution.length; j++) {
                this.dataSolution[i][j] = dataSolution[i][j];
            }
        }
    }

    public void setDataUser(int[][] dataUser){
        this.dataUser = new int[dataUser.length][dataUser.length];
        for (int i = 0; i < dataUser.length; i++) {
            for (int j = 0; j < dataUser.length; j++) {
                this.dataUser[i][j] = dataUser[i][j];
            }
        }
    }

    public void setM(int M){
        this.M = M;
    }

}
