package com.churninsight.backend.service;

import com.churninsight.backend.model.PredictionHistory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.mapper.PredictionMapper;
import com.churninsight.backend.repository.PredictionRepository;

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
        try {
            // Llamamos a la IA y guardamos la respuesta en una VARIABLE
            ChurnResponseDTO response = webClient.post()
                    .uri("/predict")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChurnResponseDTO.class)
                    .block(); // Esperamos la respuesta

            // Ahora sí podemos guardar (porque ya tenemos la respuesta 'response')
            guardarEnHistorial(request, response);

            // Finalmente devolvemos la respuesta al usuario
            return response;

        } catch (WebClientResponseException e) {
            // Si Python responde con error (4xx, 5xx), lo capturamos aquí
            System.err.println("Error llamando a IA: " + e.getResponseBodyAsString());
            throw new RuntimeException("El servicio de IA falló: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de IA");
        }
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
