package ru.indutranslator.ai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.indutranslator.ai.provider.AIProvider;
import ru.indutranslator.ai.provider.AIProviderFactory;
import ru.indutranslator.ai.strategy.CircuitBreaker;
import ru.indutranslator.ai.strategy.RetryStrategy;
import ru.indutranslator.ai.service.AIService;
import ru.indutranslator.domain.dto.AIConfigurationDto;
import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;
import ru.indutranslator.domain.entity.AIConfiguration;
import ru.indutranslator.domain.mapper.AIConfigurationMapper;
import ru.indutranslator.domain.repository.AIConfigurationRepository;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {
    
    private final AIConfigurationRepository aiConfigurationRepository;
    private final AIConfigurationMapper aiConfigurationMapper;
    private final AIProviderFactory providerFactory;
    private final RetryStrategy retryStrategy;
    private final CircuitBreaker circuitBreaker;
    
    @Override
    public AIConfigurationDto getActiveConfiguration() {
        AIConfiguration activeConfig = aiConfigurationRepository.findByActiveTrue()
            .orElseGet(() -> {
                AIConfiguration config = AIConfiguration.builder()
                    .provider("openai")
                    .model("gpt-4")
                    .maxTokens(2048)
                    .temperature(0.7)
                    .topP(1.0)
                    .active(true)
                    .build();
                aiConfigurationRepository.save(config);
                return config;
            });
        return aiConfigurationMapper.toDto(activeConfig);
    }
    
    @Override
    public AIConfigurationDto updateConfiguration(AIConfigurationDto configDto) {
        AIConfiguration existingConfig = aiConfigurationRepository.findByActiveTrue().orElse(null);
        
        if (existingConfig != null) {
            existingConfig.setProvider(configDto.getProvider());
            existingConfig.setApiKey(configDto.getApiKey());
            existingConfig.setBaseUrl(configDto.getBaseUrl());
            existingConfig.setModel(configDto.getModel());
            existingConfig.setMaxTokens(configDto.getMaxTokens());
            existingConfig.setTemperature(configDto.getTemperature());
            existingConfig.setTopP(configDto.getTopP());
            existingConfig.setUpdatedAt(Instant.now());
            
            // Deactivate other configurations
            List<AIConfiguration> otherConfigs = aiConfigurationRepository.findAll();
            AIConfiguration finalExistingConfig = existingConfig;
            otherConfigs.forEach(c -> {
                if (!c.getId().equals(finalExistingConfig.getId())) {
                    c.setActive(false);
                }
            });
            aiConfigurationRepository.saveAll(otherConfigs);
        } else {
            existingConfig = aiConfigurationMapper.toEntity(configDto);
            existingConfig.setActive(true);
            existingConfig.setCreatedAt(Instant.now());
            existingConfig.setUpdatedAt(Instant.now());
        }
        
        AIConfiguration savedConfig = aiConfigurationRepository.save(existingConfig);
        return aiConfigurationMapper.toDto(savedConfig);
    }
    
    @Override
    public AIResponseDto generate(AIRequestDto requestDto) {
        return generateWithProvider(requestDto, null);
    }
    
    @Override
    public String generateText(String prompt) {
        AIRequestDto request = AIRequestDto.builder()
            .prompt(prompt)
            .build();
        return generate(request).getChoices().get(0).getText();
    }
    
    @Override
    public AIConfigurationDto setActiveConfiguration(Long id) {
        java.util.Optional<AIConfiguration> optionalConfig = aiConfigurationRepository.findById(id);
        if (optionalConfig.isEmpty()) {
            throw new RuntimeException("Configuration not found");
        }
        
        AIConfiguration config = optionalConfig.get();
        config.setActive(true);
        config.setUpdatedAt(Instant.now());
        
        // Deactivate all other configurations
        List<AIConfiguration> allConfigs = aiConfigurationRepository.findAll();
        allConfigs.forEach(c -> {
            if (!c.getId().equals(id)) {
                c.setActive(false);
            }
        });
        aiConfigurationRepository.saveAll(allConfigs);
        
        return aiConfigurationMapper.toDto(config);
    }
    
    @Override
    public AIResponseDto generateWithProvider(AIRequestDto requestDto, String providerName) {
        // Select provider
        String provider;
        
        // If no provider specified, try to get from active DB config
        if (providerName == null || providerName.isEmpty()) {
            AIConfiguration activeConfig = aiConfigurationRepository.findByActiveTrue().orElse(null);
            provider = activeConfig != null ? activeConfig.getProvider() : "gigachat";
        } else {
            provider = providerName;
        }
        
        AIProvider aiProvider = providerFactory.getProvider(provider);
        
        // Check circuit breaker
        if (!circuitBreaker.allowCall(provider)) {
            log.warn("Circuit breaker OPEN for provider: {}", provider);
            throw new RuntimeException("Service temporarily unavailable. Please try again later.");
        }
        
        // If request has no API key but DB config exists, use DB key
        if ((requestDto.getApiKey() == null || requestDto.getApiKey().isEmpty()) && 
            provider.equals("gigachat")) {
            AIConfiguration activeConfig = aiConfigurationRepository.findByActiveTrue().orElse(null);
            if (activeConfig != null && activeConfig.getApiKey() != null) {
                requestDto.setApiKey(activeConfig.getApiKey());
            }
        }
        
        try {
            // Execute with retry
            return retryStrategy.executeWithRetry(() -> {
                AIResponseDto response = aiProvider.generate(requestDto);
                circuitBreaker.recordSuccess(provider);
                return response;
            });
        } catch (Exception e) {
            circuitBreaker.recordFailure(provider);
            log.error("AI generation failed for provider {}: {}", provider, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public List<String> getAvailableProviders() {
        return providerFactory.getAllProviders().stream()
            .map(AIProvider::getName)
            .toList();
    }

    @Override
    public void resetCircuitBreaker(String providerName) {
        circuitBreaker.reset(providerName);
        log.info("Circuit breaker reset for provider: {}", providerName);
    }
}
