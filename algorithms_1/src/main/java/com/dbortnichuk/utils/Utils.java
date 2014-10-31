package com.dbortnichuk.utils;

import java.util.List;

/**
 * User: dbortnichuk
 * Date: 8/19/14
 */
public class Utils {

    //including start\end
    public static int getRandomInt(int start, int end){
        return start + (int)(Math.random() * ((end - start) + 1));
    }

    public static int getMin(List<Integer> input){
        int minValue = Integer.MAX_VALUE;
        for (int number : input) {
            if (number < minValue) {
                minValue = number;
            }
        }
        return minValue;
    }


}
