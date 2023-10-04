package buet.cse6705.group8;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author sharafat
 */
public class Individual implements Serializable {
    private int[] permutation;
    private int reversalDistance;

    public Individual(int[] permutation) {
        this.permutation = permutation;
    }

    public int[] getPermutation() {
        return permutation;
    }

    public int getReversalDistance() {
        return reversalDistance;
    }

    public void setReversalDistance(int reversalDistance) {
        this.reversalDistance = reversalDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Individual that = (Individual) o;

        return reversalDistance == that.reversalDistance && Arrays.equals(permutation, that.permutation);
    }

    @Override
    public int hashCode() {
        int result = permutation != null ? Arrays.hashCode(permutation) : 0;
        result = 31 * result + reversalDistance;
        return result;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "permutation=" + Arrays.toString(permutation) +
                ", reversalDistance=" + reversalDistance +
                '}';
    }

    public Individual clone() {
        int[] permutation = new int[this.permutation.length];
        System.arraycopy(this.permutation, 0, permutation, 0, permutation.length);

        Individual individual = new Individual(permutation);
        individual.setReversalDistance(reversalDistance);

        return individual;
    }
}
