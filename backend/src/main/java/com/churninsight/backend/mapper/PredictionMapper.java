package com.churninsight.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.model.PredictionHistory;

// componentModel = "spring" permite inyectarlo con @Autowired o constructor
@Mapper(componentModel = "spring")
public interface PredictionMapper {

    // Mapeamos Req + Res -> Entity
    
    // Mapeos explícitos (Solo si los nombres no coinciden)
    // Como en el DTO es 'prevision' y en Entity es 'resultado', hay que decirlo:
    @Mapping(source = "res.prevision", target = "resultado")
    
    // Los demás campos (antiguedad, contrato, etc.) se mapean SOLOS porque se llaman igual.
    PredictionHistory toEntity(ChurnRequestDTO req, ChurnResponseDTO res);
}