package com.churninsight.backend.service;

import com.churninsight.backend.model.PredictionHistory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.mapper.PredictionMapper;
import com.churninsight.backend.repository.PredictionHistoryRepository;

@Service
public class PredictionService {

    private final WebClient webClient;
    private final PredictionHistoryRepository predictionHistoryRepository; // Inyectamos el repo
    private final PredictionMapper predictionMapper; // Inyectamos el Mapper

    // Spring inyecta automáticamente el WebClient que configuramos arriba
    public PredictionService(WebClient aiWebClient, PredictionHistoryRepository predictionHistoryRepository, PredictionMapper predictionMapper) {
        this.webClient = aiWebClient;
        this.predictionHistoryRepository = predictionHistoryRepository;
        this.predictionMapper = predictionMapper;
    }

    /**
     * Solo calcula, NO guarda en BD.
     */
    public ChurnResponseDTO calcularPrediccion(ChurnRequestDTO request) {
        try {
            return webClient.post()
                    .uri("/predict")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChurnResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.err.println("Error llamando a IA: " + e.getResponseBodyAsString());
            throw new RuntimeException("El servicio de IA falló: " + e.getStatusCode());
        } catch (Exception e) {
            System.err.println("Error de conexión con IA: " + e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio de IA", e);
        }
    }

    /**
     *  Calcula Y Guarda.
     */
    public ChurnResponseDTO obtenerPrediccion(ChurnRequestDTO request) {
        // 1. Reutilizamos la lógica de cálculo
        ChurnResponseDTO response = calcularPrediccion(request);

        // 2. Guardamos (Solo para peticiones individuales)
        if (response != null) {
            guardarEnHistorial(request, response);
        }

        return response;
    }

    // Método privado para mapear y guardar
    private void guardarEnHistorial(ChurnRequestDTO req, ChurnResponseDTO res) {
        try {
            if (res == null) return;
            PredictionHistory history = predictionMapper.toEntity(req, res);
            predictionHistoryRepository.save(history);
            System.out.println("📝 Historial individual guardado correctamente.");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo guardar el historial: " + e.getMessage());
        }
    }
}
