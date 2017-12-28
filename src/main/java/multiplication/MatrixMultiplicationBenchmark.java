package multiplication;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import multiplication.util.ComparisonPlot;
import multiplication.util.MatrixHelperUtil;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutablePair;


import static multiplication.ConcurrentSquareMatrixMultiplier.MultiplicationAlgorithm.CLASSIC;
import static multiplication.ConcurrentSquareMatrixMultiplier.MultiplicationAlgorithm.CUSTOM;


public class MatrixMultiplicationBenchmark {

    /**
     * Performs different matrix multiplication algorithm comparison for squared matrices
     */
    public static void main(String[] args) {
        int dimensionSizeStart = 100;
        int dimensionSizeFinish = 900;

        MultiValueMap naiveClassicMetrics = new MultiValueMap();
        MultiValueMap naiveCustomMetrics = new MultiValueMap();
        MultiValueMap concurrentClassicMetrics = new MultiValueMap();
        MultiValueMap concurrentCustomMetrics = new MultiValueMap();
        MultiValueMap concurrentForkJoinMetrics = new MultiValueMap();

        for (int i = dimensionSizeStart; i <= dimensionSizeFinish; i += dimensionSizeStart * 0.3) {
            double dataSize = i * i;

            double[][] a = MatrixHelperUtil.random(i, i);
            double[][] b = MatrixHelperUtil.random(i, i);

            executeExperiment(() -> NaiveSquareMatrixMultiplier.multiplyNaivePrinceton(a, b), naiveClassicMetrics, dataSize);
            executeExperiment(() -> NaiveSquareMatrixMultiplier.multiplyNaiveCustom(a, b), naiveCustomMetrics, dataSize);
            executeExperiment(() -> ConcurrentSquareMatrixMultiplier.multiply(a, b, CLASSIC), concurrentClassicMetrics, dataSize);
            executeExperiment(() -> ForkJoinSquareMatrixMultiplier.multiply(a, b), concurrentForkJoinMetrics, dataSize);
            executeExperiment(() -> ConcurrentSquareMatrixMultiplier.multiply(a, b, CUSTOM), concurrentCustomMetrics, dataSize);

            System.out.println(MessageFormat.format("{0}%", (((double) i / dimensionSizeFinish) * 100)));
        }

        ComparisonPlot.display(
                new ImmutablePair<>("Naive Classic", naiveClassicMetrics),
                new ImmutablePair<>("Naive Custom", naiveCustomMetrics),
                new ImmutablePair<>("Concurrent Classic", concurrentClassicMetrics),
                new ImmutablePair<>("Fork/Join", concurrentForkJoinMetrics),
                new ImmutablePair<>("Concurrent Custom", concurrentCustomMetrics)
        );
    }

    private static void executeExperiment(MatrixMultiplicationRunner matrixMultiplicationRunner,
                                          MultiValueMap valueMap, double dataSize) {
        StopWatch stopWatch = StopWatch.createStarted();
        matrixMultiplicationRunner.run();
        long execTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
        valueMap.put(dataSize, (double) execTime);
    }

}
