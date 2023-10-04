package buet.cse6705.group8.algos;

import buet.cse6705.group8.Individual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sharafat
 */
public class DFS_ROT {
    private static final Logger log = LoggerFactory.getLogger(DFS_ROT.class);

    public static int[][] changeTrivialMembersToNonTrivial(int[][] singedPermutations) {
        List<Individual> individuals = new ArrayList<Individual>(singedPermutations.length);
        for (int[] singedPermutation : singedPermutations) {
            individuals.add(new Individual(singedPermutation));
        }

        List<Individual> resultingIndividuals = changeTrivialMembersToNonTrivial(individuals);
        int[][] resultingSignedPermutations = new int[singedPermutations.length][];
        for (int i = 0; i < resultingIndividuals.size(); i++) {
            resultingSignedPermutations[i] = resultingIndividuals.get(i).getPermutation();
        }

        return resultingSignedPermutations;
    }

    public static List<Individual> changeTrivialMembersToNonTrivial(List<Individual> individuals) {
        for (Individual individual : individuals) {
            applyDFS_ROT(individual.getPermutation());
        }

        return individuals;
    }

    private static void applyDFS_ROT(int[] alpha) {
        int[] alphaForLogging = alpha.clone();
        boolean alphaChanged = false;   //for logging

        int e = getReversalDistance(alpha);

        for (int i = 0; i < alpha.length; i++) {
            log.trace("DFS_ROT-i: {}", i);

            int[] alphaPrime = changeSign(alpha, i);
            int ePrime = getReversalDistance(alphaPrime);
            if (ePrime < e) {
                alpha = alphaPrime;
                e = ePrime;
                alphaChanged = true;    //for logging
            }
        }

        log.trace("DFS_ROT processing of {} resulted in {}. alphaChanged={}",
                new Object[]{alphaForLogging, alpha, alphaChanged});
    }

    private static int getReversalDistance(int[] permutation) {
        Individual individual = new Individual(permutation);
        INV_DIST.populateReversalDistance(Arrays.asList(individual));

        return individual.getReversalDistance();
    }

    private static int[] changeSign(int[] permutation, int position) {
        int[] alteredPermutation = permutation.clone();
        alteredPermutation[position] = alteredPermutation[position] * -1;
        return alteredPermutation;
    }
}
