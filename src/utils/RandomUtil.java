package utils;

import java.util.Random;

public class RandomUtil {
    private static Random random = new Random();

    public static int getRandomInt(int lowerBound, int upperBound){
        return random.nextInt(upperBound-lowerBound)+lowerBound;
    }

}
