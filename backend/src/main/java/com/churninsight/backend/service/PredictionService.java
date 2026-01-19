package com.churninsight.backend.service;

import com.churninsight.backend.model.PredictionHistory;
import com.churninsight.backend.dto.BatchPredictionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.scheduling.annotation.Async;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.mapper.PredictionMapper;
import com.churninsight.backend.repository.PredictionRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class PredictionService {

    private final WebClient webClient;
    private final PredictionRepository predictionRepository; // Inyectamos el repo
    private final PredictionMapper predictionMapper; // Inyectamos el Mapper

    // Spring inyecta automáticamente el WebClient que configuramos arriba
    public PredictionService(WebClient aiWebClient, PredictionRepository predictionRepository, PredictionMapper predictionMapper) {
        this.webClient = aiWebClient;
        this.predictionRepository = predictionRepository;
        this.predictionMapper = predictionMapper;
    }

    public ChurnResponseDTO obtenerPrediccion(ChurnRequestDTO request) {
        ChurnResponseDTO response;
        try {
            // 1. Intentamos llamar a la IA
            response = webClient.post()
                    .uri("/predict")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChurnResponseDTO.class)
                    .block(); // Esperamos la respuesta
        } catch (WebClientResponseException e) {
            System.err.println("Error llamando a IA: " + e.getResponseBodyAsString());
            throw new RuntimeException("El servicio de IA falló: " + e.getStatusCode());
        } catch (Exception e) {
            // Logueamos el error real para depurar
            System.err.println("Error de conexión con IA: " + e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio de IA", e);
        }

        // 2. Intentar guardar (Fuera del catch principal para que no mate la respuesta)
        if (response != null) {
            guardarEnHistorial(request, response);
        }

        // Finalmente devolvemos la respuesta al usuario
        return response;

    }

    // Método privado para mapear y guardar en la base de datos,
    // para que un fallo en la DB no afecte la respuesta al usuario
    private void guardarEnHistorial(ChurnRequestDTO req, ChurnResponseDTO res) {
        try {
            if (res == null)
                return; // Validación extra por seguridad

            // MAGIA DE MAPSTRUCT
            // Reemplaza las 10 líneas de setters por esta sola línea:
            PredictionHistory history = predictionMapper.toEntity(req, res);

            predictionRepository.save(history);
            System.out.println("📝 Historial guardado correctamente.");

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo guardar el historial: " + e.getMessage());
        }
    }
}
