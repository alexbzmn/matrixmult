package multiplication;

import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class NaiveSquaredMatrixMultiplierTest {

    @Test
    public void shouldMultiplyCorrectly() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 200);

        double[][] expected = NaiveSquaredMatrixMultiplier.multiplyNaivePrinceton(matrixA, matrixB);
        double[][] actual = NaiveSquaredMatrixMultiplier.multiplyNaiveCustom(matrixA, matrixB);

        assertTrue(MatrixHelperUtil.equals(expected, actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectDimensions() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 300);

        NaiveSquaredMatrixMultiplier.multiplyNaiveCustom(matrixA, matrixB);
    }

}