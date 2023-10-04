package buet.cse6705.group8.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author sharafat
 */
public class TimeBasedTimer implements Timer {
    private long maxTime;
    private long startTime;
    private long stopTime;

    public TimeBasedTimer(long maxTimeInMillis) {
        maxTime = maxTimeInMillis;
    }

    @Override
    public void start() {
        startTime = currentTime();
    }

    @Override
    public void stop() {
        stopTime = currentTime();
    }

    @Override
    public boolean isTimeUp() {
        return currentTime() - startTime >= maxTime;
    }

    @Override
    public long getElapsedTime() {
        return stopTime - startTime;
    }

    public String getFormattedElapsedTime() {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(getElapsedTime()),
                TimeUnit.MILLISECONDS.toMinutes(getElapsedTime()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getElapsedTime())),
                TimeUnit.MILLISECONDS.toSeconds(getElapsedTime()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getElapsedTime()))
        );
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}
