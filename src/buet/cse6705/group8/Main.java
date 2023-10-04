package buet.cse6705.group8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sharafat
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final int USER_STOPPED_EXECUTION = -1;
    static int best;

    public static void main(String[] args) {
        int[] givenPermutation = parseStringForGivenPermutation(args[0]);
        log.trace("Given permutation: {}", givenPermutation);

        best = SortingByReversal.run(givenPermutation);
        System.out.println("Best: " + best);
    }

    private static int[] parseStringForGivenPermutation(String permutationString) {
        String[] numbersAsString = permutationString.split(" ");
        int[] numbers = new int[numbersAsString.length];

        for (int i = 0; i < numbersAsString.length; i++) {
            numbers[i] = Integer.parseInt(numbersAsString[i]);
        }

        return numbers;
    }
}
