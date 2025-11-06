package ace.ucv.approach.stream;

import ace.ucv.model.Matrix;

import java.util.stream.IntStream;
import java.util.stream.IntStream;

public class StreamParallelMultiplication {

    private StringBuilder log = new StringBuilder();

    public String getLog() {
        return log.toString();
    }

    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("The number of columns in matrix A must equal the number of rows in matrix B.");
        }

        int rowsA = matrixA.getRows();
        int colsB = matrixB.getCols();
        int[][] resultData = new int[rowsA][colsB];

        IntStream.range(0, rowsA).parallel().forEach(i -> {
            for (int j = 0; j < colsB; j++) {
                int sum = 0;
                StringBuilder elementLog = new StringBuilder();
                elementLog.append("Calculating element [").append(i).append(",").append(j).append("]: ");
                for (int k = 0; k < matrixA.getCols(); k++) {
                    sum += matrixA.getData()[i][k] * matrixB.getData()[k][j];
                    elementLog.append(matrixA.getData()[i][k]).append("*").append(matrixB.getData()[k][j]);
                    if (k < matrixA.getCols() - 1) elementLog.append(" + ");
                }
                resultData[i][j] = sum;
                elementLog.append(" = ").append(sum).append("\n");

                synchronized (log) {
                    log.append(elementLog);
                }
            }
        });

        return new Matrix(resultData, rowsA, colsB);
    }
}
