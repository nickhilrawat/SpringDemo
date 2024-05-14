package com.example;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ExcelReaderWithoutPoi {

    public static void main(String[] args) {
        String filePath = "src/main/java/com/example/Marriage Contribution.xls"; // Change this to the path of your Excel file

        try {
            Map<String, List<Map<String, Object>>> sheetsData = readExcel(filePath);
            System.out.println(sheetsData); // Print the data for each sheet
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<Map<String, Object>>> readExcel(String filePath) throws Exception {
        Map<String, List<Map<String, Object>>> sheetsData = new HashMap<>();
        Map<Integer, String> sharedStrings = new HashMap<>();

        try (ZipFile zipFile = new ZipFile(filePath)) {
            // Read shared strings
            try (InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("xl/sharedStrings.xml"))) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(inputStream);

                NodeList sharedStringNodes = doc.getElementsByTagName("t");
                for (int i = 0; i < sharedStringNodes.getLength(); i++) {
                    sharedStrings.put(i, sharedStringNodes.item(i).getTextContent());
                }
            } catch (IOException e) {
                // No shared strings file found, continue without shared strings
            }

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith("xl/worksheets/sheet")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(inputStream);

                        NodeList rows = doc.getElementsByTagName("row");
                        List<Map<String, Object>> sheetData = new ArrayList<>();

                        for (int i = 0; i < rows.getLength(); i++) {
                            Node row = rows.item(i);
                            if (row.getNodeType() == Node.ELEMENT_NODE) {
                                NodeList cells = ((Element) row).getElementsByTagName("c");

                                Map<String, Object> rowData = new HashMap<>();
                                for (int j = 0; j < cells.getLength(); j++) {
                                    Element cell = (Element) cells.item(j);
                                    String column = cell.getAttribute("r");
                                    Object value = getValue(cell, sharedStrings);
                                    rowData.put(column, value);
                                }
                                sheetData.add(rowData);
                            }
                        }

                        sheetsData.put(entry.getName(), sheetData);
                    }
                }
            }
        }

        return sheetsData;
    }

    private static Object getValue(Element cell, Map<Integer, String> sharedStrings) {
        NodeList children = cell.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals("v")) {
                String value = child.getTextContent();
                try {
                    int idx = Integer.parseInt(value);
                    return Optional.ofNullable(sharedStrings.get(idx)).orElse(value);
                } catch (NumberFormatException ex) {
                    // Not a shared string index, return as string
                    return value;
                }
            }
        }
        return null; // Empty cell
    }
}
