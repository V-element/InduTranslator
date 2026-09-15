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
public class SourceTaskDto {

    private Long id;

    private Long enterpriseId;

    private Long sourceSystemId;

    private String externalId;

    private String title;

    private String description;

    private Integer priority;

    private String status;

    private Long createdByUserId;

    private String createdByName;

    private List<Long> routeIds;

    private List<Long> versionIds;

    private List<Long> adaptationIds;

    private Instant createdAt;

    private Instant updatedAt;

    private EnterpriseDto enterprise;

    private SourceSystemDto sourceSystem;
}
