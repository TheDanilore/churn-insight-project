package com.churninsight.backend.specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import com.churninsight.backend.model.BatchJob;

public class BatchJobSpecifications {

    public static Specification<BatchJob> withFilters(String status, String format, LocalDate dateFrom, LocalDate dateTo) {
        return (root, query, cb) -> {
            // 1. Usamos una lista estándar de Java
            List<Predicate> predicates = new ArrayList<>();

            // --- FILTRO ESTADO ---
            if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // --- FILTRO FORMATO ---
            if (format != null && !format.isEmpty() && !"all".equalsIgnoreCase(format)) {
                // Usamos lower() en ambos lados para ignorar mayúsculas/minúsculas
                String pattern = "%." + format.toLowerCase(); 
                predicates.add(cb.like(cb.lower(root.get("fileName")), pattern));
            }

            // --- FILTRO FECHA DESDE ---
            if (dateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom.atStartOfDay()));
            }

            // --- FILTRO FECHA HASTA ---
            if (dateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), dateTo.atTime(LocalTime.MAX)));
            }

            // 2. Combinamos todo al final
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}