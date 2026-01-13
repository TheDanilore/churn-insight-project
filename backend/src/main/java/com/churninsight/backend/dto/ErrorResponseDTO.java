package com.churninsight.backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

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
                // Validaciones básicas
                if (error == null || error.isBlank()) {
                        title = "Error";
                }

                if (status == null) {
                        status = 500; // Internal Server Error por defecto
                }

                if (timestamp == null) {
                        timestamp = LocalDateTime.now();
                }

                // Si mensaje es null pero tenemos errors, crear un detail genérico
                if (message == null && errors != null && !errors.isEmpty()) {
                        message = "Error de validación en " + errors.size() + " campo(s)";
                }
        }

        /**
         * Error de validación (422 Unprocessable Entity).
         */
        public static ErrorResponseDTO validationError(
                String message,
                List<ValidationError> errors,
                String path,
                String method) {

                return new ErrorResponseDTO(
                        "https://api.midominio.com/errors/validation-error",
                        "Error de Validación",
                        422,
                        message,
                        null, // instance
                        LocalDateTime.now(),
                        path,
                        method,
                        errors
                );
        }
        /**
         * Error de recurso no encontrado (404 Not Found).
         */
        public static ErrorResponseDTO notFound(
                String resourceName,
                String resourceId,
                String path,
                String method) {

                return new ErrorResponseDTO(
                        "https://api.midominio.com/errors/not-found",
                        "Recurso No Encontrado",
                        404,
                        String.format("%s con ID '%s' no encontrado", resourceName, resourceId),
                        null,
                        LocalDateTime.now(),
                        path,
                        method,
                        null // errors
                );
        }

        /**
         * Error de solicitud inválida (400 Bad Request).
         */
        public static ErrorResponseDTO badRequest(
                String detail,
                String path,
                String method) {

                return new ErrorResponseDTO(
                        "https://api.midominio.com/errors/bad-request",
                        "Solicitud Inválida",
                        400,
                        detail,
                        null,
                        LocalDateTime.now(),
                        path,
                        method,
                        null
                );
        }

        /**
         * Error de autenticación (401 Unauthorized).
         */
        public static ErrorResponseDTO unauthorized(
                String detail,
                String path,
                String method) {

                return new ErrorResponseDTO(
                        "https://api.midominio.com/errors/unauthorized",
                        "No Autorizado",
                        401,
                        detail != null ? detail : "Credenciales de autenticación inválidas o faltantes",
                        null,
                        LocalDateTime.now(),
                        path,
                        method,
                        null
                );
        }

        /**
         * Error de permisos (403 Forbidden).
         */
        public static ErrorResponseDTO forbidden(
                String detail,
                String path,
                String method) {

                return new ErrorResponseDTO(
                        "https://api.midominio.com/errors/forbidden",
                        "Prohibido",
                        403,
                        detail != null ? detail : "No tiene permisos para acceder a este recurso",
                        null,
                        LocalDateTime.now(),
                        path,
                        method,
                        null
                );
        }


}
