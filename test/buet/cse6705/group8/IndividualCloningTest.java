package buet.cse6705.group8;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author sharafat
 */
public class IndividualCloningTest {

    @Test
    public void testCloning() {
        int[] permutation = new int[] {1, -4, 2, -5, 3};
        int reversalDistance = 5;

        Individual source = new Individual(permutation);
        source.setReversalDistance(reversalDistance);
        Individual result = source.clone();

        assertFalse("source and result object reference is the same, expected different", source == result);
        assertFalse("source and result permutation reference is the same, expected different",
                source.getPermutation() == result.getPermutation());
        assertArrayEquals("source and result permutation numbers are different, expected same",
                permutation, result.getPermutation());
        assertEquals(reversalDistance, result.getReversalDistance());
    }

}
