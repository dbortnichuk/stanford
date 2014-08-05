package com.dbortnichuk.algorithms.sort;

/**
 * User: dbortnichuk
 * Date: 4/27/14
 */
public class BubbleSort {

    public int[] sort(int[] input) {

        int swapVar;
        int n = input.length;
        for (int i = 0; i < (n - 1); i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (input[j] > input[j + 1]) /* For descending order use < */ {
                    swapVar = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = swapVar;
                }
            }
        }
        return input;
    }

    public static BubbleSort newInstance() {
        return new BubbleSort();
    }
}
