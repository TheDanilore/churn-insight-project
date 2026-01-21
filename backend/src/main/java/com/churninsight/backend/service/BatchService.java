package com.churninsight.backend.service;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.model.PredictionHistory;
import com.churninsight.backend.repository.PredictionHistoryRepository;
import com.churninsight.backend.specification.PredictionHistorySpecs;

import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service
public class BatchService {

    private final PredictionHistoryRepository predictionHistoryRepository;

    public BatchService(PredictionHistoryRepository predictionHistoryRepository) {
        this.predictionHistoryRepository = predictionHistoryRepository;
    }

    /**
     * Guarda un lote de predicciones vinculadas a un Job ID
     */
    @Transactional
    public void saveBatchResults(List<ChurnRequestDTO> inputs, List<ChurnResponseDTO> outputs, String jobId) {
        List<PredictionHistory> histories = new ArrayList<>();

        if (inputs.size() != outputs.size())
            return;

        for (int i = 0; i < inputs.size(); i++) {
            ChurnRequestDTO input = inputs.get(i);
            ChurnResponseDTO output = outputs.get(i);

            PredictionHistory history = new PredictionHistory();

            // VINCULACIÓN IMPORTANTE
            history.setJobId(jobId);

            // DATOS CLIENTE
            history.setClientName(input.clientName());
            history.setEmail(input.email());
            history.setPhone(input.phone());

            // DATOS PREDICCIÓN
            history.setAntiguedad(input.antiguedad());
            history.setContrato(input.contrato());
            history.setCargosMensuales(input.cargosMensuales());
            history.setSoporteTecnico(input.soporteTecnico());
            history.setServicioInternet(input.servicioInternet());
            history.setMetodoPago(input.metodoPago());

            // RESULTADOS
            history.setResultado(output.prevision());
            history.setProbabilidad(output.probabilidad());
            history.setAlerta(output.alerta());
            history.setFechaRegistro(java.time.LocalDateTime.now());

            histories.add(history);
        }
        predictionHistoryRepository.saveAll(histories);
    }

    public Page<PredictionHistory> getBatchResultsPaged(String jobId, int page, int size, String search,
            String alerta) {
        // Ordenamos por probabilidad descendente (los más riesgosos primero)
        Pageable pageable = PageRequest.of(page, size, Sort.by("probabilidad").descending());

        // Creamos la especificación
        Specification<PredictionHistory> spec = PredictionHistorySpecs.getResultsByJob(jobId, search, alerta);

        return predictionHistoryRepository.findAll(spec, pageable);
    }

    /**
     * Genera un reporte Excel COMPLETO con todos los datos del lote
     */
    public byte[] generateExcelReport(String jobId) throws IOException {
        List<PredictionHistory> data = predictionHistoryRepository.findByJobId(jobId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Resultados Predicción");

            // 1. ESTILOS DE ENCABEZADO
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 2. DEFINIR TODAS LAS COLUMNAS
            String[] headers = {
                    "Nombre Cliente", // 0
                    "Email", // 1
                    "Teléfono", // 2
                    "Antigüedad (Meses)", // 3
                    "Contrato", // 4
                    "Cargos Mensuales", // 5
                    "Soporte Técnico", // 6
                    "Internet", // 7
                    "Método Pago", // 8
                    "Riesgo", // 9
                    "Probabilidad", // 10
                    "Resultado", // 11
                    "Fecha Registro" // 12
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 3. LLENAR DATOS
            int rowIdx = 1;
            for (PredictionHistory rowData : data) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                // --- DATOS CLIENTE ---
                row.createCell(col++).setCellValue(defaultString(rowData.getClientName()));
                row.createCell(col++).setCellValue(defaultString(rowData.getEmail()));
                row.createCell(col++).setCellValue(defaultString(rowData.getPhone()));

                // --- DATOS DEL SERVICIO ---
                row.createCell(col++).setCellValue(rowData.getAntiguedad() != null ? rowData.getAntiguedad() : 0);
                row.createCell(col++).setCellValue(defaultString(rowData.getContrato()));
                // Formato moneda (double)
                row.createCell(col++)
                        .setCellValue(rowData.getCargosMensuales() != null ? rowData.getCargosMensuales() : 0.0);
                row.createCell(col++).setCellValue(defaultString(rowData.getSoporteTecnico()));
                row.createCell(col++).setCellValue(defaultString(rowData.getServicioInternet()));
                row.createCell(col++).setCellValue(defaultString(rowData.getMetodoPago()));

                // --- PREDICCIÓN IA ---
                row.createCell(col++).setCellValue(defaultString(rowData.getAlerta())); // Riesgo
                // Probabilidad (ej: 0.31)
                row.createCell(col++).setCellValue(rowData.getProbabilidad() != null ? rowData.getProbabilidad() : 0.0);
                row.createCell(col++).setCellValue(defaultString(rowData.getResultado())); // "Se queda" / "Se va"

                // --- METADATA ---
                row.createCell(col++)
                        .setCellValue(rowData.getFechaRegistro() != null ? rowData.getFechaRegistro().toString() : "");
            }

            // 4. AUTO-AJUSTAR ANCHO DE COLUMNAS
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 5. ESCRIBIR
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    // Pequeño helper para evitar escribir "null" en el Excel
    private String defaultString(String value) {
        return value != null ? value : "";
    }
}