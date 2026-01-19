package com.churninsight.backend.dto;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponseDTO(

        @JsonProperty("timestamp")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // <--- Formato de fecha legible
        LocalDateTime timestamp, //Marca de tiempo del error.

        @JsonProperty("status")
        int status,//Código de estado HTTP.

        @JsonProperty("error")
        String error,//Título corto y legible del error

        @JsonProperty("message")
        String message, //Descripción detallada del error.

        @JsonProperty("path")
        String path, //Ruta de la solicitud que causó el error.

        @JsonProperty("method")
        String method,// Método HTTP de la solicitud.

        @JsonProperty("errors")
        Map<String, String> validationErrors
)

{
        public ErrorResponseDTO {
                if (timestamp == null) timestamp = LocalDateTime.now();
                if (error == null || error.isBlank()) error = "Error";
                if (message == null) message = "No message available";
        }

        // Static helper para errores comunes
        public static ErrorResponseDTO of(int status, String error, String message, String path, String method, Map<String, String> errors) {
                return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, path, method, errors);
        }
}
