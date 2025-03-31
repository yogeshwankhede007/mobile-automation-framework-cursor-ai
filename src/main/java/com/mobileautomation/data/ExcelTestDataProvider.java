package com.mobileautomation.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelTestDataProvider implements TestDataProvider {
    private static final Logger logger = LogManager.getLogger(ExcelTestDataProvider.class);
    private static final String EXCEL_PATH = "src/test/resources/testdata/testdata.xlsx";
    private final Map<String, Map<String, String>> testDataCache;
    private final Map<String, Integer> columnIndices;

    public ExcelTestDataProvider() {
        this.testDataCache = new ConcurrentHashMap<>();
        this.columnIndices = new HashMap<>();
        loadTestData();
    }

    @Override
    public Map<String, String> getTestData(String testCaseId) {
        Map<String, String> data = testDataCache.get(testCaseId);
        if (data == null) {
            throw new IllegalArgumentException("No test data found for test case ID: " + testCaseId);
        }
        return new HashMap<>(data);
    }

    @Override
    public void loadTestData() {
        try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet("TestData");
            if (sheet == null) {
                throw new IllegalStateException("TestData sheet not found in Excel file");
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalStateException("Header row not found in Excel file");
            }

            initializeColumnIndices(headerRow);
            loadDataRows(sheet);

            logger.info("Loaded {} test data sets from Excel", testDataCache.size());
        } catch (IOException e) {
            logger.error("Failed to load test data from Excel", e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    private void initializeColumnIndices(Row headerRow) {
        columnIndices.clear();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String header = getCellValueAsString(headerRow.getCell(i));
            if (header.equalsIgnoreCase("TestCaseId")) {
                columnIndices.put("TestCaseId", i);
            } else {
                columnIndices.put(header, i);
            }
        }

        if (!columnIndices.containsKey("TestCaseId")) {
            throw new IllegalStateException("TestCaseId column not found in Excel file");
        }
    }

    private void loadDataRows(Sheet sheet) {
        int testCaseIdCol = columnIndices.get("TestCaseId");
        testDataCache.clear();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String testCaseId = getCellValueAsString(row.getCell(testCaseIdCol));
            if (testCaseId.isEmpty()) continue;

            Map<String, String> rowData = new HashMap<>();
            for (Map.Entry<String, Integer> entry : columnIndices.entrySet()) {
                if (!entry.getKey().equals("TestCaseId")) {
                    String key = entry.getKey();
                    int colIndex = entry.getValue();
                    String value = getCellValueAsString(row.getCell(colIndex));
                    rowData.put(key, value);
                }
            }

            testDataCache.put(testCaseId, rowData);
        }
    }

    @Override
    public void refreshTestData() {
        loadTestData();
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    }
                    return String.valueOf(cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception e) {
                        return cell.getStringCellValue();
                    }
                default:
                    return "";
            }
        } catch (Exception e) {
            logger.warn("Error reading cell value: {}", e.getMessage());
            return "";
        }
    }
} 