package ru.indutranslator.ai.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Factory for AI providers.
 * Selects the appropriate provider based on configuration.
 */
@Slf4j
@Configuration
public class AIProviderFactory {
    
    private final List<AIProvider> providers;
    
    @Autowired
    public AIProviderFactory(List<AIProvider> providers) {
        this.providers = providers;
    }
    
    /**
     * Get provider by name.
     * @param providerName provider name (openai, ollama, vllm, lmstudio, fallback)
     * @return AIProvider instance
     */
    public AIProvider getProvider(String providerName) {
        return providers.stream()
            .filter(p -> p.getName().equalsIgnoreCase(providerName))
            .findFirst()
            .orElseGet(() -> {
                log.warn("Provider {} not found, using fallback", providerName);
                return getFallbackProvider();
            });
    }
    
    /**
     * Get the primary provider (first healthy one).
     * @return primary AIProvider
     */
    @Bean
    @Primary
    public AIProvider getPrimaryProvider() {
        // Try to find healthy provider in order
        for (AIProvider provider : providers) {
            if (provider.isHealthy()) {
                log.info("Using primary provider: {}", provider.getName());
                return provider;
            }
        }
        
        // All providers failed, use fallback
        log.error("All AI providers failed, using fallback");
        return getFallbackProvider();
    }
    
    /**
     * Get fallback provider.
     * @return fallback AIProvider
     */
    private AIProvider getFallbackProvider() {
        return providers.stream()
            .filter(p -> p.getName().equalsIgnoreCase("fallback"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Fallback provider not found"));
    }
    
    /**
     * Get all available providers.
     * @return list of AIProvider instances
     */
    public List<AIProvider> getAllProviders() {
        return providers;
    }
    
    /**
     * Check if any provider is healthy.
     * @return true if at least one provider is healthy
     */
    public boolean hasHealthyProvider() {
        return providers.stream().anyMatch(AIProvider::isHealthy);
    }
}
