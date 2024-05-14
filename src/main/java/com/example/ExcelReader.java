package com.example;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    public static void main(String[] args) {
        String filePath = "src/main/java/com/example/Marriage Contribution2.xls"; // Change this to the path of your Excel file

        try {
            Map<String, List<Map<String, String>>> sheetsData = readExcel(filePath);
            System.out.println(sheetsData); // Print the data for each sheet
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<Map<String, String>>> readExcel(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Map<String, List<Map<String, String>>> sheetsData = new HashMap<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            List<Map<String, String>> sheetData = new ArrayList<>();

            for (Row row : sheet) {
                Map<String, String> rowData = new HashMap<>();
                for (Cell cell : row) {
                    rowData.put(String.valueOf(cell.getColumnIndex()), getCellValueAsString(cell));
                }
                sheetData.add(rowData);
            }

            sheetsData.put(sheet.getSheetName(), sheetData);
        }

        workbook.close();
        fileInputStream.close();

        return sheetsData;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}

