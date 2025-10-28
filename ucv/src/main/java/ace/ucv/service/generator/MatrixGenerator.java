package ace.ucv.service.generator;

import ace.ucv.model.Matrix;

import java.util.Random;

public class MatrixGenerator {
    private final Random rand = new Random();
    private final RandomMatrixGenerator randomMatrixGenerator = new RandomMatrixGenerator();

    public Matrix[] generateRandomMatrices(int rowsMin, int rowsMax, int colsMin, int colsMax) {
        int rowsA = rand.nextInt(rowsMax - rowsMin + 1) + rowsMin;
        int colsA = rand.nextInt(colsMax - colsMin + 1) + colsMin;
        int rowsB = colsA;
        int colsB = rand.nextInt(colsMax - colsMin + 1) + colsMin;

        Matrix matrixA = randomMatrixGenerator.generateRandomMatrix(rowsA, colsA);
        Matrix matrixB = randomMatrixGenerator.generateRandomMatrix(rowsB, colsB);

        return new Matrix[]{matrixA, matrixB};
    }
}