package multiplication;

final class NaiveSquaredMatrixMultiplier {

    private NaiveSquaredMatrixMultiplier() {
    }

    /**
     * Multiplication with row-to-row optimization.
     * It performs better for me(my machine) due to reduced number of pointer movements for first matrix
     * and fact that matrices are squared (from task description).
     *
     * @param a multiplicand
     * @param b multiplier
     * @return matrix multiplication product
     */
    static double[][] multiplyNaiveCustom(double[][] a, double[][] b) {
        int n = a.length;
        int m = a[0].length;
        int t = b.length;
        int p = b[0].length;

        if (m != t) {
            throw new IllegalArgumentException("Illegal matrix dimensions.");
        }
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
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }
}
