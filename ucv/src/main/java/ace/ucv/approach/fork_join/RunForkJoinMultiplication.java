package ace.ucv.approach.fork_join;

import ace.ucv.model.Matrix;
import ace.ucv.service.output.MatrixPrinter;
import ace.ucv.service.output.PerformanceMetricsRecorder;
import ace.ucv.utils.FilePaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RunForkJoinMultiplication {

	private final ForkJoinStreamMatrixMultiplication forkJoinMultiplication = new ForkJoinStreamMatrixMultiplication();

	private final MatrixPrinter printer = new MatrixPrinter();
	private final PerformanceMetricsRecorder metricsRecorder;

	public RunForkJoinMultiplication() {
		this.metricsRecorder = new PerformanceMetricsRecorder(FilePaths.FORK_JOIN_XLSX);
	}

	public Matrix multiply(Matrix matrixA, Matrix matrixB) throws IOException {
		long startTime = System.nanoTime();

		Matrix result = ForkJoinStreamMatrixMultiplication.multiply(matrixA, matrixB);

		long endTime = System.nanoTime();
		metricsRecorder.recordMetric("Timings", "Fork-Join Multiplication Time",
				(endTime - startTime) / 1_000_000_000.0);

		Path filePath = Paths.get(FilePaths.FORK_JOIN_TXT);
		printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
		printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
		printer.writeMatrixToFile("Fork-Join Multiplication Result:", result, filePath);

		String detailedLog = ForkJoinStreamMatrixMultiplication.getLog();
		if (!detailedLog.isEmpty()) {
			Files.writeString(filePath, "\nComputation Steps:\n" + detailedLog + "\n", StandardOpenOption.APPEND);
		}

		metricsRecorder.saveToFile();

		return result;
	}

	public String getLog() {
		return ForkJoinStreamMatrixMultiplication.getLog();
	}
}
