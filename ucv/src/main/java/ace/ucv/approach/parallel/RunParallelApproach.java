package ace.ucv.approach.parallel;

import ace.ucv.model.Matrix;
import ace.ucv.service.output.MatrixPrinter;
import ace.ucv.service.output.PerformanceMetricsRecorder;
import ace.ucv.utils.FilePaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static ace.ucv.utils.FilePaths.PARALLEL_XLSX;

public class RunParallelApproach {

    private final MatrixPrinter printer = new MatrixPrinter();
    private final PerformanceMetricsRecorder metricsRecorder;
    private final ParallelMatrixMultiplication parallelMultiplication = new ParallelMatrixMultiplication();

    public RunParallelApproach() {
        // initialize the metrics recorder with a path to save the excel file
        this.metricsRecorder = new PerformanceMetricsRecorder(PARALLEL_XLSX);
    }

    public void runSetup(Matrix matrixA, Matrix matrixB) throws IOException {
        long startTime = System.nanoTime();

        // perform parallel multiplication
        Matrix result;
        try {
            result = parallelMultiplication.multiply(matrixA, matrixB);
        } catch (InterruptedException e) {
            throw new RuntimeException("Parallel matrix multiplication was interrupted", e);
        }

        // write to file
        final Path filePath = Paths.get(FilePaths.PARALLEL_TXT);
        printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
        printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
        printer.writeMatrixToFile("Parallel Multiplication Result:", result, filePath);

        String detailedLog = parallelMultiplication.getLog();
        if (!detailedLog.isEmpty()) {
            Files.writeString(filePath, "\nComputation Steps:\n" + detailedLog + "\n", StandardOpenOption.APPEND);
        }

        metricsRecorder.saveToFile();
    }
}
