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
public class TaskVersionDto {

    private Long id;

    private Long sourceTaskId;

    private Integer versionNumber;

    private String title;

    private String description;

    private String content;

    private String versionType;

    private Long createdById;

    private String createdByName;

    private Instant createdAt;

    private String diffFromPrevious;
}
