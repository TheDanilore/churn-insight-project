package com.churninsight.service;

import com.churninsight.dto.ClientDataRequest;
import com.churninsight.dto.PredictionResponse;
import com.churninsight.entity.Prediction;
import com.churninsight.repository.PredictionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PredictionService {
    
    @Autowired
    private PredictionRepository predictionRepository;
    
    @Autowired
    private WebClient webClient;
    
    @Value("${fastapi.url:http://localhost:8000}")
    private String fastApiUrl;
    
    public PredictionResponse predict(ClientDataRequest request) {
        try {
            log.info("Iniciando predicción para cliente: {}", request.getCustomerId());
            
            // Llamar a FastAPI
            PredictionResponse response = webClient.post()
                    .uri(fastApiUrl + "/predict")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PredictionResponse.class)
                    .block();
            
            // Guardar predicción en BD
            if (response != null) {
                Prediction prediction = Prediction.builder()
                        .customerId(request.getCustomerId())
                        .prediction(response.getPrediction())
                        .probability(response.getProbability())
                        .confidence(response.getConfidence())
                        .topFeatures(String.join(",", response.getTopFeatures()))
                        .build();
                
                predictionRepository.save(prediction);
                log.info("Predicción guardada para cliente: {}", request.getCustomerId());
            }
            
            return response;
        } catch (Exception e) {
            log.error("Error en la predicción: ", e);
            throw new RuntimeException("Error al conectar con el servicio de predicción", e);
        }
    }
    
    public Object getStats() {
        long total = predictionRepository.count();
        long churnCount = predictionRepository.countByPrediction("Va a cancelar");
        
        return new Object() {
            public final int total_evaluados = (int) total;
            public final double tasa_churn = total > 0 ? (double) churnCount / total : 0;
            public final double accuracy = 0.82;
            public final double precision = 0.79;
            public final double recall = 0.85;
            public final double f1_score = 0.82;
        };
    }
}
