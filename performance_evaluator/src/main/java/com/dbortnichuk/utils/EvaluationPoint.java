package com.dbortnichuk.utils;

import com.dbortnichuk.utils.Constants;

/**
 * User: dbortnichuk
 * Date: 4/23/14
 */
public class EvaluationPoint {

    private String name;
    private int index;
    private long pointTime;
    private long pointMemory;

    public EvaluationPoint(int index, String name) {
        this.name = Constants.STRING_POINT + index + Constants.STRING_SPACE + name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getPointTime() {
        return pointTime;
    }

    public void setPointTime(long pointTime) {
        this.pointTime = pointTime;
    }

    public long getPointMemory() {
        return pointMemory;
    }

    public void setPointMemory(long pointMemory) {
        this.pointMemory = pointMemory;
    }
}
