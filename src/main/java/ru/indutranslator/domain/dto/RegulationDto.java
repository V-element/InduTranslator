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
public class RegulationDto {

    private Long id;

    private String code;

    private String title;

    private String description;

    private String version;

    private String contentType;

    private String content;

    private Long departmentId;

    private String departmentName;

    private List<Long> taskRouteIds;

    private Boolean active;

    private Instant effectiveDate;

    private Instant createdAt;

    private Instant updatedAt;
}
