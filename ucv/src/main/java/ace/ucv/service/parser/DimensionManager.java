package ace.ucv.service.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static ace.ucv.utils.FilePaths.MATRIX_DIMENSIONS;

/**
 * DimensionManager is responsible for providing matrix dimensions by reading
 * them from a predefined file. Uses MatrixParser to parse the file content and
 * returns a map of key-value pairs representing matrix dimensions. Handles
 * logging for success and failure cases.
 */
public class DimensionManager {
	private static final Logger logger = LogManager.getLogger(DimensionManager.class);

	// Parser instance used to read and parse the dimension file
	private MatrixParser parser = new MatrixParser();

	/**
	 * Retrieves matrix dimensions from the file specified in
	 * Constants.DIMENSIONS_FILE
	 * 
	 * @return a map of dimension names to integer values
	 * @throws IOException if reading the file fails
	 */
	public Map<String, Integer> getDimensions() throws IOException {
		try {
			Map<String, Integer> dimensions = parser.readMatrixDimensionsFromFile(MATRIX_DIMENSIONS);
			logger.info("Dimensions read successfully from file.");
			return dimensions;
		} catch (IOException e) {
			// Log detailed error if the file cannot be read
			logger.error(new StringBuilder().append("Error: Could not read dimensions from file ")
					.append(MATRIX_DIMENSIONS).toString(), e);
			throw e;
		}
	}
}
