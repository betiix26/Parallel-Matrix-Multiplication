package ace.ucv.approach.stream;

import ace.ucv.model.Matrix;

import java.util.stream.IntStream;

public class StreamParallelMultiplication {

    private final StringBuilder log = new StringBuilder();

    public String getLog() {
        return log.toString();
    }

    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("The number of columns in matrix A must equal the number of rows in matrix B.");
        }

        log.append("=== Parallel Stream Multiplication Started ===\n");

        long totalStart = System.nanoTime(); 

        long readStart = System.nanoTime();
        int rowsA = matrixA.getRows();
        int colsA = matrixA.getCols();
        int colsB = matrixB.getCols();

        int[][] dataA = matrixA.getData();
        int[][] dataB = matrixB.getData();
        long readEnd = System.nanoTime();

        long computeStart = System.nanoTime();

        int[][] resultData = IntStream.range(0, rowsA)
                .parallel()
                .mapToObj(i -> {
                    int[] rowResult = new int[colsB];
                    StringBuilder rowLog = new StringBuilder();
                    for (int j = 0; j < colsB; j++) {
                        int sum = 0;
                        StringBuilder elementLog = new StringBuilder();
                        elementLog.append("Element [").append(i).append(",").append(j).append("]: ");
                        for (int k = 0; k < colsA; k++) {
                            sum += dataA[i][k] * dataB[k][j];
                            elementLog.append(dataA[i][k]).append("*").append(dataB[k][j]);
                            if (k < colsA - 1) elementLog.append(" + ");
                        }
                        elementLog.append(" = ").append(sum).append("\n");
                        rowResult[j] = sum;
                        rowLog.append(elementLog);
                    }

                    synchronized (log) {
                        log.append(rowLog);
                    }
                    return rowResult;
                })
                .toArray(int[][]::new);

        long computeEnd = System.nanoTime();
        long totalEnd = System.nanoTime();

        log.append(String.format("\nData read/preparation time: %.4f ms\n", (readEnd - readStart) / 1_000_000.0));
        log.append(String.format("Computation time: %.4f ms\n", (computeEnd - computeStart) / 1_000_000.0));
        log.append(String.format("Total execution time: %.4f ms\n", (totalEnd - totalStart) / 1_000_000.0));

        log.append("=== Parallel Stream Multiplication Completed ===\n");

        return new Matrix(resultData, rowsA, colsB);
    }
}
