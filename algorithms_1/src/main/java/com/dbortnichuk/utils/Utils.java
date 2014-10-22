package com.dbortnichuk.utils;

/**
 * User: dbortnichuk
 * Date: 8/19/14
 */
public class Utils {

    //including start\end
    public static int getRandomInt(int start, int end){
        return start + (int)(Math.random() * ((end - start) + 1));
    }


}
