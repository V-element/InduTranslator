package ru.indutranslator.ai.provider;

import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;

/**
 * Interface for AI providers.
 * Implements Strategy pattern for different AI provider implementations.
 */
public interface AIProvider {
    
    /**
     * Generate AI response from request.
     * @param request AI request with prompt and configuration
     * @return AI response with generated content
     */
    AIResponseDto generate(AIRequestDto request);
    
    /**
     * Generate text response from prompt.
     * @param prompt text prompt
     * @return generated text response
     */
    String generateText(String prompt);
    
    /**
     * Get provider name.
     * @return provider name (openai, ollama, vllm, lmstudio, fallback)
     */
    String getName();
    
    /**
     * Check if provider is healthy.
     * @return true if provider is healthy and available
     */
    boolean isHealthy();
}
