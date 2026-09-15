package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoiMetricDto {

    private Long id;

    private Long sourceTaskId;

    private String metricName;

    private String metricType;

    private Double baselineValue;

    private Double currentValue;

    private Double improvementValue;

    private Double improvementPercentage;

    private String currency;

    private Instant calculationDate;

    private String description;

    private Instant createdAt;
}
