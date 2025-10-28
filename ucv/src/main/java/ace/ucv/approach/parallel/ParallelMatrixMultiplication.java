package ace.ucv.approach.parallel;

import ace.ucv.model.Matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixMultiplication {

    public Matrix multiply(Matrix matrixA, Matrix matrixB) throws InterruptedException {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("The number of columns in matrix A must equal the number of rows in matrix B.");
        }

        int rowsA = matrixA.getRows();
        int colsB = matrixB.getCols();
        int[][] resultData = new int[rowsA][colsB];

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int fromRow = i * rowsA / numThreads;
            final int toRow = (i + 1) * rowsA / numThreads;

            executor.submit(() -> {
                for (int row = fromRow; row < toRow; row++) {
                    for (int col = 0; col < colsB; col++) {
                        for (int k = 0; k < matrixA.getCols(); k++) {
                            resultData[row][col] += matrixA.getData()[row][k] * matrixB.getData()[k][col];
                        }
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        return new Matrix(resultData, rowsA, colsB);
    }

}