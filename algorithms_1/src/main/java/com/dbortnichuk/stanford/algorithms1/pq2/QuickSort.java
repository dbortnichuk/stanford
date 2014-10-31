package com.dbortnichuk.stanford.algorithms1.pq2;

import com.dbortnichuk.exception.PerformanceEvaluatorException;
import com.dbortnichuk.utils.PerformanceEvaluator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: dbortnichuk
 * Date: 8/8/14
 */
public class QuickSort {

    List<Integer> data;
    long comparisons = -1;
    long comparisonsOneByOne = -1;

    public QuickSort(String filePath) {
        BufferedReader reader = null;
        try {
            URL fileURL = getClass().getResource(filePath);
            String absoluteFilePath = fileURL.getPath();
            reader = new BufferedReader(new FileReader(absoluteFilePath));
            String line = null;
            int lineNumber = 0;
            data = new ArrayList<Integer>();
            List<String> invalidData = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                try {
                    Integer number = Integer.parseInt(line);
                    data.add(number);
                } catch (IllegalArgumentException e) {
                    invalidData.add(line);
                }
                lineNumber++;
            }
//            System.out.println("Valid input size: " + data.size());
//            System.out.println("Valid input: " + data);
//            System.out.println();
//            System.out.println("Invalid input size: " + invalidData.size());
//            System.out.println("Invalid input: " + invalidData);
//            System.out.println();
//            System.out.println("Total input size: " + lineNumber);
//            System.out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Integer> getDataAsList() {
        return data;
    }

    public Integer[] getDataAsArray() {
        return data.toArray(new Integer[]{});
    }

    public long getComparisons() {
        if (comparisons < 0) {
            System.out.println("Data hasn't been sorted");
            return comparisons;
        }
        return comparisons;
    }

    public long getComparisonsOneByOne() {
        if (comparisonsOneByOne < 0) {
            System.out.println("Data hasn't been sorted");
            return comparisonsOneByOne;
        }
        return comparisonsOneByOne;
    }

    public static void main(String[] args) throws PerformanceEvaluatorException {
        PerformanceEvaluator performanceEvaluator = PerformanceEvaluator.newInstance();

        QuickSort quickSort = new QuickSort("/pq2q1q2q3.txt");
        Integer[] inputData = quickSort.getDataAsArray();

        performanceEvaluator.start();
        Integer[] output = quickSort.sort(inputData, 0, inputData.length - 1);
        performanceEvaluator.stop();

        System.out.println(Arrays.asList(output));
        System.out.println("Comparisons count: " + quickSort.getComparisons());
        System.out.println("Comparisons one by one count: " + quickSort.getComparisonsOneByOne());
    }


    public Integer[] sort(Integer[] inputArray, int start, int end) {

        if (comparisons < 0) {
            comparisons = 0;
        }

        if (comparisonsOneByOne < 0) {
            comparisonsOneByOne = 0;
        }

        if (inputArray.length < 2) {
            return inputArray;
        }

        int currentPivotIndex = partitionUsingMedian(inputArray, start, end);
        if (start < currentPivotIndex) {
            sort(inputArray, start, currentPivotIndex - 1);
        }
        if (currentPivotIndex < end) {
            sort(inputArray, currentPivotIndex + 1, end);
        }
        return inputArray;
    }

    private int partitionUsingMedian(Integer[] inputArray, int start, int end) {
        if (end - start < 1) {
            return start;
        }

        int localStart = inputArray[start];
        int localEnd = inputArray[end];
        int localMiddle = inputArray[(start + end) / 2];

        int localMedian = getMedianOfThree(localStart, localEnd, localMiddle);

        if (localMedian == localStart) {
            return partitionUsingFirst(inputArray, start, end);
        } else if (localMedian == localEnd) {
            return partitionUsingLastNew(inputArray, start, end);
        } else {
            return partitionUsingMiddle(inputArray, start, end);
        }
    }

    private int partitionUsingMiddle(Integer[] inputArray, int start, int end) {

        int middle = (start + end) / 2;
        int tmp = 0;

        tmp = inputArray[start];
        inputArray[start] = inputArray[middle];
        inputArray[middle] = tmp;

        return partitionUsingFirst(inputArray, start, end);
    }

    private int getMedianOfThree(int first, int second, int third) {
        if (first > second) {
            if (second > third) {
                return second;
            } else if (first > third) {
                return third;
            } else {
                return first;
            }
        } else {
            if (first > third) {
                return first;
            } else if (second > third) {
                return third;
            } else {
                return second;
            }
        }
    }

    /*
     This method is not switching last element with first to bu used as a pivot and as a result is using a bit different partition subroutine
     */
    private int partitionUsingLast(Integer[] inputArray, int start, int end) {
        if (end - start < 1) {
            return start;
        }

        int i = start;
        int tmp = 0;
        int localPivot = inputArray[end];
        for (int j = start; j < end; j++) {
            if (inputArray[j] < localPivot) {
                tmp = inputArray[i];
                inputArray[i] = inputArray[j];
                inputArray[j] = tmp;
                i++;

            }
        }

        tmp = inputArray[end];
        inputArray[end] = inputArray[i];
        inputArray[i] = tmp;

        comparisons += end - start;

        return i;

    }

    private int partitionUsingLastNew(Integer[] inputArray, int start, int end) {
        if (end - start < 1) {
            return start;
        }
        int i = start + 1;
        int tmp = 0;

        tmp = inputArray[start];
        inputArray[start] = inputArray[end];
        inputArray[end] = tmp;

        int localPivot = inputArray[start];
        for (int j = start + 1; j <= end; j++) {
            if (inputArray[j] < localPivot) {
                tmp = inputArray[i];
                inputArray[i] = inputArray[j];
                inputArray[j] = tmp;
                i++;

            }
        }
        tmp = inputArray[start];
        inputArray[start] = inputArray[i - 1];
        inputArray[i - 1] = tmp;

        comparisons += end - start;

        return i - 1;

    }



    private int partitionUsingFirst(Integer[] inputArray, int start, int end) {
        if (end - start < 1) {
            return start;
        }

        int i = start + 1;
        int tmp = 0;
        int localPivot = inputArray[start];
        for (int j = start + 1; j <= end; j++) {
            if (inputArray[j] < localPivot) {
                tmp = inputArray[i];
                inputArray[i] = inputArray[j];
                inputArray[j] = tmp;
                i++;

            }
        }
        tmp = inputArray[start];
        inputArray[start] = inputArray[i - 1];
        inputArray[i - 1] = tmp;

        comparisons += end - start;

        return i - 1;
    }


}
