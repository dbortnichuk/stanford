package com.dbortnichuk.algorithms.sort;

import com.dbortnichuk.exception.PerformanceEvaluatorException;
import com.dbortnichuk.utils.Constants;
import com.dbortnichuk.utils.PerformanceEvaluator;

/**
 * User: dbortnichuk
 * Date: 4/23/14
 */
public class MergeSort {


    private static final int LENGTH = 50;
    private static final int MAX = 50;

    private static int[] input = new int[LENGTH];

    static{
        for (int i=0; i < input.length; i++){
             input[i] = (int)(Math.random()*MAX);
        }
    }

    public static void main(String[] args) throws PerformanceEvaluatorException, InterruptedException {
        PerformanceEvaluator evaluator = new PerformanceEvaluator();
        evaluator.point();

        for (int i = 0; i < Constants.KILO * Constants.KILO; i++){
             new PerformanceEvaluator();
             if(i == 200000 ){
                 evaluator.point("index: " + i);
             } else if(i == 500000){
                 evaluator.point("index: " + i);
             } else if(i == 900000){
                 evaluator.point("index: " + i);
             }
        }

        evaluator.stop();




    }

}
