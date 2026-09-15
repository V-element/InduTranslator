package ru.indutranslator.ai.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;

import java.util.List;

/**
 * Fallback provider implementation.
 * Used when all other providers fail.
 * Returns mock response for graceful degradation.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FallbackAIProvider implements AIProvider {
    
    @Override
    public AIResponseDto generate(AIRequestDto request) {
        log.warn("Using fallback provider - all other providers failed");
        
        return AIResponseDto.builder()
            .id(java.util.UUID.randomUUID().toString())
            .object("chat.completion")
            .created(System.currentTimeMillis() / 1000)
            .choices(List.of(AIResponseDto.Choice.builder()
                .index(0)
                .text("This is a fallback response. Please configure a valid AI provider.")
                .finishReason("stop")
                .build()))
            .usage(AIResponseDto.Usage.builder()
                .promptTokens(0)
                .completionTokens(0)
                .totalTokens(0)
                .build())
            .metadata(java.util.Collections.emptyMap())
            .build();
    }
    
    @Override
    public String generateText(String prompt) {
        return "This is a fallback response. Please configure a valid AI provider.";
    }
    
    @Override
    public String getName() {
        return "fallback";
    }
    
    @Override
    public boolean isHealthy() {
        return true;
    }
}
