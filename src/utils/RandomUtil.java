package utils;

import matrix.dataformat.Point;

import java.util.Random;

public class RandomUtil {
    private static Random random = new Random();

    public static int getRandomInt(int lowerBound, int upperBound){
        return random.nextInt(upperBound-lowerBound)+lowerBound;
    }

    public static Point getRandomUpperLeftPoint(int dimension, int numOfFixedData){
        int rowIndex = RandomUtil.getRandomInt(0,dimension-numOfFixedData);
        int columnIndex = RandomUtil.getRandomInt(0,dimension-numOfFixedData);
        return new Point(rowIndex,columnIndex);
    }

}
