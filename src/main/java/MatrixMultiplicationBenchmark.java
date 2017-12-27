import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;


public class MatrixMultiplicationBenchmark {

    /**
     * Performs different matrix multiplication algorithm comparison for squared matrices
     */
    public static void main(String[] args) {
        int dimensionSizeStart = 100;
        int dimensionSizeFinish = 300;

        MultiValueMap naiveClassicMetrics = new MultiValueMap();
        MultiValueMap naiveCustomMetrics = new MultiValueMap();
        MultiValueMap concurrentClassicMetrics = new MultiValueMap();
        MultiValueMap concurrentCustomMetrics = new MultiValueMap();

        for (int i = dimensionSizeStart; i <= dimensionSizeFinish; i += dimensionSizeStart * 0.1) {
            double dataSize = i * i;

            double[][] a = MatrixHelperUtil.random(i, i);
            double[][] b = MatrixHelperUtil.random(i, i);

            StopWatch stopWatchNaiveClassic = StopWatch.createStarted();
            NaiveSquaredMatrixMultiplier.multiplyNaivePrinceton(a, b);
            long execTimeNaiveClassic = stopWatchNaiveClassic.getTime(TimeUnit.MILLISECONDS);
            naiveClassicMetrics.put(dataSize, (double) execTimeNaiveClassic);

            StopWatch stopWatchNaiveCustom = StopWatch.createStarted();
            NaiveSquaredMatrixMultiplier.multiplyNaiveCustom(a, b);
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

}
