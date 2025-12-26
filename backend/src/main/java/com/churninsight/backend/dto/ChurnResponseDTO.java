package com.churninsight.backend.dto;

public record ChurnResponseDTO(
    String prevision,     // Ej: "Va a cancelar"
    Double probabilidad,  // Ej: 0.85
    String alerta         // Ej: "ALTA" (Para que el Frontend sepa si pintar Rojo o Verde)
) {}
