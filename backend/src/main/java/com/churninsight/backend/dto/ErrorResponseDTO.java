package com.churninsight.backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErrorResponseDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // <--- Formato de fecha legible
        LocalDateTime timestamp,
        
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}
