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
public class ClarificationDto {

    private Long id;

    private Long sourceTaskId;

    private String question;

    private String answer;

    private Long askedByUserId;

    private String askedByName;

    private Long answeredByUserId;

    private String answeredByName;

    private String status;

    private Instant requestedAt;

    private Instant answeredAt;
}
