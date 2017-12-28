package multiplication;

import multiplication.util.MatrixHelperUtil;
import org.junit.Test;


import static org.junit.Assert.*;

public class ForkJoinMultiplierTest {

    @Test
    public void shouldMultiplyCorrectly() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 200);

        double[][] expected = NaiveSquaredMatrixMultiplier.multiplyNaivePrinceton(matrixA, matrixB);
        double[][] actual = new ForkJoinMultiplier().multiply(matrixA, matrixB);

        MatrixHelperUtil.printMatrix(expected);
        System.out.println("\n");
        MatrixHelperUtil.printMatrix(actual);

        assertTrue(MatrixHelperUtil.equals(expected, actual));
    }

}