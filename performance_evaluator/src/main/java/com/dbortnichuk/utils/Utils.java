package com.dbortnichuk.utils;

import com.dbortnichuk.utils.Constants;

/**
 * User: dbortnichuk
 * Date: 5/2/14
 */
public class Utils {


    public static void printIntArray(int[] arrayToPrint, String message) {

        if (message == null) {
            message = Constants.STRING_EMPTY;
        }
        System.out.println(message);
        for (int i = 0; i < arrayToPrint.length; i++) {
            System.out.print(arrayToPrint[i] + Constants.STRING_SPACE);
        }
        System.out.println(Constants.STRING_EMPTY);

    }

}
