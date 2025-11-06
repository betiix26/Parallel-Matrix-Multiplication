package ace.ucv.approach.stream;

import ace.ucv.model.Matrix;
import ace.ucv.service.output.MatrixPrinter;
import ace.ucv.service.output.PerformanceMetricsRecorder;
import ace.ucv.utils.FilePaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static ace.ucv.utils.FilePaths.STREAM_XLSX;

public class RunStreamParallelApproach {

    private final StreamParallelMultiplication streamMultiplication = new StreamParallelMultiplication();
    private final MatrixPrinter printer = new MatrixPrinter();
    private final PerformanceMetricsRecorder metricsRecorder;

    public RunStreamParallelApproach() {
        this.metricsRecorder = new PerformanceMetricsRecorder(STREAM_XLSX);
    }

    public void runSetup(Matrix matrixA, Matrix matrixB) throws IOException {
        long startTime = System.nanoTime();
//        Matrix result = streamMultiplication.multiply(matrixA, matrixB);
//        long endTime = System.nanoTime();
//        metricsRecorder.recordMetric("Timings", "Stream Parallel Multiplication Time", (endTime - startTime) / 1_000_000_000.0);
//
//        Path filePath = Paths.get(FilePaths.STREAM_TXT);
//        printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
//        printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
//        printer.writeMatrixToFile("Stream Parallel Multiplication Result:", result, filePath);
//
//        metricsRecorder.saveToFile();
        Matrix result = streamMultiplication.multiply(matrixA, matrixB);

        Path filePath = Paths.get(FilePaths.STREAM_TXT);
        printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
        printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
        printer.writeMatrixToFile("Stream Parallel Multiplication Result:", result, filePath);

        String detailedLog = streamMultiplication.getLog();
        if (!detailedLog.isEmpty()) {
            Files.writeString(filePath, "\nComputation Steps:\n" + detailedLog + "\n", StandardOpenOption.APPEND);
        }

        metricsRecorder.saveToFile();

    }
}
