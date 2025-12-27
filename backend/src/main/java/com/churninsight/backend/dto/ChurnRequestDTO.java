package com.churninsight.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChurnRequestDTO(
    
    @NotBlank(message = "La antigüedad es obligatoria")
    @Min(value = 0, message = "La antigüedad no puede ser negativa")
    @JsonProperty("antiguedad")
    Integer antiguedad, // Meses como cliente

    @NotBlank(message = "El tipo de contrato es obligatorio")
    @Pattern(regexp = "^(Month-to-month|One year|Two year)$",
            message = "El contrato debe ser: Month-to-month, One year o Two year")
    @JsonProperty("contrato")
    String contrato,

    @NotBlank(message = "Los cargos mensuales son obligatorios")
    @Min(value = 0,
         inclusive = true,
         message = "Los cargos mensuales no pueden ser negativos")
    @JsonProperty("cargos_mensuales") // Mapea JSON "cargos_mensuales" a Java "cargosMensuales"
    Double cargosMensuales,

    @NotBlank(message = "El soporte técnico es obligatorio")
    @Pattern(regexp = "^(Yes|No|No internet service)$",
            message = "Valor inválido para soporte técnico")
    @JsonProperty("soporte_tecnico")
    String soporteTecnico,

    @NotBlank(message = "El servicio de internet es obligatorio")
    // Validación estricta para que coincida con Python
    @Pattern(regexp = "^(DSL|Fiber optic|No)$",
            message = "El servicio debe ser: DSL, Fiber optic o No")
    @JsonProperty("servicio_internet")
    String servicioInternet,

    @NotBlank(message = "El método de pago es obligatorio")
    // Validación estricta (Ojo con los paréntesis en el regex)
    @Pattern(regexp = "^(Electronic check|Mailed check|Bank transfer \\(automatic\\)|Credit card \\(automatic\\))$",
            message = "Método de pago no reconocido")
    @JsonProperty("metodo_pago")
    String metodoPago
) {
    // Constructor compacto con validación manual
    @JsonCreator
    public ChurnRequestDTO {
        Objects.requireNonNull(antiguedad, "La antigüedad es obligatoria");
        Objects.requireNonNull(contrato, "El tipo de contrato es obligatorio");
        Objects.requireNonNull(cargosMensuales, "Los cargos mensuales son obligatorios");
        Objects.requireNonNull(soporteTecnico, "El soporte técnico es obligatorio");
        Objects.requireNonNull(servicioInternet, "El servicio de internet es obligatorio");
        Objects.requireNonNull(metodoPago, "El método de pago es obligatorio");

        if (antiguedad < 0) {
            throw new IllegalArgumentException("La antigüedad no puede ser negativa");
        }
        if (cargosMensuales <= 0) {
            throw new IllegalArgumentException("Los cargos mensuales deben ser mayores a 0");
        }

    }
}
