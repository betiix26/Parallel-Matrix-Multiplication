package ace.ucv.approach.parallel;

import ace.ucv.model.Matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixMultiplication {

    private final StringBuilder log = new StringBuilder();

    public String getLog() {
        return log.toString();
    }

    public Matrix multiply(Matrix matrixA, Matrix matrixB) throws InterruptedException {
        log.append("=== Parallel Multiplication Started ===\n");

        long totalStart = System.nanoTime();
        long readStart = System.nanoTime();

        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException(
                    "The number of columns in matrix A must equal the number of rows in matrix B.");
        }

        int rowsA = matrixA.getRows();
        int colsB = matrixB.getCols();
        int[][] dataA = matrixA.getData();
        int[][] dataB = matrixB.getData();
        int[][] resultData = new int[rowsA][colsB];

        long readEnd = System.nanoTime();
        double readTime = (readEnd - readStart) / 1_000_000.0;

        long computeStart = System.nanoTime();

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int fromRow = i * rowsA / numThreads;
            final int toRow = (i + 1) * rowsA / numThreads;

            executor.submit(() -> {
                for (int row = fromRow; row < toRow; row++) {
                    for (int col = 0; col < colsB; col++) {
                        int sum = 0;
                        StringBuilder elementLog = new StringBuilder();
                        elementLog.append("Calculating element [").append(row).append(",").append(col).append("]: ");
                        for (int k = 0; k < matrixA.getCols(); k++) {
                            sum += dataA[row][k] * dataB[k][col];
                            elementLog.append(dataA[row][k]).append("*").append(dataB[k][col]);
                            if (k < matrixA.getCols() - 1) elementLog.append(" + ");
                        }
                        resultData[row][col] = sum;
                        elementLog.append(" = ").append(sum).append("\n");

                        synchronized (log) {
                            log.append(elementLog);
                        }
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        long computeEnd = System.nanoTime();
        double computeTime = (computeEnd - computeStart) / 1_000_000.0;
        double totalTime = (System.nanoTime() - totalStart) / 1_000_000.0;

        log.append(String.format("Data read/preparation time: %.4f ms\n", readTime));
        log.append(String.format("Computation time: %.4f ms\n", computeTime));
        log.append(String.format("Total execution time: %.4f ms\n", totalTime));
        log.append("=== Parallel Multiplication Completed ===\n");

        return new Matrix(resultData, rowsA, colsB);
    }
}
