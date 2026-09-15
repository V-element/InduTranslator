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
public class ExecutionResultDto {

    private Long id;

    private Long sourceTaskId;

    private String stepName;

    private String status;

    private String resultType;

    private String resultData;

    private List<String> attachments;

    private List<String> comments;

    private List<AcknowledgementDto> acknowledgements;

    private Instant completedAt;

    private Long completedByUserId;

    private String completedByName;
}
