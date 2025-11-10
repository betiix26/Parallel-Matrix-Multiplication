package ace.ucv.approach.strassen;

import ace.ucv.model.Matrix;
import ace.ucv.utils.MatrixUtility;

public class StrassenMatrixMultiplication {

    private final StringBuilder log = new StringBuilder();

    public String getLog() {
        return log.toString();
    }

    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
        log.append("=== Strassen Multiplication Started ===\n");

        long totalStart = System.nanoTime();
        long readStart = System.nanoTime();

        int originalRowsA = matrixA.getRows();
        int originalColsB = matrixB.getCols();

        matrixA = MatrixUtility.padMatrix(matrixA);
        matrixB = MatrixUtility.padMatrix(matrixB);

        long readEnd = System.nanoTime();
        double readTime = (readEnd - readStart) / 1_000_000.0;

        long computeStart = System.nanoTime();
        Matrix result = multiplyInternal(matrixA, matrixB, originalRowsA, originalColsB);
        long computeEnd = System.nanoTime();
        double computeTime = (computeEnd - computeStart) / 1_000_000.0;

        long totalEnd = System.nanoTime();
        double totalTime = (totalEnd - totalStart) / 1_000_000.0;

        log.append(String.format("Data read/preparation time: %.4f ms\n", readTime));
        log.append(String.format("Computation time: %.4f ms\n", computeTime));
        log.append(String.format("Total execution time: %.4f ms\n", totalTime));
        log.append("=== Strassen Multiplication Completed ===\n");

        return result;
    }

    private Matrix multiplyInternal(Matrix matrixA, Matrix matrixB, int originalRowsA, int originalColsB) {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("Invalid dimensions for multiplication.");
        }

        int n = matrixA.getRows();

        if (n == 1) {
            int val = matrixA.getData()[0][0] * matrixB.getData()[0][0];
            log.append("Base case multiply: ").append(matrixA.getData()[0][0])
               .append("*").append(matrixB.getData()[0][0]).append(" = ").append(val).append("\n");
            return new Matrix(new int[][]{{val}}, 1, 1);
        }

        Matrix[] subMatricesA = MatrixUtility.split(matrixA);
        Matrix[] subMatricesB = MatrixUtility.split(matrixB);
        log.append("Split matrices into submatrices\n");

        Matrix M1 = multiplyInternal(MatrixUtility.add(subMatricesA[0], subMatricesA[3]),
                                     MatrixUtility.add(subMatricesB[0], subMatricesB[3]), n/2, n/2);
        log.append("Computed M1\n");

        Matrix M2 = multiplyInternal(MatrixUtility.add(subMatricesA[2], subMatricesA[3]),
                                     subMatricesB[0], n/2, n/2);
        log.append("Computed M2\n");

        Matrix M3 = multiplyInternal(subMatricesA[0],
                                     MatrixUtility.subtract(subMatricesB[1], subMatricesB[3]), n/2, n/2);
        log.append("Computed M3\n");

        Matrix M4 = multiplyInternal(subMatricesA[3],
                                     MatrixUtility.subtract(subMatricesB[2], subMatricesB[0]), n/2, n/2);
        log.append("Computed M4\n");

        Matrix M5 = multiplyInternal(MatrixUtility.add(subMatricesA[0], subMatricesA[1]),
                                     subMatricesB[3], n/2, n/2);
        log.append("Computed M5\n");

        Matrix M6 = multiplyInternal(MatrixUtility.subtract(subMatricesA[2], subMatricesA[0]),
                                     MatrixUtility.add(subMatricesB[0], subMatricesB[1]), n/2, n/2);
        log.append("Computed M6\n");

        Matrix M7 = multiplyInternal(MatrixUtility.subtract(subMatricesA[1], subMatricesA[3]),
                                     MatrixUtility.add(subMatricesB[2], subMatricesB[3]), n/2, n/2);
        log.append("Computed M7\n");

        // Combine
        Matrix C11 = MatrixUtility.add(MatrixUtility.subtract(MatrixUtility.add(M1, M4), M5), M7);
        Matrix C12 = MatrixUtility.add(M3, M5);
        Matrix C21 = MatrixUtility.add(M2, M4);
        Matrix C22 = MatrixUtility.add(MatrixUtility.subtract(MatrixUtility.add(M1, M3), M2), M6);

        Matrix result = MatrixUtility.combine(C11, C12, C21, C22, n);
        return MatrixUtility.cropMatrix(result, originalRowsA, originalColsB);
    }
}
