package com.dbortnichuk.stanford.algorithms.pq1;

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

    public static void main(String[] args) {
        InversionsCounter inversionsCounter = new InversionsCounter("/IntegerArray.txt");
        Integer[] inputData = inversionsCounter.getData().toArray(new Integer[]{});
    }

}
