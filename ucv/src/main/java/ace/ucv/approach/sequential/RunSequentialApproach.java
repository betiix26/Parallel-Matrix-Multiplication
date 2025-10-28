package ace.ucv.approach.sequential;

import ace.ucv.model.Matrix;
import ace.ucv.service.output.MatrixPrinter;
import ace.ucv.service.output.PerformanceMetricsRecorder;
import ace.ucv.utils.FilePaths;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ace.ucv.utils.FilePaths.SEQUENTIAL_XLSX;

/**
 * Handles the execution of the classic sequential matrix multiplication
 * approach. Responsible for multiplying two matrices, recording performance
 * metrics, and saving both matrices and the result to files.
 */
public class RunSequentialApproach {

	private final SequentialMatrixMultiplication sequentialMultiplication = new SequentialMatrixMultiplication();
	private final MatrixPrinter printer = new MatrixPrinter();
	private final PerformanceMetricsRecorder metricsRecorder;

	/**
	 * Initializes the RunSequentialApproach class, setting up the performance
	 * metrics recorder with the predefined output Excel file path.
	 */
	public RunSequentialApproach() {
		this.metricsRecorder = new PerformanceMetricsRecorder(SEQUENTIAL_XLSX);
	}

	/**
	 * Performs sequential multiplication of two matrices, records execution time,
	 * and writes the input matrices and result to output files.
	 *
	 * @param matrixA first input matrix
	 * @param matrixB second input matrix
	 * @throws IOException if there is an error writing matrices to files
	 */
	public void runSetup(Matrix matrixA, Matrix matrixB) throws IOException {
		// measure execution time of the multiplication
		long startTime = System.nanoTime();
		Matrix result = sequentialMultiplication.multiply(matrixA, matrixB);
		long endTime = System.nanoTime();

		// record execution time in seconds
		metricsRecorder.recordMetric("Timings", "Classic Multiplication Time", (endTime - startTime) / 1_000_000_000.0);

		// define the path to the output text file
		Path filePath = Paths.get(FilePaths.SEQUENTIAL_TXT);

		// write the input matrices and the result to the file
		printer.writeMatrixToFile("Matrix A:", matrixA, filePath);
		printer.writeMatrixToFile("Matrix B:", matrixB, filePath);
		printer.writeMatrixToFile("Classic Multiplication Result:", result, filePath);

		// save performance metrics to Excel file
		metricsRecorder.saveToFile();
	}

}
