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
public class TaskAdaptationDto {

    private Long id;

    private Long sourceTaskId;

    private Long departmentId;

    private String departmentName;

    private Long roleId;

    private String roleName;

    private String adaptationType;

    private String description;

    private String originalContent;

    private String adaptedContent;

    private String reason;

    private List<String> citations;

    private Boolean isFallback;

    private String status;

    private Long createdById;

    private String createdByName;

    private Instant createdAt;

    private Instant updatedAt;
}
