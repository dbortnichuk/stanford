package com.dbortnichuk.exception;

/**
 * User: dbortnichuk
 * Date: 4/24/14
 */
public class PerformanceEvaluatorException extends Exception {

    public PerformanceEvaluatorException() {

    }

    public PerformanceEvaluatorException(String message) {
        super(message);
    }

    public PerformanceEvaluatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
