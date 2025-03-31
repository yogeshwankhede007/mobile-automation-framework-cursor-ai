package com.mobileautomation.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestDataProvider {
    private static final Logger logger = LogManager.getLogger(TestDataProvider.class);

    public static Object[][] getTestDataFromExcel(String sheetName) {
        String excelPath = "src/test/resources/testdata/testdata.xlsx";
        List<Object[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(excelPath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IOException("Sheet not found: " + sheetName);
            }

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                Object[] rowData = new Object[colCount];

                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = getCellValue(cell);
                }

                data.add(rowData);
            }

            logger.info("Successfully read {} rows from Excel sheet: {}", data.size(), sheetName);
            return data.toArray(new Object[0][]);
        } catch (IOException e) {
            logger.error("Failed to read Excel file: {}", e.getMessage());
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    public static Object[][] getTestDataFromCSV(String csvFile) {
        String csvPath = "src/test/resources/testdata/" + csvFile;
        List<Object[]> data = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> rows = reader.readAll();
            
            // Skip header row
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                data.add(row);
            }

            logger.info("Successfully read {} rows from CSV file: {}", data.size(), csvFile);
            return data.toArray(new Object[0][]);
        } catch (IOException | CsvException e) {
            logger.error("Failed to read CSV file: {}", e.getMessage());
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                try {
                    return cell.getNumericCellValue();
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            default:
                return null;
        }
    }
} 