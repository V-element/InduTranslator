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
public class RouteStepDto {

    private Long id;

    private Long routeId;

    private Integer sequence;

    private String name;

    private String description;

    private String status;

    private Long assignedUserId;

    private String assignedUserName;

    private Long departmentId;

    private String departmentName;

    private Instant slaStartTime;

    private Instant slaEndTime;

    private String slaStatus;

    private String metadata;

    private Instant createdAt;

    private Instant updatedAt;
}
