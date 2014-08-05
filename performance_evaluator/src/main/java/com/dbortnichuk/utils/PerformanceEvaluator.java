package com.dbortnichuk.utils;


import com.dbortnichuk.exception.PerformanceEvaluatorException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dbortnichuk
 * Date: 4/23/14
 */
public class PerformanceEvaluator {

    private PerformanceEvaluator core;

    private String name;

    private List<EvaluationPoint> points;
    private Runtime runtime;

    private String timePrecision = Constants.STRING_MICRO;
    private String memoryPrecision = Constants.STRING_NOMINAL;
    private boolean displayTime = true;
    private boolean displayMemory = false;

    private PerformanceEvaluator(String name) {
        this.name = name;
    }

    public static PerformanceEvaluator newInstance(String instanceName) {
        PerformanceEvaluator instance = new PerformanceEvaluator(instanceName);
        return instance;
    }

    public static PerformanceEvaluator newInstance() {
        return newInstance(Constants.STRING_EMPTY);
    }

    private void init() {
        points = new ArrayList<EvaluationPoint>();
        runtime = Runtime.getRuntime();
    }

    private void finalise() {
        points = null;
        runtime = null;
    }

    private void print(EvaluationPoint point) {
        if (point.getIndex() == 0) {
            long currentPointMemory = point.getPointMemory();

            System.out.println(point.getName());
            System.out.println(Constants.MESSAGE_FREE_MEMORY + currentPointMemory + memoryPrecision + Constants.STRING_BYTES);
            System.out.println(Constants.STRING_EMPTY);
        } else {
            long currentPointTime = point.getPointTime();
            long previousPointTime = points.get(point.getIndex() - 1).getPointTime();
            long startPointTime = points.get(0).getPointTime();

            long currentPointMemory = point.getPointMemory();
            long previousPointMemory = points.get(point.getIndex() - 1).getPointMemory();
            long startPointMemory = points.get(0).getPointMemory();

            System.out.println(point.getName());

            if (displayTime) {
                System.out.println(Constants.MESSAGE_INTERMEDIATE_OPERATION_TIME + (currentPointTime - previousPointTime) + timePrecision + Constants.STRING_SECONDS);
                System.out.println(Constants.MESSAGE_TOTAL_OPERATION_TIME + (currentPointTime - startPointTime) + timePrecision + Constants.STRING_SECONDS);
            }

            if (displayMemory) {
                System.out.println(Constants.MESSAGE_INTERMEDIATE_OPERATION_MEMORY + (previousPointMemory - currentPointMemory) + memoryPrecision + Constants.STRING_BYTES);
                System.out.println(Constants.MESSAGE_TOTAL_OPERATION_MEMORY + (startPointMemory - currentPointMemory) + memoryPrecision + Constants.STRING_BYTES);
                System.out.println(Constants.MESSAGE_FREE_MEMORY + currentPointMemory + memoryPrecision + Constants.STRING_BYTES);
            }

            System.out.println(Constants.STRING_EMPTY);
        }
    }

    private EvaluationPoint establishPoint(String name) throws PerformanceEvaluatorException {
        EvaluationPoint point = null;
        if (points != null && runtime != null) {
            point = new EvaluationPoint(points.size(), name);

            long currentTime = 0;
            if (timePrecision.equals(Constants.STRING_NOMINAL)) {
                currentTime = System.nanoTime() / (Constants.KILO * Constants.KILO * Constants.KILO);
            } else if (timePrecision.equals(Constants.STRING_MILI)) {
                currentTime = System.nanoTime() / (Constants.KILO * Constants.KILO);
            } else if (timePrecision.equals(Constants.STRING_MICRO)) {
                currentTime = System.nanoTime() / Constants.KILO;
            } else if (timePrecision.equals(Constants.STRING_NANO)) {
                currentTime = System.nanoTime();
            }
            point.setPointTime(currentTime);

            long currentMemory = 0;
            if (memoryPrecision.equals(Constants.STRING_NOMINAL)) {
                currentMemory = runtime.freeMemory();
            } else if (memoryPrecision.equals(Constants.STRING_KILO)) {
                currentMemory = runtime.freeMemory() / Constants.KILO;
            } else if (memoryPrecision.equals(Constants.STRING_MEGA)) {
                currentMemory = runtime.freeMemory() / (Constants.KILO * Constants.KILO);
            } else if (memoryPrecision.equals(Constants.STRING_GIGA)) {
                currentMemory = runtime.freeMemory() / (Constants.KILO * Constants.KILO * Constants.KILO);
            }
            point.setPointMemory(currentMemory);

            points.add(point);
        } else {
            throw new PerformanceEvaluatorException(Constants.STRING_PERFORMANCE_EVALUATOR + Constants.MESSAGE_NOT_INITIALIZED);
        }
        return point;
    }

    public EvaluationPoint start() throws PerformanceEvaluatorException {
        init();
        EvaluationPoint startPoint = establishPoint(Constants.STRING_START);
        print(startPoint);
        return startPoint;
    }

    public EvaluationPoint point(String name) throws PerformanceEvaluatorException {
        EvaluationPoint point = null;
        if (points == null || points.size() == 0) {
            start();
        } else {
            point = establishPoint(name);
            print(point);
        }
        return point;
    }

    public void point() throws PerformanceEvaluatorException {
        point(Constants.STRING_EMPTY);
    }

    public EvaluationPoint stop() throws PerformanceEvaluatorException {
        EvaluationPoint stopPoint = null;
        if (points == null || points.size() < 1) {
            throw new PerformanceEvaluatorException(Constants.STRING_PERFORMANCE_EVALUATOR + Constants.MESSAGE_NOT_STARTED);
        } else {
            stopPoint = establishPoint(Constants.STRING_STOP);
            print(stopPoint);
            finalise();
        }
        return stopPoint;
    }

    public void configure(boolean printTime, String timePrecisionCode, boolean printMemory, String memoryPrecisionCode) throws PerformanceEvaluatorException {
        timePrecision = validateTimePrecision(timePrecisionCode);
        memoryPrecision = validateMemoryPrecision(memoryPrecisionCode);
        displayTime = printTime;
        displayMemory = printMemory;
    }

    private String validateTimePrecision(String timePrecisionCode) throws PerformanceEvaluatorException {
        if (timePrecisionCode != null && !timePrecisionCode.isEmpty()) {
            if (timePrecisionCode.equals(Constants.STRING_NOMINAL) || timePrecisionCode.equals(Constants.STRING_MILI) || timePrecisionCode.equals(Constants.STRING_MICRO)
                    || timePrecisionCode.equals(Constants.STRING_NANO)) {
                return timePrecisionCode;
            } else {
                throw new PerformanceEvaluatorException(Constants.STRING_PERFORMANCE_EVALUATOR
                        + Constants.STRING_TIME + Constants.MESSAGE_PRECISION_NOT_SUPPORTED + timePrecisionCode);
            }
        } else {
            throw new PerformanceEvaluatorException(Constants.STRING_PERFORMANCE_EVALUATOR
                    + Constants.STRING_TIME + Constants.MESSAGE_PRECISION_NOT_PROVIDED);
        }
    }

    private String validateMemoryPrecision(String memoryPrecisionCode) throws PerformanceEvaluatorException {
        if (memoryPrecisionCode != null && !memoryPrecisionCode.isEmpty()) {
            if (memoryPrecisionCode.equals(Constants.STRING_NOMINAL) || memoryPrecisionCode.equals(Constants.STRING_KILO)
                    || memoryPrecisionCode.equals(Constants.STRING_MEGA) || memoryPrecisionCode.equals(Constants.STRING_GIGA)) {
                return memoryPrecisionCode;
            } else {
                throw new PerformanceEvaluatorException(Constants.STRING_PERFORMANCE_EVALUATOR
                        + Constants.STRING_MEMORY + Constants.MESSAGE_PRECISION_NOT_SUPPORTED + memoryPrecisionCode);
            }
        } else {
            throw new PerformanceEvaluatorException(Constants.STRING_PERFORMANCE_EVALUATOR
                    + Constants.STRING_MEMORY + Constants.MESSAGE_PRECISION_NOT_PROVIDED);
        }
    }

}
