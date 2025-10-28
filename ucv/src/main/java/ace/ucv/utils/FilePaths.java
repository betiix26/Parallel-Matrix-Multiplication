package ace.ucv.utils;

/**
 * Utility class holding paths for input and output files for all multiplication
 * methods.
 */
public class FilePaths {

	// Base directories
	public static final String INPUT_DIR = "src/main/resources/in/";
	public static final String OUTPUT_DIR = "src/main/resources/out/";

	// Input file
	public static final String MATRIX_DIMENSIONS = INPUT_DIR + "matrix_dimensions.txt";

	// Sequential multiplication
	public static final String SEQUENTIAL_TXT = OUTPUT_DIR + "sequential_result.txt";
	public static final String SEQUENTIAL_XLSX = OUTPUT_DIR + "sequential_metrics.xlsx";

	// Parallel multiplication
	public static final String PARALLEL_TXT = OUTPUT_DIR + "parallel_result.txt";
	public static final String PARALLEL_XLSX = OUTPUT_DIR + "parallel_metrics.xlsx";

	// Strassen multiplication
	public static final String STRASSEN_TXT = OUTPUT_DIR + "strassen_result.txt";
	public static final String STRASSEN_XLSX = OUTPUT_DIR + "strassen_metrics.xlsx";

	// Stream-based parallel multiplication
	public static final String STREAM_TXT = OUTPUT_DIR + "stream_parallel_result.txt";
	public static final String STREAM_XLSX = OUTPUT_DIR + "stream_parallel_metrics.xlsx";

	// Fork/Join multiplication
	public static final String FORK_JOIN_TXT = OUTPUT_DIR + "fork_join_result.txt";
	public static final String FORK_JOIN_XLSX = OUTPUT_DIR + "fork_join_metrics.xlsx";
	
	// Private constructor to prevent instantiation
    private FilePaths() { }
}