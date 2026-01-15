package com.churninsight.backend.exception;

import com.churninsight.backend.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // CASO 1: Error de Validación del DTO (@Valid falló)
    // Ej: Antigüedad negativa o Contrato vacío.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Extraemos los campos que fallaron y sus mensajes
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        logger.warn("⚠️ Error de validación en: {}", request.getRequestURI());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // 400
                "Bad Request",
                "Datos de entrada inválidos",
                request.getRequestURI(),
                request.getMethod(),
                errors // Enviamos el detalle de qué campos fallaron
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // CASO 2: Error de Conexión con Python (WebClient)
    // Ej: El contenedor de Python está apagado o hay timeout.
    @ExceptionHandler({ResourceAccessException.class, WebClientResponseException.class})
    public ResponseEntity<ErrorResponseDTO> handlePythonConnectionErrors(Exception ex, HttpServletRequest request) {

        logger.error("🔥 Error conectando con Microservicio IA: {}", ex.getMessage());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(), // 503
                "Service Unavailable",
                "El servicio de Inteligencia Artificial no está disponible en este momento. Intente más tarde.",
                request.getRequestURI(),
                request.getMethod(),
                null
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    // CASO 3: Error Genérico (Cualquier otra cosa)
    // Ej: NullPointerException, error de base de datos inesperado.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralErrors(Exception ex, HttpServletRequest request) {

        logger.error("❌ Error interno no controlado: ", ex); // Importante loguear el stack trace aquí

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "Internal Server Error",
                "Ocurrió un error inesperado en el servidor.",
                request.getRequestURI(),
                request.getMethod(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
