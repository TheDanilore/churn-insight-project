package com.churninsight.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prediction_history") // Nombre de tabla más semántico
@Data
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // --- INPUTS (Lo que envió el usuario) ---
    private Integer antiguedad;
    private String contrato;
    
    @Column(name = "cargos_mensuales")
    private Double cargosMensuales;
    
    @Column(name = "soporte_tecnico")
    private String soporteTecnico;
    
    @Column(name = "servicio_internet")
    private String servicioInternet;
    
    @Column(name = "metodo_pago")
    private String metodoPago;

    // --- OUTPUTS (Lo que respondió la IA) ---
    private String resultado; // "Va a cancelar" / "Se queda"
    private Double probabilidad;
    private String alerta;    // "ALTA" / "BAJA"

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
