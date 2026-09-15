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
public class ImportResultDto {

    private Long id;

    private String sourceSystem;

    private String sourceId;

    private String type;

    private String status;

    private String errorMessage;

    private Long importedEntityId;

    private Long userId;

    private Instant importedAt;

    private Instant completedAt;
}
