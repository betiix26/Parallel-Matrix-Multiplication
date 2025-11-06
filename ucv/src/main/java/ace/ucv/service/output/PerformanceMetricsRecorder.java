package ace.ucv.service.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class PerformanceMetricsRecorder {
    private static final Logger logger = LogManager.getLogger(PerformanceMetricsRecorder.class);
    private final XSSFWorkbook workbook;
    private final String filePath;

    public PerformanceMetricsRecorder(String filePath) {
        this.filePath = filePath;
        this.workbook = new XSSFWorkbook();
    }

    public void recordMetric(String sheetName, String metricDescription, double value) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            createHeader(sheet);
        }

        int lastRow = sheet.getLastRowNum();
        if (lastRow == 0 && sheet.getRow(0) == null) lastRow = 0;

        Row row = sheet.createRow(lastRow + 1);
        Cell cellIndex = row.createCell(0);
        cellIndex.setCellValue(lastRow);

        Cell cellDesc = row.createCell(1);
        cellDesc.setCellValue(metricDescription);

        Cell cellValue = row.createCell(2);
        cellValue.setCellValue(value);
    }

    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        String[] titles = {"#", "Description", "Execution Time (ms)"};

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        for (int i = 0; i < titles.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 6000);
        }
    }

    public void saveToFile() {
        try (FileOutputStream out = new FileOutputStream(this.filePath)) {
            workbook.write(out);
            workbook.close();
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
