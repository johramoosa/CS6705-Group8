package buet.cse6705.group8.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sharafat
 */
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static boolean exists(int[] needle, int[][] haystack) {
        return exists(needle, haystack, haystack.length);
    }

    public static boolean exists(int[] needle, int[][] haystack, int maxIndexInHaystackToBeChecked) {
        int actualMaxIndexInHaystackToBeChecked = haystack.length < maxIndexInHaystackToBeChecked ?
                haystack.length : maxIndexInHaystackToBeChecked;

        for (int i = 0; i < actualMaxIndexInHaystackToBeChecked; i++) {
            boolean exists = true;

            for (int j = 0; j < needle.length; j++) {
                if (haystack[i][j] != needle[j]) {
                    exists = false;
                    break;
                }
            }

            if (exists) {
                return true;
            }
        }

        return false;
    }

    public static int max(int... values) {
        int max = values[0];

        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    public static int min(int... values) {
        int min = values[0];

        for (int value : values) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }

    public static float avg(int... values) {
        int total = 0;

        for (int value : values) {
            total += value;
        }

        return (float) total / (float) values.length;
    }

    public static int firstIndexOf(int value, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }

        log.warn("value {} not found in array {}", value, array);
        return -1;
    }

    public static int[][] union(int[][]... arrays) {
        int returnArrayLength = 0;
        for (int[][] array : arrays) {
            returnArrayLength += array.length;
        }

        int[][] returnArray = new int[returnArrayLength][];
        int returnArrayIndex = 0;

        for (int[][] array : arrays) {
            for (int[] innerArray : array) {
                returnArray[returnArrayIndex++] = innerArray;
            }
        }

        return returnArray;
    }
}
