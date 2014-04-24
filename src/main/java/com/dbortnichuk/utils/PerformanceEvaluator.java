package com.dbortnichuk.utils;


import com.dbortnichuk.exception.PerformanceEvaluatorException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dbortnichuk
 * Date: 4/23/14
 */
public class PerformanceEvaluator {

    private String precision = Constants.LITERAL_MICRO;
    private List<EvaluationPoint> points;
    private Runtime runtime;

    private void init() {
        points = new ArrayList<EvaluationPoint>();
        runtime = Runtime.getRuntime();
    }


    private void print(EvaluationPoint point) {
        if (point.getIndex() == 0) {
            System.out.println(point.getName());
            System.out.println(Constants.MESSAGE_FREE_MEMORY + runtime.freeMemory() + Constants.LITERAL_BYTES);
            System.out.println(Constants.LITERAL_EMPTY_STRING);
        } else {
            long currentPointTime = point.getPointTime();
            long previousPointTime = points.get(point.getIndex() - 1).getPointTime();
            long startPointTime = points.get(0).getPointTime();

            long currentPointMemory = point.getPointMemory();
            long previousPointMemory = points.get(point.getIndex() - 1).getPointMemory();
            long startPointMemory = points.get(0).getPointMemory();

            System.out.println(point.getName());

            System.out.println(Constants.MESSAGE_INTERMEDIATE_OPERATION_TIME + (currentPointTime - previousPointTime) + precision + Constants.LITERAL_SECONDS);
            System.out.println(Constants.MESSAGE_TOTAL_OPERATION_TIME + (currentPointTime - startPointTime) + precision + Constants.LITERAL_SECONDS);

            System.out.println(Constants.MESSAGE_INTERMEDIATE_OPERATION_MEMORY + (previousPointMemory - currentPointMemory) + Constants.LITERAL_BYTES);
            System.out.println(Constants.MESSAGE_TOTAL_OPERATION_MEMORY + (startPointMemory - currentPointMemory) + Constants.LITERAL_BYTES);
            System.out.println(Constants.MESSAGE_FREE_MEMORY + runtime.freeMemory() + Constants.LITERAL_BYTES);


            System.out.println(Constants.LITERAL_EMPTY_STRING);
        }
    }

    private EvaluationPoint establishPoint(String name) throws PerformanceEvaluatorException {
        EvaluationPoint point = null;
        if (points != null) {
            point = new EvaluationPoint(points.size(), name);
            long currentTime = 0;
            if (precision.equals(Constants.LITERAL_MILI)) {
                currentTime = System.nanoTime() / (Constants.KILO * Constants.KILO);
            } else if (precision.equals(Constants.LITERAL_MICRO)) {
                currentTime = System.nanoTime() / Constants.KILO;
            } else if (precision.equals(Constants.LITERAL_NANO)) {
                currentTime = System.nanoTime();
            } else {
                throw new PerformanceEvaluatorException(Constants.LITERAL_PERFORMANCE_EVALUATOR + Constants.MESSAGE_PRECISION_NOT_SUPPORTED + precision);
            }
            point.setPointTime(currentTime);
            point.setPointMemory(runtime.freeMemory());
            points.add(point);
        } else {
            throw new PerformanceEvaluatorException(Constants.LITERAL_PERFORMANCE_EVALUATOR + Constants.MESSAGE_NOT_INITIALIZED);
        }
        return point;
    }

    public void start() throws PerformanceEvaluatorException {
        init();
        EvaluationPoint startPoint = establishPoint(Constants.LITERAL_START);
        print(startPoint);
    }

    public void point(String name) throws PerformanceEvaluatorException {
        if (points == null || points.size() == 0) {
            start();
        } else {
            EvaluationPoint startPoint = establishPoint(name);
            print(startPoint);
        }
    }

    public void point() throws PerformanceEvaluatorException {
        point(Constants.LITERAL_EMPTY_STRING);
    }

    public void stop() throws PerformanceEvaluatorException {
        if (points == null || points.size() < 1) {
            throw new PerformanceEvaluatorException(Constants.LITERAL_PERFORMANCE_EVALUATOR + Constants.MESSAGE_NOT_STARTED);
        } else {
            EvaluationPoint stopPoint = establishPoint(Constants.LITERAL_STOP);
            print(stopPoint);
        }
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }
}
