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
public class CommunicationProfileDto {

    private Long id;

    private String name;

    private String description;

    private String emailFrom;

    private String emailSubjectPrefix;

    private String slackChannel;

    private String webhookUrl;

    private Boolean active;

    private Instant createdAt;
}
