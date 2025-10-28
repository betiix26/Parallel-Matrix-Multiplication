package ace.ucv.service.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser class responsible for reading matrix dimensions from a text file.
 * Expects each line in the file to be in the format: key=value
 * Logs errors for malformed lines or invalid number formats.
 */
public class MatrixParser {

    private static final Logger logger = LogManager.getLogger(MatrixParser.class);
    
    /**
     * Reads matrix dimensions from a specified file.
     * Each line should have the format: key=value
     * 
     * @param filePath path to the input file
     * @return a map containing key-value pairs of matrix dimensions
     * @throws IOException if the file cannot be read
     */
    public Map<String, Integer> readMatrixDimensionsFromFile(String filePath) throws IOException {
        Map<String, Integer> dimensions = new HashMap<>();

        try {
        	// Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                String[] parts = line.split("=");

                if (parts.length == 2) {
                    String key = parts[0].trim();

                    try {
                        int value = Integer.parseInt(parts[1].trim());
                        dimensions.put(key, value);
                    } catch (NumberFormatException e) {
                    	// Log if the value is not a valid integer
                        logger.error(String.format("Invalid number format for key: %s in line: %s", key, line));
                    }
                } else {
                	// Log if the line does not match the expected format
                   logger.error(String.format("Invalid line format (expected key=value): %s", line));
                }
            }
        } catch (IOException e) {
        	// Log and rethrow the exception if the file could not be read
            logger.error(String.format("Error reading file at path: %s", filePath));
            throw e; 
        }

        return dimensions;
    }
}
