package com.churninsight.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDataRequest {
    
    @NotBlank(message = "El ID del cliente es obligatorio")
    private String customerId;
    
    @NotNull(message = "Los meses de contrato son obligatorios")
    @Positive(message = "Los meses de contrato deben ser positivos")
    private Integer contractLengthMonths;
    
    @NotNull(message = "El cargo mensual es obligatorio")
    @PositiveOrZero(message = "El cargo mensual no puede ser negativo")
    private Double monthlyCharges;
    
    @NotNull(message = "El cargo total es obligatorio")
    @PositiveOrZero(message = "El cargo total no puede ser negativo")
    private Double totalCharges;
    
    @NotBlank(message = "El tipo de servicio de internet es obligatorio")
    private String internetService;
    
    @NotBlank(message = "El estado de seguridad en línea es obligatorio")
    private String onlineSecurity;
    
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;
    
    @NotNull(message = "Los meses de antigüedad son obligatorios")
    @PositiveOrZero(message = "Los meses de antigüedad no pueden ser negativos")
    private Integer tenureMonths;
    
    @NotNull(message = "La frecuencia de login es obligatoria")
    @PositiveOrZero(message = "La frecuencia de login no puede ser negativa")
    private Integer monthlyLoginFrequency;
}
