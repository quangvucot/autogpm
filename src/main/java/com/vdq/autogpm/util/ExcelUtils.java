package com.vdq.autogpm.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelUtils {
    private String filePath;

    public ExcelUtils(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Map<String, String>> readExcel() {
        Map<String, Map<String, String>> data = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            // Bỏ qua hàng tiêu đề
            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Cell keyCell = currentRow.getCell(0);

                if (keyCell != null) {
                    String key = getCellValueAsString(keyCell);
                    Map<String, String> rowData = new HashMap<>();

                    for (Cell cell : currentRow) {
                        String column = getColumnName(cell.getColumnIndex());
                        String value = getCellValueAsString(cell);
                        rowData.put(column, value);
                    }
                    data.put(key, rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String getColumnName(int index) {
        return String.valueOf((char) ('A' + index));
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public String getValue(String key, String column, Map<String, Map<String, String>> data) {
        if (data.containsKey(key)) {
            Map<String, String> rowData = data.get(key);
            if (rowData.containsKey(column)) {
                return rowData.get(column);
            }
        }
        return null;
    }

    public void writeExcel(Map<String, Map<String, String>> data) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet();

            int rowCount = 0;
            Row headerRow = sheet.createRow(rowCount++);
            int headerColumnCount = 0;

            // Write headers
            for (String header : data.values().iterator().next().keySet()) {
                Cell cell = headerRow.createCell(headerColumnCount++);
                cell.setCellValue(header);
            }

            // Write data
            for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                Cell keyCell = row.createCell(columnCount++);
                keyCell.setCellValue(entry.getKey());

                for (String value : entry.getValue().values()) {
                    Cell cell = row.createCell(columnCount++);
                    cell.setCellValue(value);
                }
            }
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
