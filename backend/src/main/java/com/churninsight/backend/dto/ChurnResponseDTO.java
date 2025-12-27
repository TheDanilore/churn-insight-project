package com.churninsight.backend.dto;


public record ChurnResponseDTO(
    @JsonProperty("prevision")
    String prevision,     // Ej: "Va a cancelar"

    @JsonProperty("probabilidad")
    Double probabilidad,

    @JsonProperty("mensaje")// Ej: 0.85
    String alerta         // Ej: "ALTA" (Para que el Frontend sepa si pintar Rojo o Verde)
)
        implements Serializable{

    // Constructor con valores por defecto para nulls
    public ChurnResponseDTO {
        // Asegurar que no haya nulls en la respuesta
        prevision = prevision != null ? prevision : "";
        probabilidad = probabilidad != null ? probabilidad : 0.0;
        mensaje = mensaje != null ? mensaje : "";

        // Validar rango de probabilidad
        if (probabilidad < 0.0 || probabilidad > 1.0) {
            throw new IllegalArgumentException("La probabilidad debe estar entre 0.0 y 1.0");
        }
    }
    /**
     * Factory method para respuesta exitosa.
     */
    public static ChurnResponseDTO success(String prevision, Double probabilidad, String mensaje) {
        return new ChurnResponseDTO(prevision, probabilidad, mensaje);
    }
    /**
     * Factory method para respuesta de error.
     * Mantiene la misma estructura con valores sentinela.
     */
    public static ChurnResponseDTO error(String mensajeError) {
        return new ChurnResponseDTO(
                "ERROR",          // prevision
                -1.0,             // probabilidad (sentinela para errores)
                mensajeError != null ? mensajeError : "Error desconocido"
        );
    }
    /**
     * Factory method para crear desde el modelo de ML.
     */
    public static ChurnResponseDTO fromPrediction(Double probabilidadPrediccion) {
        if (probabilidadPrediccion == null) {
            return error("No se pudo generar la predicción");
        }
        String prevision = probabilidadPrediccion >= 0.5 ? "Va a cancelar" : "Se va a quedar";
        String mensaje = generarMensajeRecomendacion(probabilidadPrediccion);

        return new ChurnResponseDTO(
                prevision,
                Math.round(probabilidadPrediccion * 100.0) / 100.0, // Redondear a 2 decimales
                mensaje
        );
    }

    private static String generarMensajeRecomendacion(Double probabilidad) {
        if (probabilidad >= 0.8) {
            return "Alto riesgo de cancelación. Se recomienda contacto inmediato del equipo de retención.";
        } else if (probabilidad >= 0.5) {
            return "Riesgo moderado de cancelación. Considerar ofertas de fidelización.";
        } else if (probabilidad >= 0.3) {
            return "Riesgo bajo de cancelación. Monitorear comportamiento.";
        } else {
            return "Cliente estable. Bajo riesgo de cancelación.";
        }
    }

    /**
     * Método auxiliar para verificar si es una respuesta de error.
     */
    public boolean isError() {
        return "ERROR".equals(prevision) && probabilidad < 0;
    }

    /**
     * Método auxiliar para verificar si es una respuesta exitosa.
     */
    public boolean isSuccess() {
        return !isError();
    }
}
