package multiplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class ConcurrentSquareMatrixMultiplier {

    private ConcurrentSquareMatrixMultiplier() {

    }

    enum MultiplicationAlgorithm {
        CLASSIC, CUSTOM
    }

    static double[][] multiply(double[][] matrixA, double[][] matrixB,
                               MultiplicationAlgorithm multiplicationAlgorithm) {

        if (matrixA == null || matrixB == null) {
            throw new IllegalArgumentException("Matrices should not be null");
        }

        if (matrixA[0].length != matrixB.length) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }

        int matrixAHeight = matrixA.length;
        int matrixBWidth = matrixB[0].length;

        double[][] resultMatrix = new double[matrixAHeight][matrixBWidth];

        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);

        if (multiplicationAlgorithm == MultiplicationAlgorithm.CLASSIC) {
            multiplyNaiveClassic(matrixA, matrixB, matrixAHeight, matrixBWidth, resultMatrix, service);
        } else {
            multiplyNaiveOptimized(matrixA, matrixB, matrixAHeight, matrixBWidth, resultMatrix, service);
        }

        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return resultMatrix;
    }

    private static void multiplyNaiveOptimized(double[][] matrixA, double[][] matrixB,
                                               int matrixAHeight, int matrixBWidth,
                                               double[][] resultMatrix,
                                               ExecutorService service) {
        for (int i = 0; i < matrixAHeight; i++) {
            int matrixHorizontalLength = matrixA[i].length;
            int matrixARowNumber = i;

            service.execute(() -> {
                for (int j = 0; j < matrixHorizontalLength; j++) {
                    for (int k = 0; k < matrixBWidth; k++) {
                        resultMatrix[matrixARowNumber][k] += matrixA[matrixARowNumber][j] * matrixB[j][k];
                    }
                }
            });
        }
    }

    private static void multiplyNaiveClassic(double[][] matrixA, double[][] matrixB,
                                             int matrixAHeight, int matrixBWidth,
                                             double[][] resultMatrix,
                                             ExecutorService service) {
        for (int i = 0; i < matrixAHeight; i++) {
            for (int j = 0; j < matrixBWidth; j++) {
                int matrixARowNumber = i;
                int matrixBColumnNumber = j;

                service.execute(() -> {
                    double productSum = 0;

                    double[] matrixARow = matrixA[matrixARowNumber];
                    for (int k = 0; k < matrixARow.length; k++) {
                        double matrixAElement = matrixARow[k];
                        double matrixBElement = matrixB[k][matrixBColumnNumber];

                        productSum += matrixAElement * matrixBElement;
                    }

                    resultMatrix[matrixARowNumber][matrixBColumnNumber] = productSum;
                });
            }
        }
    }

}
