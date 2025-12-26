package com.churninsight.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;

@Service
public class PredictionService {

    private final WebClient webClient;

    // Spring inyecta automáticamente el WebClient que configuramos arriba
    public PredictionService(WebClient aiWebClient) {
        this.webClient = aiWebClient;
    }

    public ChurnResponseDTO obtenerPrediccion(ChurnRequestDTO request) {
        try {
            // 1. Iniciamos la petición POST
            return webClient.post()
                    .uri("/predict") // Se concatena a la URL base (http://ai-service:8000)
                    .bodyValue(request) // Spring convierte el DTO a JSON automáticamente
                    .retrieve()
                    
                    // 2. Convertimos la respuesta JSON de Python a nuestro DTO Java
                    .bodyToMono(ChurnResponseDTO.class)
                    
                    // 3. .block() hace que esperemos la respuesta (Síncrono)
                    // Para un MVP esto está perfecto. En sistemas masivos se usa reactivo completo.
                    .block();

        } catch (WebClientResponseException e) {
            // Si Python responde con error (4xx, 5xx), lo capturamos aquí
            System.err.println("Error llamando a IA: " + e.getResponseBodyAsString());
            throw new RuntimeException("El servicio de IA falló: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de IA");
        }
    }
}
