package com.churninsight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsResponse {
    private Integer totalEvaluados;
    private Double tasaChurn;
    private Double accuracy;
    private Double precision;
    private Double recall;
    private Double f1Score;
}
