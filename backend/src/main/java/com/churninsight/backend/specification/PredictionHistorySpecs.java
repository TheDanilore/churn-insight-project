package com.churninsight.backend.specification;

import java.util.ArrayList;
import org.springframework.data.jpa.domain.Specification;
import com.churninsight.backend.model.PredictionHistory;
import jakarta.persistence.criteria.Predicate;
import java.util.List;

public class PredictionHistorySpecs {

    public static Specification<PredictionHistory> getResultsByJob(String jobId, String search, String alerta) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Job ID (Obligatorio)
            predicates.add(cb.equal(root.get("jobId"), jobId));

            // 2. Búsqueda General (Nombre, Email, Teléfono)
            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("clientName")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                    cb.like(cb.lower(root.get("phone")), pattern)
                ));
            }

            // 3. Filtro por Alerta (ALTA, MEDIA, BAJA)
            if (alerta != null && !alerta.isBlank() && !"ALL".equalsIgnoreCase(alerta)) {
                predicates.add(cb.equal(root.get("alerta"), alerta));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
