//package com.example.demo;
//
//import org.apache.poi.ss.usermodel.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ExcelUtility {
//    public static void main(String[] args) {
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Map<String, List<Map<String, String>>> sheetsData = new HashMap<>();
//
//        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//            Sheet sheet = workbook.getSheetAt(i);
//            List<Map<String, String>> sheetData = new ArrayList<>();
//
//            for (Row row : sheet) {
//                Map<String, String> rowData = new HashMap<>();
//                for (Cell cell : row) {
//                    rowData.put(String.valueOf(cell.getColumnIndex()), getCellValueAsString(cell));
//                }
//                sheetData.add(rowData);
//            }
//
//            sheetsData.put(sheet.getSheetName(), sheetData);
//        }
//
//        System.out.println(sheetsData);
//    }
//
//    private String getCellValueAsString(Cell cell) {
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                } else {
//                    return String.valueOf(cell.getNumericCellValue());
//                }
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            case BLANK:
//                return "";
//            default:
//                return "";
//        }
//    }
//}
