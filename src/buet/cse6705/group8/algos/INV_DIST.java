package buet.cse6705.group8.algos;

import buet.cse6705.group8.Individual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author sharafat
 */
public class INV_DIST {
    private static final Logger log = LoggerFactory.getLogger(INV_DIST.class);

    private static final String RES_DIRECTORY = "res";
    private static final String TESTDATA_FILENAME = "testdata";
    private static final String EXECTUABLE_COMMAND_LINE;

    private static String identityPermutation;

    static {
        String osDirectory = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) {
            osDirectory = "win";
        } else if (os.startsWith("linux")) {
            osDirectory = "linux";
        } else if (os.startsWith("mac")) {
            osDirectory = "mac";
        }

        final String FILE_SEPARATOR = System.getProperty("file.separator");
        EXECTUABLE_COMMAND_LINE = RES_DIRECTORY + FILE_SEPARATOR + osDirectory + FILE_SEPARATOR + "invdist -f "
                + RES_DIRECTORY + FILE_SEPARATOR + TESTDATA_FILENAME + " -L";
        log.debug("GRAPPA executable command line: {}", EXECTUABLE_COMMAND_LINE);
    }

    public static void populateReversalDistance(List<Individual> population) {
        if (identityPermutation == null) {
            identityPermutation = getIdentityPermutation(population.get(0));
            log.debug("identityPermutation: {}", identityPermutation);
        }

        for (Individual individual : population) {
            String permutation = permutationIntArrayToSpaceSeparatedString(individual.getPermutation());
            writeTestdataFile(permutation, identityPermutation);
            individual.setReversalDistance(getReversalDistance(permutation));
        }
    }

    private static String getIdentityPermutation(Individual individual) {
        int[] unsignedPermutation = new int[individual.getPermutation().length];

        for (int i = 0; i < individual.getPermutation().length; i++) {
            unsignedPermutation[i] = Math.abs(individual.getPermutation()[i]);
        }

        Arrays.sort(unsignedPermutation);

        return permutationIntArrayToSpaceSeparatedString(unsignedPermutation);
    }

    private static String permutationIntArrayToSpaceSeparatedString(int[] permutation) {
        String spaceSeparatedPermutation = "";

        for (int number : permutation) {
            spaceSeparatedPermutation += number + " ";
        }

        return spaceSeparatedPermutation.substring(0, spaceSeparatedPermutation.length() - 1);
    }

    private static void writeTestdataFile(String permutation, String identityPermutation) {
        BufferedWriter testDataFile = null;
        try {
            testDataFile = new BufferedWriter(new FileWriter(RES_DIRECTORY + "/" + TESTDATA_FILENAME));
            testDataFile.write(">S1\n" + permutation + "\n>S2\n" + identityPermutation + "\n");
        } catch (IOException e) {
            log.error("Error writing testdata.", e);
        } finally {
            if (testDataFile != null) {
                try {
                    testDataFile.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    private static int getReversalDistance(String permutation) {
        BufferedReader grappaOutputStreamReader = null;
        BufferedReader grappaErrorStreamReader = null;
        try {
            Process process = Runtime.getRuntime().exec(EXECTUABLE_COMMAND_LINE);
            grappaOutputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line, score, inversionDistance, breakpointDistance;
            score = inversionDistance = breakpointDistance = "";

            boolean grappaRunSuccessful = false;
            while ((line = grappaOutputStreamReader.readLine()) != null) {
                grappaRunSuccessful = true;

                if (line.startsWith("score")) {
                    score = getValue(line);
                } else if (line.startsWith("Inversion Distance")) {
                    inversionDistance = getValue(line);
                } else if (line.startsWith("Breakpoint Distance")) {
                    breakpointDistance = getValue(line);
                }
            }

            if (!grappaRunSuccessful) {
                grappaErrorStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String error = "";
                while ((line = grappaErrorStreamReader.readLine()) != null) {
                    error += line + "\n";
                }
                log.error("INVDIST returned error:\n {}", error);

                System.exit(-1);
            }

            log.trace("permutation: {}, score: {}, inversionDistance: {}, breakpointDistance: {}",
                    new Object[]{permutation, score, inversionDistance, breakpointDistance});
            return Integer.parseInt(inversionDistance);
        } catch (IOException e) {
            log.error("Error running INVDIST program.", e);
        } finally {
            if (grappaOutputStreamReader != null) {
                try {
                    grappaOutputStreamReader.close();
                } catch (IOException ignore) {
                }
            }
            if (grappaErrorStreamReader != null) {
                try {
                    grappaErrorStreamReader.close();
                } catch (IOException ignore) {
                }
            }
        }

        return -1;
    }

    private static String getValue(String line) {
        return line.substring(line.lastIndexOf(' ') + 1);
    }

    private static String prettyPrint(String permutation) {
        String result = "";
        for (int i = 0; i < permutation.length(); i++) {
            if (permutation.charAt(i) == '-') {
                result += permutation.charAt(i);
                result += permutation.charAt(i + 1);
                i++;
            } else {
                result += " " + permutation.charAt(i);
            }
        }

        return result;
    }

    public static void resetIdentityPermutation() {
        identityPermutation = null;
    }
}
