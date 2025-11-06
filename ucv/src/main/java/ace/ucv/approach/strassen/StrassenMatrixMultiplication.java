package ace.ucv.approach.strassen;

import ace.ucv.model.Matrix;
import ace.ucv.utils.MatrixUtility;

//public class StrassenMatrixMultiplication {
//
//    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
//        // validate dimensions
//        if (matrixA.getCols() != matrixB.getRows()) {
//            throw new IllegalArgumentException("The number of columns in the first matrix must be equal to the number of rows in the second.");
//        }
//
//        matrixA = MatrixUtility.padMatrix(matrixA);
//        matrixB = MatrixUtility.padMatrix(matrixB);
//
//        int n = matrixA.getRows();
//
//        if (n == 1) {
//            return new Matrix(new int[][]{
//                    {matrixA.getData()[0][0] * matrixB.getData()[0][0]}
//            }, 1, 1);
//        }
//
//        Matrix[] subMatricesA = MatrixUtility.split(matrixA);
//        Matrix[] subMatricesB = MatrixUtility.split(matrixB);
//
//        Matrix M1 = multiply(MatrixUtility.add(subMatricesA[0], subMatricesA[3]), MatrixUtility.add(subMatricesB[0], subMatricesB[3]));
//        Matrix M2 = multiply(MatrixUtility.add(subMatricesA[2], subMatricesA[3]), subMatricesB[0]);
//        Matrix M3 = multiply(subMatricesA[0], MatrixUtility.subtract(subMatricesB[1], subMatricesB[3]));
//        Matrix M4 = multiply(subMatricesA[3], MatrixUtility.subtract(subMatricesB[2], subMatricesB[0]));
//        Matrix M5 = multiply(MatrixUtility.add(subMatricesA[0], subMatricesA[1]), subMatricesB[3]);
//        Matrix M6 = multiply(MatrixUtility.subtract(subMatricesA[2], subMatricesA[0]), MatrixUtility.add(subMatricesB[0], subMatricesB[1]));
//        Matrix M7 = multiply(MatrixUtility.subtract(subMatricesA[1], subMatricesA[3]), MatrixUtility.add(subMatricesB[2], subMatricesB[3]));
//
//        Matrix C11 = MatrixUtility.add(MatrixUtility.subtract(MatrixUtility.add(M1, M4), M5), M7);
//        Matrix C12 = MatrixUtility.add(M3, M5);
//        Matrix C21 = MatrixUtility.add(M2, M4);
//        Matrix C22 = MatrixUtility.add(MatrixUtility.subtract(MatrixUtility.add(M1, M3), M2), M6);
//
//        Matrix result = MatrixUtility.combine(C11, C12, C21, C22, n);
//
//        return MatrixUtility.cropMatrix(result, matrixA.getRows(), matrixB.getCols());
//    }
//}

public class StrassenMatrixMultiplication {

    private StringBuilder log = new StringBuilder();

    public String getLog() {
        return log.toString();
    }

    public Matrix multiply(Matrix matrixA, Matrix matrixB) {
        return multiplyInternal(matrixA, matrixB);
    }

    private Matrix multiplyInternal(Matrix matrixA, Matrix matrixB) {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("Invalid dimensions for multiplication.");
        }

        int originalRowsA = matrixA.getRows();
        int originalColsB = matrixB.getCols();

        matrixA = MatrixUtility.padMatrix(matrixA);
        matrixB = MatrixUtility.padMatrix(matrixB);
        log.append("Padding matrices to size ").append(matrixA.getRows()).append("x").append(matrixA.getCols()).append("\n");

        int n = matrixA.getRows();

        if (n == 1) {
            int val = matrixA.getData()[0][0] * matrixB.getData()[0][0];
            log.append("Base case multiply: ").append(matrixA.getData()[0][0]).append("*").append(matrixB.getData()[0][0]).append(" = ").append(val).append("\n");
            return new Matrix(new int[][]{{val}}, 1, 1);
        }

        // Split matrices
        Matrix[] subMatricesA = MatrixUtility.split(matrixA);
        Matrix[] subMatricesB = MatrixUtility.split(matrixB);
        log.append("Split matrices into submatrices\n");

        // Compute M1..M7
        Matrix M1 = multiplyInternal(MatrixUtility.add(subMatricesA[0], subMatricesA[3]),
                                     MatrixUtility.add(subMatricesB[0], subMatricesB[3]));
        log.append("Computed M1\n");

        Matrix M2 = multiplyInternal(MatrixUtility.add(subMatricesA[2], subMatricesA[3]),
                                     subMatricesB[0]);
        log.append("Computed M2\n");

        Matrix M3 = multiplyInternal(subMatricesA[0],
                                     MatrixUtility.subtract(subMatricesB[1], subMatricesB[3]));
        log.append("Computed M3\n");

        Matrix M4 = multiplyInternal(subMatricesA[3],
                                     MatrixUtility.subtract(subMatricesB[2], subMatricesB[0]));
        log.append("Computed M4\n");

        Matrix M5 = multiplyInternal(MatrixUtility.add(subMatricesA[0], subMatricesA[1]),
                                     subMatricesB[3]);
        log.append("Computed M5\n");

        Matrix M6 = multiplyInternal(MatrixUtility.subtract(subMatricesA[2], subMatricesA[0]),
                                     MatrixUtility.add(subMatricesB[0], subMatricesB[1]));
        log.append("Computed M6\n");

        Matrix M7 = multiplyInternal(MatrixUtility.subtract(subMatricesA[1], subMatricesA[3]),
                                     MatrixUtility.add(subMatricesB[2], subMatricesB[3]));
        log.append("Computed M7\n");

        // Combine
        Matrix C11 = MatrixUtility.add(MatrixUtility.subtract(MatrixUtility.add(M1, M4), M5), M7);
        Matrix C12 = MatrixUtility.add(M3, M5);
        Matrix C21 = MatrixUtility.add(M2, M4);
        Matrix C22 = MatrixUtility.add(MatrixUtility.subtract(MatrixUtility.add(M1, M3), M2), M6);

        Matrix result = MatrixUtility.combine(C11, C12, C21, C22, n);
        result = MatrixUtility.cropMatrix(result, originalRowsA, originalColsB);
        log.append("Combined submatrices into final result\n");

        return result;
    }
}

