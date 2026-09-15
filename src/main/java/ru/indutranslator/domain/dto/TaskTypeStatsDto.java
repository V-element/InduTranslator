package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeStatsDto {

    private String taskType;

    private Long count;

    private Double averageCompletionTime;

    private Double slaComplianceRate;

    private List<String> commonIssues;
}
