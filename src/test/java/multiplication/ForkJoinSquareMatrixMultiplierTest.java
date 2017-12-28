package multiplication;

import multiplication.util.MatrixHelperUtil;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class ForkJoinSquareMatrixMultiplierTest {

    @Test
    public void shouldMultiplyCorrectly() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 200);

        double[][] expected = NaiveSquareMatrixMultiplier.multiplyNaivePrinceton(matrixA, matrixB);
        double[][] actual = ForkJoinSquareMatrixMultiplier.multiply(matrixA, matrixB);

        assertTrue(MatrixHelperUtil.equals(expected, actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectDimensions() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 300);

        ForkJoinSquareMatrixMultiplier.multiply(matrixA, matrixB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullMatrix() {
        ForkJoinSquareMatrixMultiplier.multiply(null, null);
    }

}