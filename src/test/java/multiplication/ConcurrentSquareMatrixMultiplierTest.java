package multiplication;


import multiplication.util.MatrixHelperUtil;
import org.junit.Test;


import static multiplication.ConcurrentSquareMatrixMultiplier.MultiplicationAlgorithm.CLASSIC;
import static multiplication.ConcurrentSquareMatrixMultiplier.MultiplicationAlgorithm.CUSTOM;
import static org.junit.Assert.assertTrue;

public class ConcurrentSquareMatrixMultiplierTest {

    @Test
    public void shouldMultiplyCorrectly() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 200);

        double[][] expected = NaiveSquaredMatrixMultiplier.multiplyNaivePrinceton(matrixA, matrixB);
        double[][] actual = ConcurrentSquareMatrixMultiplier.multiply(matrixA, matrixB, CLASSIC);
        double[][] actualCustom = ConcurrentSquareMatrixMultiplier.multiply(matrixA, matrixB, CUSTOM);

        assertTrue(MatrixHelperUtil.equals(expected, actual));
        assertTrue(MatrixHelperUtil.equals(expected, actualCustom));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectDimensionsClassic() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 300);

        ConcurrentSquareMatrixMultiplier.multiply(matrixA, matrixB, CLASSIC);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnIncorrectDimensionsCustom() {

        double[][] matrixA = MatrixHelperUtil.random(200, 200);
        double[][] matrixB = MatrixHelperUtil.random(200, 300);

        ConcurrentSquareMatrixMultiplier.multiply(matrixA, matrixB, CUSTOM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullMatrix() {
        ConcurrentSquareMatrixMultiplier.multiply(null, null, CUSTOM);
    }
}