package buet.cse6705.group8.utils;

import buet.cse6705.group8.Individual;
import buet.cse6705.group8.SortingByReversal;
import buet.cse6705.group8.algos.TournamentSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author sharafat
 */
public class PopulationUtils {
    private static final Logger log = LoggerFactory.getLogger(PopulationUtils.class);
    private static final Random random = new Random();

    public static List<Individual> populationArraysToObjectList(int[][] populationAsArrays) {
        List<Individual> population = new ArrayList<Individual>(populationAsArrays.length);

        for (int[] permutation : populationAsArrays) {
            population.add(new Individual(permutation));
        }

        return population;
    }

    public static Individual findBest(Individual... population) {
        Individual best = population[0];

        for (Individual individual : population) {
            if (individual.getReversalDistance() < best.getReversalDistance()) {
                best = individual;
            }
        }

        return best;
    }

    public static List<Individual> keepFittestAndMostDiverseIndividuals(List<Individual> population,
                                                                        int noOfFittestIndividualsToKeep,
                                                                        int noOfMostDiverseIndividualsToKeep) {
        assert noOfFittestIndividualsToKeep + noOfMostDiverseIndividualsToKeep <= population.size();

        sortByReversalDistance(population);
        List<Individual> fittestAndMostDiverseIndividuals =
                TournamentSelection.tournamentSelection(population, noOfFittestIndividualsToKeep,
                        SortingByReversal.VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_SELECTING_FITTEST_INDIVIDUALS,
                        true);
        fittestAndMostDiverseIndividuals.addAll(
                TournamentSelection.tournamentSelection(population, noOfMostDiverseIndividualsToKeep,
                        SortingByReversal.VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_SELECTING_MOST_DIVERSE_INDIVIDUALS,
                        false));

        return fittestAndMostDiverseIndividuals;
    }

    private static void sortByReversalDistance(List<Individual> population) {
        Collections.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return o1.getReversalDistance() - o2.getReversalDistance();
            }
        });
    }

    public static List<Individual> crossover(Individual individual1, Individual individual2) {
        float randomValueForCrossoverProbability = random.nextFloat();
        log.trace("randomValueForCrossoverProbability: {}, crossoverProbability: {}",
                randomValueForCrossoverProbability, SortingByReversal.CROSSOVER_PROBABILITY);

        if (randomValueForCrossoverProbability >= SortingByReversal.CROSSOVER_PROBABILITY) {
            log.trace("Before crossover: {} {}", individual1.getPermutation(), individual2.getPermutation());

            individual1 = individual1.clone();
            individual2 = individual2.clone();

            int[] individual1Permutation = individual1.getPermutation();
            int[] individual2Permutation = individual2.getPermutation();

            int crossPoint = random.nextInt(individual1Permutation.length);   //TODO: should we check for boundary values?
            log.trace("crossPoint: {}", crossPoint);

            for (int i = crossPoint; i < individual1Permutation.length; i++) {
                boolean individual1SignNegative = individual1Permutation[i] < 0;
                boolean individual2SignNegative = individual2Permutation[i] < 0;

                individual1Permutation[i] = Math.abs(individual1Permutation[i]) * (individual2SignNegative ? -1 : 1);
                individual2Permutation[i] = Math.abs(individual2Permutation[i]) * (individual1SignNegative ? -1 : 1);
            }

            log.trace("After crossover: {} {}", individual1.getPermutation(), individual2.getPermutation());
        }

        return new ArrayList<Individual>(Arrays.asList(individual1, individual2));
    }

    public static void mutate(Individual individual) {
        float randomValueForMutationProbability = random.nextFloat();
        log.trace("randomValueForMutationProbability: {}, mutationProbability: {}",
                randomValueForMutationProbability, SortingByReversal.MUTATION_PROBABILITY);

        if (randomValueForMutationProbability >= SortingByReversal.MUTATION_PROBABILITY) {
            //let's consider this individual for mutation
            int[] permutation = individual.getPermutation();
            for (int i = 0; i < permutation.length; i++) {
                if (random.nextBoolean()) {
                    //let's mutate... >:)
                    permutation[i] *= -1;
                }
            }
        }
    }
}
