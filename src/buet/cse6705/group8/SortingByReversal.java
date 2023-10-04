package buet.cse6705.group8;

import buet.cse6705.group8.algos.*;
import buet.cse6705.group8.utils.StrategyBasedTimer;
import buet.cse6705.group8.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static buet.cse6705.group8.utils.PopulationUtils.*;

/**
 * @author sharafat
 */
public class SortingByReversal {
    public static int MAX_NO_OF_INDIVIDUALS_IN_SEED = 5;
    public static int MAX_NO_OF_INDIVIDUALS_IN_DIVERSE_POPULATION = 3;
    public static int VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_POPULATION_DIVERSIFIER = 2;
    public static int VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_SELECTING_FITTEST_INDIVIDUALS = 2;
    public static int VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_SELECTING_MOST_DIVERSE_INDIVIDUALS = 2;
    public static int MAX_RUNNING_TIME_OF_ALGORITHM_IN_SECONDS = 1;
    public static int MAX_NO_OF_FITTEST_INDIVIDUALS_TO_SELECT = 1;
    public static int MAX_NO_OF_MOST_DIVERSE_INDIVIDUALS_TO_SELECT = 2;
    public static int TERMINATION_STRATEGY = StrategyBasedTimer.TERMINATION_STRATEGY_EXPECTED_FITNESS_FOUND;
    public static int TERMINATION_PARAMETER = 3;
    public static float CROSSOVER_PROBABILITY = 0.8f;
    public static float MUTATION_PROBABILITY = 0.2f;
    public static boolean USE_DFS_ROT = true;
    public static boolean USE_SUCCESSIVE_2_REVERSAL = true;

    public static boolean stop = false;
    private static final Logger log = LoggerFactory.getLogger(SortingByReversal.class);

    public static int run(int[] givenPermutation) {
        log.info("Using successive2reversal preprocessing: {}", USE_SUCCESSIVE_2_REVERSAL);
        log.info("Using DFS_ROT: {}", USE_DFS_ROT);

        INV_DIST.resetIdentityPermutation();

        int reversalDistanceFromSuccessive2Reversal = 0;

        if (USE_SUCCESSIVE_2_REVERSAL) {
            log.debug("Given permutation: {}", givenPermutation);

            Preprocess preprocess = new Preprocess(givenPermutation, givenPermutation.length);
            preprocess.succ2rev();

            for (int i = 0; i < preprocess.permutation.size(); i++) {
                givenPermutation[i] = preprocess.permutation.get(i);
            }
            reversalDistanceFromSuccessive2Reversal = preprocess.num;
            log.debug("Permutation after succ2rev(): {}, no. of. rev: {}",
                    givenPermutation, reversalDistanceFromSuccessive2Reversal);
        }

        int[][] subsetOfSignedPermutations = SignedPermutationGenerator.generate(
                givenPermutation, MAX_NO_OF_INDIVIDUALS_IN_SEED);
        log.info("No. of generated signed permutations: {}, generated signed permutations: {}",
                subsetOfSignedPermutations.length, subsetOfSignedPermutations);

        int[][] seed = USE_DFS_ROT ?
                DFS_ROT.changeTrivialMembersToNonTrivial(subsetOfSignedPermutations)
                //TODO: what if after changing, two permutations become the same??
                : subsetOfSignedPermutations;

        log.info("Seed size: {}, seed: {}", seed.length, seed);

        int[][] diversePopulation = PopulationDiversifier.diversify(
                seed, MAX_NO_OF_INDIVIDUALS_IN_DIVERSE_POPULATION);
        log.info("DiversePopulation size: {}, diversePopulation: {}", diversePopulation.length, diversePopulation);

        int[][] populationAsArrays = Utils.union(seed, diversePopulation);
        List<Individual> population = populationArraysToObjectList(populationAsArrays);

        Individual best = executeAlgorithm(population);
        log.info("Best: {}", best);

        return best.getReversalDistance() + reversalDistanceFromSuccessive2Reversal;
    }

    private static Individual executeAlgorithm(List<Individual> population) {
        INV_DIST.populateReversalDistance(population);

        Individual best = findBest(population.toArray(new Individual[population.size()]));
        log.debug("Initial best: {}", best);
        int generationNo = 0;

        //StrategyBasedTimer timer = new TimeBasedTimer(MAX_RUNNING_TIME_OF_ALGORITHM_IN_SECONDS * 1000);
        StrategyBasedTimer timer = new StrategyBasedTimer(TERMINATION_STRATEGY, TERMINATION_PARAMETER);
        timer.start();

        while (!timer.isTimeUp()) {
            log.debug("\n\n\n\n\nGeneration {}\n\n\n\n\n", generationNo++);

            timer.setFitnessAndCurrentNoOfGenerations(best.getReversalDistance(), generationNo);

            population = keepFittestAndMostDiverseIndividuals(population,
                    MAX_NO_OF_FITTEST_INDIVIDUALS_TO_SELECT, MAX_NO_OF_MOST_DIVERSE_INDIVIDUALS_TO_SELECT);

            List<Individual> nextGeneration = new ArrayList<Individual>(population.size());

            for (int i = 0; i < population.size(); i++) {
                for (int j = 0; j < population.size(); j++) {
                    log.trace("i: {}, j: {}", i, j);

                    if (stop) {
                        log.warn("User stopped execution!");
                        log.info("Generations generated: {}", generationNo);
                        return best;
                    }

                    if (i != j) {
                        List<Individual> children = crossover(population.get(i), population.get(j));
                        mutate(children.get(0));
                        mutate(children.get(1));
                        log.trace("After mutation: {} {}", children.get(0), children.get(1));

                        INV_DIST.populateReversalDistance(children);
                        best = findBest(best, children.get(0), children.get(1)).clone();
                        log.debug("new best: {}", best);

                        nextGeneration.addAll(children);
                    }
                }
            }

            population.addAll(nextGeneration);
            if (USE_DFS_ROT) {
                population = DFS_ROT.changeTrivialMembersToNonTrivial(population);
            }
        }

        log.info("Generations generated: {}", generationNo);

        timer.stop();
        log.info("Runtime of algorithm: {}", timer.getFormattedElapsedTime());

        return best;
    }
}
