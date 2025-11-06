package ace.ucv.approach.fork_join;

import ace.ucv.model.Matrix;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class ForkJoinStreamMatrixMultiplication {
    private static final int THRESHOLD = 64;
    private static ForkJoinPool pool;
    private static StringBuilder log = new StringBuilder();

    public static void setThreadPoolSize(int numThreads) {
        pool = new ForkJoinPool(numThreads);
    }

    public static String getLog() {
        return log.toString();
    }

    public static Matrix multiply(Matrix A, Matrix B) {
        if (A.getCols() != B.getRows()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }

        int rows = A.getRows();
        int cols = B.getCols();
        int common = A.getCols();
        Matrix result = new Matrix(rows, cols);

        if (pool == null) {
            pool = new ForkJoinPool();
        }

        pool.invoke(new MatrixMultiplyTask(A, B, result, 0, rows, 0, cols, common));
        return result;
    }

    static class MatrixMultiplyTask extends RecursiveTask<Void> {
        private final Matrix A, B, result;
        private final int rowStart, rowEnd, colStart, colEnd, common;

        public MatrixMultiplyTask(Matrix matrixA, Matrix matrixB, Matrix result, int rowStart, int rowEnd, int colStart, int colEnd, int common) {
            this.A = matrixA;
            this.B = matrixB;
            this.result = result;
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            this.colStart = colStart;
            this.colEnd = colEnd;
            this.common = common;
        }

        @Override
        protected Void compute() {
            int rowCount = rowEnd - rowStart;
            int colCount = colEnd - colStart;

            if (rowCount * colCount <= THRESHOLD) {
                IntStream.range(rowStart, rowEnd).forEach(i -> {
                    for (int j = colStart; j < colEnd; j++) {
                        int sum = 0;
                        StringBuilder elementLog = new StringBuilder();
                        elementLog.append("Calculating element [").append(i).append(",").append(j).append("]: ");
                        for (int k = 0; k < common; k++) {
                            sum += A.getValue(i, k) * B.getValue(k, j);
                            elementLog.append(A.getValue(i, k)).append("*").append(B.getValue(k, j));
                            if (k < common - 1) elementLog.append(" + ");
                        }
                        result.setValue(i, j, sum);
                        elementLog.append(" = ").append(sum).append("\n");
                        synchronized (log) {
                            log.append(elementLog);
                        }
                    }
                });
            } else {
                int midRow = (rowStart + rowEnd) / 2;
                int midCol = (colStart + colEnd) / 2;

                MatrixMultiplyTask topLeft = new MatrixMultiplyTask(A, B, result, rowStart, midRow, colStart, midCol, common);
                MatrixMultiplyTask topRight = new MatrixMultiplyTask(A, B, result, rowStart, midRow, midCol, colEnd, common);
                MatrixMultiplyTask bottomLeft = new MatrixMultiplyTask(A, B, result, midRow, rowEnd, colStart, midCol, common);
                MatrixMultiplyTask bottomRight = new MatrixMultiplyTask(A, B, result, midRow, rowEnd, midCol, colEnd, common);

                invokeAll(topLeft, topRight, bottomLeft, bottomRight);
            }
            return null;
        }
    }
}
