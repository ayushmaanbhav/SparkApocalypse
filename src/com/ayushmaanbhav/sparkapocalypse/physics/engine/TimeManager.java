package com.ayushmaanbhav.sparkapocalypse.physics.engine;

import lombok.Getter;

/**
 * Get the game time
 * @author ayush
 */
public class TimeManager {

    private final double timeFactor;

    @Getter private long currentTimeMillis;
    @Getter private long previousTimeMillis;

    private long totalTimeIntervalMillis;
    private long timeUpdateCount;

    public TimeManager(double timeFactor) {
        this.timeFactor = timeFactor;
        this.currentTimeMillis = this.previousTimeMillis = getFactoredTimeMillis();
        this.totalTimeIntervalMillis = this.timeUpdateCount = 0;
    }

    public long updateTime() {
        previousTimeMillis = currentTimeMillis;
        currentTimeMillis = getFactoredTimeMillis();

        totalTimeIntervalMillis += getElaspedTime();
        timeUpdateCount++;

        return currentTimeMillis;
    }

    public long getElaspedTime() {
        return currentTimeMillis - previousTimeMillis;
    }

    private long getFactoredTimeMillis() {
        return (long) (System.currentTimeMillis() * timeFactor);
    }

    public long getAverageTimeInterval() {
        if (timeUpdateCount == 0) return 0;
        return (long) (totalTimeIntervalMillis / (double) timeUpdateCount);
    }

    public long predictNextTimeMillis() {
        return currentTimeMillis + getAverageTimeInterval();
    }

}
