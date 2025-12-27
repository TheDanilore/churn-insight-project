package com.churninsight.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public record ChurnRequestDTO(
    
    @NotBlank(message = "La antigüedad es obligatoria")
    @Min(value = 0, message = "La antigüedad no puede ser negativa")
    @Max(value=72,message= "La antiguedad no puede exceder a 72 meses(6 años)")
    @JsonProperty("antiguedad")
    Integer antiguedad, // Meses como cliente

    @NotBlank(message = "El tipo de contrato es obligatorio")
    @Pattern(regexp = "^(Month-to-month|One year|Two year)$",
            message = "El contrato debe ser: Month-to-month, One year o Two year")
    @JsonProperty("contrato")
    String contrato,

    @NotBlank(message = "Los cargos mensuales son obligatorios")
    @DecimalMin(value = 18.25,inclusive= true
         message = "Los cargos mensuales no pueden ser negativos")
    @DecimalMax(value=118.75, inclusive= true,
         message = "Los cargos mensuales no pueden exceder 118.75")
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
)

implements  Serializable{
    //Constructor con validaciones adicionales
    public ChurnRequestDTO{
        //Validaciones adicionales manuales
    if(antiguedad!=null && antiguedad>72){
        throw new IllegalArgumentException("La antiguedad no puede exceder 72 meses(6años)");
    }

    if(cargosMensuales!=null&&cargosMensuales>118.75){
        throw new IllegalArgumentException("Los cargos mensuales no puede exceder 118.75");
    }
    //Normalizaciòn de strings

        if (contrato != null) {
            contrato = contrato.trim();
        }
        if (soporteTecnico != null) {
            soporteTecnico = soporteTecnico.trim();
        }
        if (servicioInternet != null) {
            servicioInternet = servicioInternet.trim();
        }
        if (metodoPago != null) {
            metodoPago = metodoPago.trim();
        }
    }

    /**
     * Constructor para deserialización JSON.
     * Útil cuando Jackson necesita un constructor accesible.
     */

    public static ChurnRequestDTO create(
            @JsonProperty("antiguedad") Integer antiguedad,
            @JsonProperty("contrato") String contrato,
            @JsonProperty("cargos_mensuales") Double cargosMensuales,
            @JsonProperty("soporte_tecnico") String soporteTecnico,
            @JsonProperty("servicio_internet") String servicioInternet,
            @JsonProperty("metodo_pago") String metodoPago) {

        return new ChurnRequestDTO(
                antiguedad,
                contrato,
                cargosMensuales,
                soporteTecnico,
                servicioInternet,
                metodoPago
        );
    }

    /**
     * Método de conveniencia para crear instancias en tests.
     */
    public static ChurnRequestDTO of(
            Integer antiguedad,
            String contrato,
            Double cargosMensuales,
            String soporteTecnico,
            String servicioInternet,
            String metodoPago) {

        return new ChurnRequestDTO(
                antiguedad,
                contrato,
                cargosMensuales,
                soporteTecnico,
                servicioInternet,
                metodoPago
        );
    }
}
