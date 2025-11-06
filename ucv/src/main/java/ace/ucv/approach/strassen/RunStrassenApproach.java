package ace.ucv.approach.strassen;

import ace.ucv.model.Matrix;
import ace.ucv.service.output.MatrixPrinter;
import ace.ucv.service.output.PerformanceMetricsRecorder;
import ace.ucv.utils.FilePaths;
import ace.ucv.utils.MatrixUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static ace.ucv.utils.FilePaths.STRASSEN_XLSX;
import static ace.ucv.utils.MatrixUtility.padMatrix;

public class RunStrassenApproach {

    private final MatrixPrinter printer = new MatrixPrinter();
    private final PerformanceMetricsRecorder metricsRecorder;
    private final StrassenMatrixMultiplication strassenMultiplication = new StrassenMatrixMultiplication();

    public RunStrassenApproach() {
        // initialize the metrics recorder with a path to save the Excel file
        this.metricsRecorder = new PerformanceMetricsRecorder(STRASSEN_XLSX);
    }

    public void runSetup(Matrix matrixA, Matrix matrixB) throws IOException {
        if (matrixA.getCols() != matrixB.getRows()) {
            throw new IllegalArgumentException("The number of columns in the first matrix must be equal to the number of rows in the second.");
        }

        int originalRowsA = matrixA.getRows();
        int originalColsB = matrixB.getCols();

        Matrix paddedA = padMatrix(matrixA);
        Matrix paddedB = padMatrix(matrixB);

        long startTime = System.nanoTime();

        Matrix paddedResult = strassenMultiplication.multiply(paddedA, paddedB);
        long endTime = System.nanoTime();
        metricsRecorder.recordMetric("Timings", "Strassen Multiplication Time", (endTime - startTime) / 1_000_000_000.0);

//        Matrix result = MatrixUtility.cropMatrix(paddedResult, originalRowsA, originalColsB);
//
//        final Path filePath = Paths.get(FilePaths.STRASSEN_TXT);
//        printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
//        printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
//        printer.writeMatrixToFile("Strassen Multiplication Result:", result, filePath);
//
//        metricsRecorder.saveToFile();
        Matrix result = strassenMultiplication.multiply(matrixA, matrixB);

        Path filePath = Paths.get(FilePaths.STRASSEN_TXT);
        printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
        printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
        printer.writeMatrixToFile("Strassen Multiplication Result:", result, filePath);

        Files.writeString(filePath, "\nStrassen Computation Steps:\n" + strassenMultiplication.getLog(), StandardOpenOption.APPEND);

    }
}
