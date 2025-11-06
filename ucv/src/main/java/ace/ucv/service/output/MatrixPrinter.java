package ace.ucv.service.output;

import ace.ucv.model.Matrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class MatrixPrinter {
    private static final Logger logger = LogManager.getLogger(MatrixPrinter.class);

    public void writeMatrixToFile(String label, Matrix matrix, Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("==== " + label + " ====");
        lines.add("Size: " + matrix.getRows() + " x " + matrix.getCols());
        for (int i = 0; i < matrix.getRows(); i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < matrix.getCols(); j++) {
                line.append(matrix.getData()[i][j]).append("\t");
            }
            lines.add(line.toString());
        }
        lines.add(""); // newline separator

        try {
            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing matrix to file: {}", filePath, e);
            throw e;
        }
    }

    public void writeComputationLog(Matrix A, Matrix B, Matrix result, String methodName, Path outputPath, String detailedLog) {
        try {
            if (Files.exists(outputPath)) {
                Files.delete(outputPath);
            }
            writeMatrixToFile("Matrix A (" + methodName + ")", A, outputPath);
            writeMatrixToFile("Matrix B (" + methodName + ")", B, outputPath);
            writeMatrixToFile("Result (" + methodName + ")", result, outputPath);

            if (detailedLog != null && !detailedLog.isEmpty()) {
                Files.writeString(outputPath, "\nComputation Steps:\n" + detailedLog + "\n", StandardOpenOption.APPEND);
            }

            Files.writeString(outputPath,
                    "Verification: A * B = Result computed using " + methodName + "\n\n",
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            logger.error("Failed to write computation log to file", e);
        }
    }

}
