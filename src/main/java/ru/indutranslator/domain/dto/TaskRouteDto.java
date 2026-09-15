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
public class TaskRouteDto {

    private Long id;

    private Long sourceTaskId;

    private String name;

    private String description;

    private Integer sequence;

    private String status;

    private List<RouteStepDto> steps;

    private List<Long> stepIds;

    private Instant createdAt;

    private Instant updatedAt;
}
