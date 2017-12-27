import java.util.Random;

final class MatrixHelperUtil {
    private MatrixHelperUtil() {

    }

    void assertEqual(double[][] expected, double[][] actual) {
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

    static double[][] random(int x, int y) {
        double[][] res = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                res[i][j] = new Random().nextInt(1000);
            }
        }

        return res;
    }

}
