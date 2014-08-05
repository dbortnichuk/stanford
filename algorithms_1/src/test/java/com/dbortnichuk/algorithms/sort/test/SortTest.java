package com.dbortnichuk.algorithms.sort.test;

import com.dbortnichuk.algorithms.sort.BubbleSort;
import com.dbortnichuk.algorithms.sort.MergeSort;
import com.dbortnichuk.exception.PerformanceEvaluatorException;
import com.dbortnichuk.utils.PerformanceEvaluator;
import com.dbortnichuk.utils.Utils;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * User: dbortnichuk
 * Date: 4/27/14
 */

public class SortTest {

    private PerformanceEvaluator performanceEvaluator = PerformanceEvaluator.newInstance();

    //@Ignore
    @Test
    public void mergeSortTest() throws PerformanceEvaluatorException {
        int inputLength = 1000000;

        int[] input = new int[inputLength];
        for(int i = 0; i < inputLength; i++){
            //input[i] = i;
            input[i] = (int)(Math.random() * 50);
        }

        //Utils.printIntArray(input, "Before sort:");

        performanceEvaluator.start();

        MergeSort mergeSort = MergeSort.newInstance();
        mergeSort.sort(input);

        performanceEvaluator.stop();

        //Utils.printIntArray(input, "After sort: ");

    }

    @Ignore
    @Test
    public void bubbleSortTest() throws PerformanceEvaluatorException {
        int inputLength = 1000000;

        int[] input = new int[inputLength];
        for(int i = 0; i < inputLength; i++){
            //input[i] = i;
            input[i] = (int)(Math.random() * 50);
        }

        //Utils.printIntArray(input, "Before sort:");

        performanceEvaluator.start();

        BubbleSort mergeSort = BubbleSort.newInstance();
        mergeSort.sort(input);

        performanceEvaluator.stop();

        //Utils.printIntArray(input, "After sort: ");


    }

}
