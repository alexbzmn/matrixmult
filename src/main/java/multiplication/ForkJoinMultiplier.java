package multiplication;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinMultiplier {
    private static final int SIZE = 32;
    private static final int THRESHOLD = 8;

    private double[][] a = new double[SIZE][SIZE];
    private double[][] b = new double[SIZE][SIZE];
    private double[][] c = new double[SIZE][SIZE];

    private ForkJoinPool forkJoinPool;

    double[][] multiply(double[][] a, double[][] b) {
        MatrixMultiplyTask mainTask = new MatrixMultiplyTask(a, 0, 0, b, 0, 0, c, 0, 0, SIZE);
        forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(mainTask);

        return c;
    }

    private class MatrixMultiplyTask extends RecursiveAction {
        private final double[][] A;
        private final int aRow;
        private final int aCol;

        private final double[][] B;
        private final int bRow;
        private final int bCol;

        private final double[][] C;
        private final int cRow;
        private final int cCol;

        private final int size;

        MatrixMultiplyTask(double[][] A, int aRow, int aCol, double[][] B,
                           int bRow, int bCol, double[][] C, int cRow, int cCol, int size) {
            this.A = A;
            this.aRow = aRow;
            this.aCol = aCol;
            this.B = B;
            this.bRow = bRow;
            this.bCol = bCol;
            this.C = C;
            this.cRow = cRow;
            this.cCol = cCol;
            this.size = size;
        }

        private ForkJoinTask<?> seq(final ForkJoinTask<?>... tasks) {
            return adapt(() -> {
                for (ForkJoinTask<?> task : tasks) {
                    task.invoke();
                }
            });
        }

        @Override
        protected void compute() {
            if (size <= THRESHOLD) {
                multiplyStride2();
            } else {

                int h = size / 2;

                invokeAll(seq(
                        new MatrixMultiplyTask(A, aRow, aCol, // A11
                                B, bRow, bCol, // B11
                                C, cRow, cCol, // C11
                                h),

                        new MatrixMultiplyTask(A, aRow, aCol + h, // A12
                                B, bRow + h, bCol, // B21
                                C, cRow, cCol, // C11
                                h),

                        new MatrixMultiplyTask(A, aRow, aCol, // A11
                                B, bRow, bCol + h, // B12
                                C, cRow, cCol + h, // C12
                                h),

                        new MatrixMultiplyTask(A, aRow, aCol + h, // A12
                                B, bRow + h, bCol + h, // B22
                                C, cRow, cCol + h, // C12
                                h),

                        new MatrixMultiplyTask(A, aRow + h, aCol, // A21
                                B, bRow, bCol, // B11
                                C, cRow + h, cCol, // C21
                                h),

                        new MatrixMultiplyTask(A, aRow + h, aCol + h, // A22
                                B, bRow + h, bCol, // B21
                                C, cRow + h, cCol, // C21
                                h),

                        new MatrixMultiplyTask(A, aRow + h, aCol, // A21
                                B, bRow, bCol + h, // B12
                                C, cRow + h, cCol + h, // C22
                                h),

                        new MatrixMultiplyTask(A, aRow + h, aCol + h, // A22
                                B, bRow + h, bCol + h, // B22
                                C, cRow + h, cCol + h, // C22
                                h)));
            }
        }

        private void multiplyStride2() {
            for (int j = 0; j < size; j += 2) {
                for (int i = 0; i < size; i += 2) {

                    double[] a0 = A[aRow + i];
                    double[] a1 = A[aRow + i + 1];

                    double s00 = 0.0;
                    double s01 = 0.0;
                    double s10 = 0.0;
                    double s11 = 0.0;

                    for (int k = 0; k < size; k += 2) {

                        double[] b0 = B[bRow + k];

                        s00 += a0[aCol + k] * b0[bCol + j];
                        s10 += a1[aCol + k] * b0[bCol + j];
                        s01 += a0[aCol + k] * b0[bCol + j + 1];
                        s11 += a1[aCol + k] * b0[bCol + j + 1];

                        double[] b1 = B[bRow + k + 1];

                        s00 += a0[aCol + k + 1] * b1[bCol + j];
                        s10 += a1[aCol + k + 1] * b1[bCol + j];
                        s01 += a0[aCol + k + 1] * b1[bCol + j + 1];
                        s11 += a1[aCol + k + 1] * b1[bCol + j + 1];
                    }

                    C[cRow + i][cCol + j] += s00;
                    C[cRow + i][cCol + j + 1] += s01;
                    C[cRow + i + 1][cCol + j] += s10;
                    C[cRow + i + 1][cCol + j + 1] += s11;
                }
            }
        }
    }
}
