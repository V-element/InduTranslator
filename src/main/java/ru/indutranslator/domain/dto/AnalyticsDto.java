package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {

    private Long totalTasks;

    private Long completedTasks;

    private Long pendingTasks;

    private Long overdueTasks;

    private Double averageCompletionTime;

    private Double slaComplianceRate;

    private List<DepartmentStatsDto> departmentStats;

    private List<TaskTypeStatsDto> taskTypeStats;

    private List<TimeSeriesDto> timeSeriesData;

    private Instant generatedAt;
}
