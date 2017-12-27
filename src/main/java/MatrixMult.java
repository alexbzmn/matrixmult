import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;


public class MatrixMult {

    public static void main(String[] args) {
        int dimensionSizeStart = 100;
        int dimensionSizeFinish = 300;

        MultiValueMap naiveClassicMetrics = new MultiValueMap();
        MultiValueMap naiveCustomMetrics = new MultiValueMap();
        MultiValueMap concurrentClassicMetrics = new MultiValueMap();
        MultiValueMap concurrentCustomMetrics = new MultiValueMap();

        for (int i = dimensionSizeStart; i <= dimensionSizeFinish; i += dimensionSizeStart * 0.1) {
            double dataSize = i * i;

            double[][] a = random(i, i);
            double[][] b = random(i, i);

            StopWatch stopWatchNaiveClassic = StopWatch.createStarted();
            multiplyNaivePrinceton(a, b);
            long execTimeNaiveClassic = stopWatchNaiveClassic.getTime(TimeUnit.MILLISECONDS);
            naiveClassicMetrics.put(dataSize, (double) execTimeNaiveClassic);

            StopWatch stopWatchNaiveCustom = StopWatch.createStarted();
            multiplyNaiveCustom(a, b);
            long execTimeNaiveCustom = stopWatchNaiveCustom.getTime(TimeUnit.MILLISECONDS);
            naiveCustomMetrics.put(dataSize, (double) execTimeNaiveCustom);

            StopWatch stopWatchConcurrentClassic = StopWatch.createStarted();
            ConcurrentSquareMatrixMultiplier.multiply(a, b, ConcurrentSquareMatrixMultiplier.MultiplicationAlgorithm.CLASSIC);
            long execTimeConcurrentClassic = stopWatchConcurrentClassic.getTime(TimeUnit.MILLISECONDS);
            concurrentClassicMetrics.put(dataSize, (double) execTimeConcurrentClassic);

            StopWatch stopWatchConcurrentCustom = StopWatch.createStarted();
            ConcurrentSquareMatrixMultiplier.multiply(a, b, ConcurrentSquareMatrixMultiplier.MultiplicationAlgorithm.CUSTOM);
            long execTimeConcurrentCustom = stopWatchConcurrentCustom.getTime(TimeUnit.MILLISECONDS);
            concurrentCustomMetrics.put(dataSize, (double) execTimeConcurrentCustom);

            System.out.println(MessageFormat.format("{0}%", (((double) i / dimensionSizeFinish) * 100)));
        }

        ComparisonPlot.display(
                new ImmutablePair<>("Naive Classic", naiveClassicMetrics),
                new ImmutablePair<>("Naive Custom", naiveCustomMetrics),
                new ImmutablePair<>("Concurrent Classic", concurrentClassicMetrics),
                new ImmutablePair<>("Concurrent Custom", concurrentCustomMetrics)
        );
    }

    static void printMatrix(double[][] matrix) {
        Arrays.stream(matrix).forEach(doubles -> System.out.println(Arrays.toString(doubles)));
    }

    private static void assertEqual(double[][] expected, double[][] actual) {
        int x = expected.length;
        int y = expected[0].length;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (expected[i][j] != actual[i][j]) {
                    throw new AssertionError("Matrices are not equal!");
                }
            }
        }
    }

    private static double[][] random(int x, int y) {
        double[][] res = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                res[i][j] = new Random().nextInt(1000);
            }
        }

        return res;
    }

    private static double[][] multiplyNaiveCustom(double[][] a, double[][] b) {
        int n = a.length;
        int m = a[0].length;
        int t = b.length;
        int p = b[0].length;

        if (m != t) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] res = new double[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    res[i][k] += a[i][j] * b[j][k];
                }
            }
        }

        return res;
    }

    /**
     * Implementation is taken from Princeton library:
     * https://introcs.cs.princeton.edu/java/22library/Matrix.java.html
     *
     * @param a
     * @param b
     * @return
     */
    private static double[][] multiplyNaivePrinceton(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }
}
