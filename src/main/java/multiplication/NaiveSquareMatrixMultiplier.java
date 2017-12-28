package multiplication;

final class NaiveSquareMatrixMultiplier {

    private NaiveSquareMatrixMultiplier() {
    }

    /**
     * Multiplication with row-to-row optimization.
     * It performs better for me(my machine) due to reduced number of pointer movements for first matrix
     * and fact that matrices are squared (from task description).
     *
     * @param matrixA multiplicand
     * @param matrixB multiplier
     * @return matrix multiplication product
     */
    static double[][] multiplyNaiveCustom(double[][] matrixA, double[][] matrixB) {
        if (matrixA == null || matrixB == null) {
            throw new IllegalArgumentException("Matrices should not be null");
        }

        int matrixARowNumber = matrixA.length;
        int matrixBRowNumber = matrixB.length;
        int matrixAColumnNumber = matrixA[0].length;
        int matrixBColumnNumber = matrixB[0].length;

        if (matrixAColumnNumber != matrixBRowNumber) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }
        double[][] resultMatrix = new double[matrixARowNumber][matrixBColumnNumber];

        for (int i = 0; i < matrixARowNumber; i++) {
            for (int j = 0; j < matrixAColumnNumber; j++) {
                for (int k = 0; k < matrixBColumnNumber; k++) {
                    resultMatrix[i][k] += matrixA[i][j] * matrixB[j][k];
                }
            }
        }

        return resultMatrix;
    }

    /**
     * Implementation is taken from Princeton library(and considered as a "classic" one):
     * https://introcs.cs.princeton.edu/java/22library/Matrix.java.html
     *
     * @param a multiplicand
     * @param b multiplier
     * @return matrix multiplication product
     */
    static double[][] multiplyNaivePrinceton(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new IllegalArgumentException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }
}
