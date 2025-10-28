package ace.ucv.approach.stream;

import ace.ucv.model.Matrix;

import java.util.stream.IntStream;

public class StreamParallelMultiplication {

    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("The number of columns in matrix A must equal the number of rows in matrix B.");
        }

        int rowsA = matrixA.getRows();
        int colsB = matrixB.getCols();
        int[][] resultData = new int[rowsA][colsB];
        
        IntStream.range(0, rowsA).parallel().forEach(i -> {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < matrixA.getCols(); k++) {
                    resultData[i][j] += matrixA.getData()[i][k] * matrixB.getData()[k][j];
                }
            }
        });

        return new Matrix(resultData, rowsA, colsB);
    }

}
