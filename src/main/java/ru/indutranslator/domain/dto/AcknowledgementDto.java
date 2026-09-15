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
public class AcknowledgementDto {

    private Long id;

    private Long executionResultId;

    private Long acknowledgedByUserId;

    private String acknowledgedByName;

    private String status;

    private String comment;

    private Instant acknowledgedAt;

    private ExecutionResultDto executionResult;
}
