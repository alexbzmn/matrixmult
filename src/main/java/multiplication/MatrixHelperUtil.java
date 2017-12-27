package multiplication;

import java.util.Random;

final class MatrixHelperUtil {
    private MatrixHelperUtil() {

    }

    static boolean equals(double[][] expected, double[][] actual) {
        int x = expected.length;
        int y = expected[0].length;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (expected[i][j] != actual[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    static double[][] random(int x, int y) {
        double[][] res = new double[y][y];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                res[i][j] = new Random().nextInt(1000);
            }
        }

        return res;
    }

}
