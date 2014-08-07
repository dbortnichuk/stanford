package com.dbortnichuk.stanford.algorithms.pq1;

import com.dbortnichuk.exception.PerformanceEvaluatorException;
import com.dbortnichuk.utils.PerformanceEvaluator;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dbortnichuk
 * Date: 8/4/14
 */
public class InversionsCounter {

    List<Integer> data;

    public InversionsCounter(String filePath) {
        BufferedReader reader = null;
        try {
//            InputStream is = getClass().getResourceAsStream(filePath);
//            System.out.println("Total file size to read (in bytes) : " + is.available());
            URL fileURL = getClass().getResource(filePath);
            String absoluteFilePath = fileURL.getPath();
            reader = new BufferedReader(new FileReader(absoluteFilePath));
            String line = null;
            List<Integer> numbersList = new ArrayList<Integer>();
            while ((line = reader.readLine()) != null) {
                Integer number = Integer.parseInt(line);
                numbersList.add(number);
            }
            data = numbersList;
            System.out.println("Input size: " + data.size());
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

    public List<Integer> getData() {
        return data;
    }

    public static void main(String[] args) throws PerformanceEvaluatorException {
        PerformanceEvaluator performanceEvaluator = PerformanceEvaluator.newInstance();

        InversionsCounter inversionsCounter = new InversionsCounter("/IntegerArray.txt");
        Integer[] inputData = inversionsCounter.getData().toArray(new Integer[]{});

        performanceEvaluator.start();
        long inversionCount = inversionsCounter.getInversionsOnlogn(inputData, 0, inputData.length - 1);
        //long inversionCount = inversionsCounter.getInversionsOn2(inputData);
        performanceEvaluator.stop();

        System.out.println("Inversions count: " + inversionCount);
    }

    public long getInversionsOn2(Integer[] array) {
        long inversionsCount = 0;
        int arrayLength = array.length;
        for (int i = 0; i < arrayLength - 1; i++) {
            for (int j = i + 1; j < arrayLength; j++) {
                if (array[i] > array[j]) {
                    inversionsCount++;
                }
            }
        }
        return inversionsCount;
    }

    public long getInversionsOnlogn(Integer[] array, int start, int end) {
        long inversionsCount = 0;
        if (start < end) {
            int median = (start + end) / 2;
            long leftInversionsCount = getInversionsOnlogn(array, start, median);
            long rightInversionsCount = getInversionsOnlogn(array, median + 1, end);
            long mergeInversionsCount = getMergeInversions(array, start, median, end);
            inversionsCount = leftInversionsCount + rightInversionsCount + mergeInversionsCount;
        }
        return inversionsCount;
    }

    private long getMergeInversions(Integer[] array, int start, int median, int end) {
        int mergeInversionsCount = 0;

        int leftLength = median - start + 1;
        int rightLength = end - median;
        Integer[] leftHalf = new Integer[leftLength + 1];
        Integer[] rightHalf = new Integer[rightLength + 1];
        for (int i = 0; i < leftLength; i++) leftHalf[i] = array[start + i];
        for (int i = 0; i < rightLength; i++) rightHalf[i] = array[median + 1 + i];

        leftHalf[leftLength] = Integer.MAX_VALUE;
        rightHalf[rightLength] = Integer.MAX_VALUE;
        int i = 0, j = 0;
        for (int k = start; k <= end; k++) {
            if (leftHalf[i] <= rightHalf[j]) {
                array[k] = leftHalf[i];
                i++;
            } else {
                array[k] = rightHalf[j];
                j++;
                mergeInversionsCount = mergeInversionsCount + (leftLength - i);
            }
        }
        return mergeInversionsCount;
    }


}
