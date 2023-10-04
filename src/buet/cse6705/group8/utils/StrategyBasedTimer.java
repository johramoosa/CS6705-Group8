package buet.cse6705.group8.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sharafat
 */
public class StrategyBasedTimer implements Timer {
    public static final int TERMINATION_STRATEGY_EXPECTED_FITNESS_FOUND = 0;
    public static final int TERMINATION_STRATEGY_FITNESS_HASNT_CHANGED_FOR_GENERATIONS = 1;
    public static final int TERMINATION_STRATEGY_NO_OF_GENERATIONS = 2;
    public static final int TERMINATION_STRATEGY_RUNNING_TIME = 3;

    private static final Logger log = LoggerFactory.getLogger(StrategyBasedTimer.class);

    private int terminationStrategy, terminationParameter, currentNoOfGenerations,
            currentReversalDistance = Integer.MAX_VALUE, noOfGenerationsFitnessDidntChange;
    private TimeBasedTimer timer;

    public StrategyBasedTimer(int terminationStrategy, int terminationParameter) {
        this.terminationStrategy = terminationStrategy;
        this.terminationParameter = terminationParameter;

        timer = new TimeBasedTimer(terminationParameter * 1000);
    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void stop() {
        timer.stop();
    }

    @Override
    public boolean isTimeUp() {
        switch (terminationStrategy) {
            case TERMINATION_STRATEGY_EXPECTED_FITNESS_FOUND:
                log.trace("Termination Strategy: {}, currentReversalDistance: {}, terminationParameter: {}",
                        new Object[]{"EXPECTED_FITNESS_FOUND", currentReversalDistance, terminationParameter});
                return currentReversalDistance <= terminationParameter;
            case TERMINATION_STRATEGY_FITNESS_HASNT_CHANGED_FOR_GENERATIONS:
                log.trace("Termination Strategy: {}, noOfGenerationsFitnessDidntChange: {}, currentReversalDistance: {}," +
                        " terminationParameter: {}",
                        new Object[]{"FITNESS_HASNT_CHANGED_FOR_GENERATIONS", noOfGenerationsFitnessDidntChange,
                                currentReversalDistance, terminationParameter});
                return noOfGenerationsFitnessDidntChange > terminationParameter;
            case TERMINATION_STRATEGY_NO_OF_GENERATIONS:
                log.trace("Termination Strategy: {}, currentNoOfGenerations: {}, terminationParameter: {}",
                        new Object[]{"NO_OF_GENERATIONS", currentNoOfGenerations, terminationParameter});
                return currentNoOfGenerations > terminationParameter;
            case TERMINATION_STRATEGY_RUNNING_TIME:
                log.trace("Termination Strategy: {}, elapsedTime: {}, terminationParameter: {}",
                        new Object[]{"RUNNING_TIME", timer.getElapsedTime(), terminationParameter});
                return timer.isTimeUp();
        }
        return false;
    }

    @Override
    public long getElapsedTime() {
        return timer.getElapsedTime();
    }

    public void setFitnessAndCurrentNoOfGenerations(int reversalDistance, int currentNoOfGenerations) {
        if (reversalDistance == currentReversalDistance && currentNoOfGenerations > 0) {
            noOfGenerationsFitnessDidntChange++;
        }
        currentReversalDistance = reversalDistance;
        this.currentNoOfGenerations = currentNoOfGenerations;
    }

    public String getFormattedElapsedTime() {
        return timer.getFormattedElapsedTime();
    }
}
