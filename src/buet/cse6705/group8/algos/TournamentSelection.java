package buet.cse6705.group8.algos;

import buet.cse6705.group8.Individual;
import buet.cse6705.group8.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentSelection {
    private static final Logger log = LoggerFactory.getLogger(TournamentSelection.class);

    public static List<Individual> tournamentSelection(List<Individual> population, int noOfIndividualsToSelect, int t,
                                                       boolean selectFittest) {
        log.debug("Given population for tournament selection: {}", population);

        int[][] populationArray = new int[population.size()][];
        for (int i = 0; i < population.size(); i++) {
            populationArray[i] = population.get(i).getPermutation();
        }

        int[][] selectedPopulationArray = tournamentSelection(populationArray, noOfIndividualsToSelect, t, selectFittest);

        List<Individual> selectedPopulation = new ArrayList<Individual>(t);
        for (int[] individualInSelectedPopulationArray : selectedPopulationArray) {
            for (Individual individualInPopulation : population) {
                if (individualInPopulation.getPermutation() == individualInSelectedPopulationArray) {
                    boolean permutationExistsInSelectedPopulationList = false;

                    for (Individual individualInSelectedPopulation : selectedPopulation) {
                        if (individualInSelectedPopulation.getPermutation() == individualInSelectedPopulationArray) {
                            permutationExistsInSelectedPopulationList = true;
                            break;
                        }
                    }

                    if (!permutationExistsInSelectedPopulationList) {
                        selectedPopulation.add(individualInPopulation);
                    }
                }
            }
        }

        log.debug("Selected population after tournament selection: {}", selectedPopulation);

        return selectedPopulation;
    }

    public static int[][] tournamentSelection(int[][] population, int noOfIndividualsToSelect, int t,
                                              boolean selectFittest) {
        int[][] selectedPopulation = new int[noOfIndividualsToSelect][population[0].length];

        List<Integer> populationArrayIndices = new ArrayList<Integer>(population.length);
        //put 1, 2, 3 etc. indices into populationArrayIndices
        for (int i = 0; i < population.length; i++) {
            populationArrayIndices.add(i);
        }

        for (int i = 0; i < selectedPopulation.length; i++) {
            Collections.shuffle(populationArrayIndices);

            int[] hammingDistances = new int[t];
            int hammingDistancesIndex = 0;
            for (int j = 0; j < t; j++) {
                hammingDistances[hammingDistancesIndex] = hammingDistance(population, populationArrayIndices.get(j));
            }

            int chosenHammingDistance = selectFittest ? Utils.max(hammingDistances) : Utils.min(hammingDistances);
            int indexOfChosenHammingDistance = Utils.firstIndexOf(chosenHammingDistance, hammingDistances);

            selectedPopulation[i] = population[populationArrayIndices.get(indexOfChosenHammingDistance)];
            populationArrayIndices.remove(indexOfChosenHammingDistance);
        }

        return selectedPopulation;
    }

    private static int hammingDistance(int[][] population, int indexInPopulationWhoseHammingDistanceIsToBeCalculated) {
        int hammingDistance = 0;

        for (int i = 0; i < population.length; i++) {
            if (i != indexInPopulationWhoseHammingDistanceIsToBeCalculated) {
                for (int j = 0; j < population[i].length; j++) {
                    if (population[i][j] != population[indexInPopulationWhoseHammingDistanceIsToBeCalculated][j]) {
                        hammingDistance++;
                    }
                }
            }
        }

        return hammingDistance;
    }
}
