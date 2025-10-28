package ace.ucv.service.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class PerformanceMetricsRecorder {
    private static final Logger logger = LogManager.getLogger(PerformanceMetricsRecorder.class);
    private XSSFWorkbook workbook;
    private String filePath;

    public PerformanceMetricsRecorder(String filePath) {
        this.filePath = filePath;
        this.workbook = new XSSFWorkbook();
    }

    public void recordMetric(String sheetName, String metricDescription, double value) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }

        int lastRow = sheet.getLastRowNum();
        Row row = sheet.createRow(lastRow + 1);
        Cell cellDesc = row.createCell(0);
        cellDesc.setCellValue(metricDescription);

        Cell cellValue = row.createCell(1);
        cellValue.setCellValue(value);
    }

    public void saveToFile() {
        try (FileOutputStream out = new FileOutputStream(this.filePath)) {
            workbook.write(out);
            logger.info("Metrics written to Excel file successfully.");
        } catch (IOException e) {
            logger.error("Failed to write metrics to Excel file", e);
        }
    }
    public void saveToFile(String path) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(path)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

}