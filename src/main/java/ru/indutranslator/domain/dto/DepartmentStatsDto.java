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
public class DepartmentStatsDto {

    private Long departmentId;

    private String departmentName;

    private Long taskCount;

    private Long completedCount;

    private Long pendingCount;

    private Double averageCompletionTime;

    private Double slaComplianceRate;

    private List<String> topReasonsForReturn;
}
