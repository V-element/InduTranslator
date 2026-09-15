package ru.indutranslator.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIRequestDto {

    @NotBlank
    private String prompt;

    private String model;

    private Integer maxTokens;

    private Double temperature;

    private Double topP;

    private String apiKey;

    private Map<String, Object> parameters;
}

