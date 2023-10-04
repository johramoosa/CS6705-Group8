package buet.cse6705.group8.algos;

import buet.cse6705.group8.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author sharafat
 */
public class SignedPermutationGenerator {
    private static final Logger log = LoggerFactory.getLogger(SignedPermutationGenerator.class);

    private static final Random random = new Random();

    public static int[][] generate(int[] unsignedPermutation, int maxNoOfPermutationsToBeGenerated) {
        int noOfPermutationsToBeGenerated = getNoOfPermutationsToBeGenerated(
                unsignedPermutation.length, maxNoOfPermutationsToBeGenerated);

        int[][] signedPermutations = new int[noOfPermutationsToBeGenerated][unsignedPermutation.length];

        for (int i = 0; i < signedPermutations.length; i++) {
            int[] randomSignedPermutation;

            do {
                randomSignedPermutation = generateRandomSignedPermutation(unsignedPermutation);
            } while (Utils.exists(randomSignedPermutation, signedPermutations, i));

            signedPermutations[i] = randomSignedPermutation;
        }

        return signedPermutations;
    }

    private static int getNoOfPermutationsToBeGenerated(int noOfItemsInPermutation, int maxNoOfPermutationsToBeGenerated) {
        int possibleNoOfSignedPermutations = (int) Math.pow(2, noOfItemsInPermutation);
        log.trace("possibleNoOfSignedPermutations: {}, maxNoOfPermutationsToBeGenerated: {}",
                possibleNoOfSignedPermutations, maxNoOfPermutationsToBeGenerated);

        return possibleNoOfSignedPermutations < maxNoOfPermutationsToBeGenerated ?
                possibleNoOfSignedPermutations : maxNoOfPermutationsToBeGenerated;
    }

    private static int[] generateRandomSignedPermutation(int[] unsignedPermutation) {
        int[] randomSignedPermutation = new int[unsignedPermutation.length];

        for (int i = 0; i < randomSignedPermutation.length; i++) {
            int number = unsignedPermutation[i];

            if (random.nextBoolean()) {
                //set sign to negative
                number *= -1;
            }

            randomSignedPermutation[i] = number;
        }

        return randomSignedPermutation;
    }
}
