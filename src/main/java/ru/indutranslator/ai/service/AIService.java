package ru.indutranslator.ai.service;

import ru.indutranslator.domain.dto.AIConfigurationDto;
import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;

import java.util.List;

/**
 * AI service interface.
 * Provides AI generation capabilities using different providers.
 */
public interface AIService {
    
    /**
     * Get active AI configuration.
     * @return active configuration DTO
     */
    AIConfigurationDto getActiveConfiguration();
    
    /**
     * Update AI configuration.
     * @param configDto configuration DTO
     * @return updated configuration DTO
     */
    AIConfigurationDto updateConfiguration(AIConfigurationDto configDto);
    
    /**
     * Generate AI response from request.
     * @param requestDto AI request with prompt and configuration
     * @return AI response with generated content
     */
    AIResponseDto generate(AIRequestDto requestDto);
    
    /**
     * Generate text response from prompt.
     * @param prompt text prompt
     * @return generated text response
     */
    String generateText(String prompt);
    
    /**
     * Set active AI configuration.
     * @param id configuration ID
     * @return updated configuration DTO
     */
    AIConfigurationDto setActiveConfiguration(Long id);
    
    /**
     * Generate with specific provider.
     * @param requestDto AI request
     * @param providerName provider name (openai, ollama, vllm, lmstudio)
     * @return AI response
     */
    AIResponseDto generateWithProvider(AIRequestDto requestDto, String providerName);
    
    /**
     * Get available providers.
     * @return list of provider names
     */
    List<String> getAvailableProviders();
    
    /**
     * Reset circuit breaker for provider.
     * @param providerName provider name
     */
    void resetCircuitBreaker(String providerName);
}
