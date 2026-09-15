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
public class IntegrationDto {

    private Long id;

    private String name;

    private String description;

    private String type;

    private String apiUrl;

    private String apiKey;

    private String config;

    private Boolean active;

    private Long lastSyncId;

    private Instant lastSyncAt;

    private String lastStatus;

    private Instant createdAt;

    private Instant updatedAt;
}
