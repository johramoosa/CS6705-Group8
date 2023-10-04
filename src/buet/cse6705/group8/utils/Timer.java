package buet.cse6705.group8.utils;

/**
 * @author sharafat
 */
public interface Timer {
    void start();
    void stop();
    boolean isTimeUp();
    long getElapsedTime();
}
