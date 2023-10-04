package buet.cse6705.group8.algos;

import buet.cse6705.group8.SortingByReversal;
import buet.cse6705.group8.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author sharafat
 */
public class PopulationDiversifier {
    private static final Logger log = LoggerFactory.getLogger(PopulationDiversifier.class);

    public static int[][] diversify(int[][] population, int maxNoOfIndividualsInDiversePopulation) {
        int[][] fittestDiversePopulation = TournamentSelection.tournamentSelection(
                population, maxNoOfIndividualsInDiversePopulation / 2,
                SortingByReversal.VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_POPULATION_DIVERSIFIER, true);

        int[][] mostUnfitDiversePopulation = TournamentSelection.tournamentSelection(
                population, maxNoOfIndividualsInDiversePopulation % 2 == 0
                ? maxNoOfIndividualsInDiversePopulation / 2 : maxNoOfIndividualsInDiversePopulation / 2 + 1,
                SortingByReversal.VALUE_OF_T_IN_TOURNAMENT_SELECTION_FOR_POPULATION_DIVERSIFIER, false);

        int[][] diversePopulation = Utils.union(fittestDiversePopulation, mostUnfitDiversePopulation);

        int[][] diversePopulationWithRandomSignChange = changeSignRandomly(diversePopulation);
        log.debug("diversePopulationWithRandomSignChange size: {}, diversePopulationWithRandomSignChange: {}",
                diversePopulationWithRandomSignChange.length, diversePopulationWithRandomSignChange);

        return diversePopulationWithRandomSignChange;
    }

    private static int[][] changeSignRandomly(int[][] originalPopulation) {
        int[][] population = new int[originalPopulation.length][originalPopulation[0].length];

        int populationIndex = 0;
        Random random = new Random();

        do {
            for (int[] permutation : originalPopulation) {
                //randomly decide if we wanna pick this permutation for changing signs
                if (random.nextBoolean()) {
                    int[] newPermutation = permutation.clone();
                    for (int i = 0; i < newPermutation.length; i++) {
                        if (random.nextBoolean()) {
                            //change sign
                            newPermutation[i] *= -1;
                        }
                    }
                    if (!Utils.exists(newPermutation, population)) {
                        population[populationIndex++] = newPermutation;
                        if (populationIndex == population.length) {
                            break;
                        }
                    }
                }
            }
        } while (populationIndex < population.length);

        return population;
    }

}
