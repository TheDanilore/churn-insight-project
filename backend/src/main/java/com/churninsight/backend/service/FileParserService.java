package com.churninsight.backend.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

@Service
public class FileParserService {
    private static final Logger logger = LoggerFactory.getLogger(FileParserService.class);

    /**
     * Parsea un archivo (CSV o Excel) y devuelve una lista de filas
     * Cada fila es un array de Strings
     */
    public List<String[]> parseFile(byte[] fileContent, String fileName) throws Exception {
        List<String[]> rows = new ArrayList<>();
        
        String lowerFileName = fileName.toLowerCase();
        
        if (lowerFileName.endsWith(".csv")) {
            rows = parseCsv(fileContent);
        } else if (lowerFileName.endsWith(".xlsx")) {
            rows = parseExcel(fileContent, true);
        } else if (lowerFileName.endsWith(".xls")) {
            rows = parseExcel(fileContent, false);
        } else {
            throw new IllegalArgumentException("Formato de archivo no soportado. Use .csv, .xlsx o .xls");
        }
        
        logger.info("📄 Archivo parseado: {} - {} filas (incluyendo encabezado)", fileName, rows.size());
        return rows;
    }

    /**
     * Parsea un archivo CSV
     */
    private List<String[]> parseCsv(byte[] fileContent) throws IOException {
        List<String[]> rows = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(fileContent)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split por coma, pero respeta espacios en valores
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // Limpiar comillas si existen
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                    if (values[i].startsWith("\"") && values[i].endsWith("\"")) {
                        values[i] = values[i].substring(1, values[i].length() - 1);
                    }
                }
                rows.add(values);
            }
        }
        
        return rows;
    }

    /**
     * Parsea un archivo Excel (.xlsx o .xls)
     * @param isXlsx true para .xlsx, false para .xls
     */
    private List<String[]> parseExcel(byte[] fileContent, boolean isXlsx) throws IOException {
        List<String[]> rows = new ArrayList<>();
        
        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            Workbook workbook = isXlsx 
                ? new XSSFWorkbook(inputStream)
                : new HSSFWorkbook(inputStream);
            
            Sheet sheet = workbook.getSheetAt(0); // Primera hoja
            
            for (Row row : sheet) {
                List<String> cellValues = new ArrayList<>();
                
                // Obtener el número máximo de columnas
                int lastCellNum = row.getLastCellNum();
                
                for (int i = 0; i < lastCellNum; i++) {
                    Cell cell = row.getCell(i);
                    cellValues.add(getCellValueAsString(cell));
                }
                
                rows.add(cellValues.toArray(new String[0]));
            }
            
            workbook.close();
        }
        
        return rows;
    }

    /**
     * Extrae el valor de una celda Excel como String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Si es fecha, formatea como número
                if (DateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
                return String.valueOf((long) cell.getNumericCellValue());
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
