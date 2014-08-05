package com.dbortnichuk.algorithms.sort;

import java.io.FileNotFoundException;

/**
 * User: dbortnichuk
 * Date: 4/23/14
 */
public class MergeSort {

    public  int[] sort(int[] input) {

        if (input.length <= 1) {
            return input;
        }

        // Split the array in half
        int[] first = new int[input.length / 2];
        int[] second = new int[input.length - first.length];
        System.arraycopy(input, 0, first, 0, first.length);
        System.arraycopy(input, first.length, second, 0, second.length);

        // Sort each half
        sort(first);
        sort(second);

        // Merge the halves together, overwriting the original array
        merge(first, second, input);
        return input;
    }

    private void merge(int[] first, int[] second, int [] result) {
        // Merge both halves into the result array
        // Next element to consider in the first array
        int iFirst = 0;
        // Next element to consider in the second array
        int iSecond = 0;

        // Next open position in the result
        int j = 0;
        // As long as neither iFirst nor iSecond is past the end, move the
        // smaller element into the result.
        while (iFirst < first.length && iSecond < second.length) {
            if (first[iFirst] < second[iSecond]) {
                result[j] = first[iFirst];
                iFirst++;
            } else {
                result[j] = second[iSecond];
                iSecond++;
            }
            j++;
        }
        // copy what's left
        System.arraycopy(first, iFirst, result, j, first.length - iFirst);
        System.arraycopy(second, iSecond, result, j, second.length - iSecond);
    }

    public static MergeSort newInstance(){
        return new MergeSort();
    }
}
