package multiplication;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class ForkJoinSquareMatrixMultiplier {
    private static final int DEFAULT_ROW = -1;

    private ForkJoinSquareMatrixMultiplier() {

    }

    public static double[][] multiply(double[][] matrixA, double[][] matrixB) {
        if (matrixA == null || matrixB == null) {
            throw new IllegalArgumentException("Matrices should not be null");
        }

        if (matrixA[0].length != matrixB.length) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }

        int matrixAHeight = matrixA.length;
        int matrixBWidth = matrixB[0].length;

        double[][] resultMatrix = new double[matrixAHeight][matrixBWidth];
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinOperator(matrixA, matrixB, resultMatrix, DEFAULT_ROW));

        return resultMatrix;
    }

    static class ForkJoinOperator extends RecursiveAction {
        private double[][] matrixA;
        private double[][] matrixB;
        private double[][] resultMatrix;
        private int row;

        private ForkJoinOperator(double[][] matrixA, double[][] matrixB, double[][] resultMatrix, int row) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.resultMatrix = resultMatrix;
            this.row = row;
        }

        @Override
        public void compute() {
            if (row == DEFAULT_ROW) {
                List<ForkJoinOperator> tasks = new LinkedList<>();
                for (int i = 0; i < matrixA.length; i++) {
                    tasks.add(new ForkJoinOperator(matrixA, matrixB, resultMatrix, i));
                }

                invokeAll(tasks);
            } else {
                multiplySingleRowColumn();
            }
        }

        private void multiplySingleRowColumn() {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixA.length; k++) {
                    resultMatrix[row][j] += matrixA[row][k] * matrixB[k][j];
                }
            }
        }
    }

}