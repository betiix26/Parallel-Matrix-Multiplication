package ace.ucv.approach.sequential;

import ace.ucv.model.Matrix;

public class SequentialMatrixMultiplication {

    private final StringBuilder log = new StringBuilder();

    public String getLog() {
        return log.toString();
    }

    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException(
                    "The number of columns in the first matrix must be equal to the number of rows in the second.");
        }

        log.append("=== Sequential Multiplication Started ===\n");

        long readStart = System.nanoTime();

        int rowsA = matrixA.getRows();
        int colsA = matrixA.getCols();
        int colsB = matrixB.getCols();

        int[][] dataA = matrixA.getData();
        int[][] dataB = matrixB.getData();

        long readEnd = System.nanoTime();
        

        long computeStart = System.nanoTime();

        int[][] resultData = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                int sum = 0;
                StringBuilder elementLog = new StringBuilder();
                elementLog.append("Calculating element [").append(i).append(",").append(j).append("]: ");

                for (int k = 0; k < colsA; k++) {
                    sum += dataA[i][k] * dataB[k][j];
                    elementLog.append(dataA[i][k]).append("*").append(dataB[k][j]);
                    if (k < colsA - 1) elementLog.append(" + ");
                }

                resultData[i][j] = sum;
                elementLog.append(" = ").append(sum).append("\n");
                log.append(elementLog);
            }
        }

        long computeEnd = System.nanoTime();
        log.append(String.format("Data read/preparation time: %.4f ms\n", (readEnd - readStart) / 1_000_000.0));
        log.append(String.format("Computation time: %.4f ms\n", (computeEnd - computeStart) / 1_000_000.0));
        log.append("=== Sequential Multiplication Completed ===\n");

        return new Matrix(resultData, rowsA, colsB);
    }
}
