package com.churninsight.backend.service;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.model.PredictionHistory;
import com.churninsight.backend.repository.PredictionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService {

    private final PredictionRepository repository;

    public BatchService(PredictionRepository predictionRepository) {
        this.repository = predictionRepository;
    }

    @Transactional
    protected List<PredictionHistory> batchInsert(List<ChurnRequestDTO> inputs, List<ChurnResponseDTO> outputs) {
        List<PredictionHistory> histories = new ArrayList<>();

        if (inputs.size() == outputs.size()) {
            for (int i = 0; i < inputs.size(); i++) {
                ChurnRequestDTO input = inputs.get(i);
                ChurnResponseDTO output = outputs.get(i);

                PredictionHistory history = new PredictionHistory();
                // Mapeo de la entrada (Request)
                history.setAntiguedad(input.antiguedad());
                history.setContrato(input.contrato());
                history.setCargosMensuales(input.cargosMensuales());
                history.setSoporteTecnico(input.soporteTecnico());
                history.setServicioInternet(input.servicioInternet());
                history.setMetodoPago(input.metodoPago());

                // Mapeo de la salida (Response de la IA)
                history.setResultado(output.prevision());
                history.setProbabilidad(output.probabilidad());
                history.setAlerta(output.alerta());

                histories.add(history);
            }
        } else {
            throw new IllegalArgumentException("El tamaño de las entradas y salidas no coincide para el batch insert.");
        }
        return repository.saveAll(histories);
    }
}
