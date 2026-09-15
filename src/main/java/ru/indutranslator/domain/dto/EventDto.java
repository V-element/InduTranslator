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
public class EventDto {

    private Long id;

    private Long sourceTaskId;

    private String eventType;

    private String message;

    private String severity;

    private String metadata;

    private Instant occurredAt;
}
