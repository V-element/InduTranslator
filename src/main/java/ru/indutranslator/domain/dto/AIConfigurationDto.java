package ru.indutranslator.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIConfigurationDto {

    private Long id;

    @NotBlank
    private String provider;

    @NotBlank
    private String apiKey;

    @NotBlank
    private String baseUrl;

    @NotBlank
    private String model;

    @NotNull
    private Integer maxTokens;

    @NotNull
    private Double temperature;

    @NotNull
    private Double topP;

    private boolean active;
}

