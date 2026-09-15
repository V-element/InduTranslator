package ru.indutranslator.ai.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAI provider implementation.
 * Supports OpenAI API (GPT-4, GPT-3.5, etc.)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIProvider implements AIProvider {
    
    private final RestTemplate restTemplate;
    
    @Override
    public AIResponseDto generate(AIRequestDto request) {
        String model = request.getModel() != null ? request.getModel() : "gpt-4";
        String prompt = request.getPrompt();
        Integer maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 2048;
        Double temperature = request.getTemperature() != null ? request.getTemperature() : 0.7;
        Double topP = request.getTopP() != null ? request.getTopP() : 1.0;
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", temperature);
            requestBody.put("top_p", topP);
            requestBody.put("stream", false);
            
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Get API key from request or use default
            String apiKey = request.getApiKey();
            if (apiKey != null && !apiKey.isEmpty()) {
                headers.set("Authorization", "Bearer " + apiKey);
            }
            
            // Create request
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // Default: OpenAI API
            String baseUrl = System.getProperty("ai.openai.base-url", "https://api.openai.com/v1");
            String apiUrl = baseUrl.endsWith("/") ? baseUrl + "chat/completions" : baseUrl + "/chat/completions";
            
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);
            
            return buildResponseFromChatCompletions(response.getBody());
            
        } catch (Exception e) {
            log.error("OpenAI generation failed: {}", e.getMessage(), e);
            throw new RuntimeException("OpenAI generation failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String generateText(String prompt) {
        AIRequestDto request = AIRequestDto.builder()
            .prompt(prompt)
            .build();
        return generate(request).getChoices().get(0).getText();
    }
    
    @Override
    public String getName() {
        return "openai";
    }
    
    @Override
    public boolean isHealthy() {
        try {
            String baseUrl = System.getProperty("ai.openai.base-url", "https://api.openai.com/v1");
            String healthUrl = baseUrl.endsWith("/") ? baseUrl + "models" : baseUrl + "/models";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + System.getProperty("ai.openai.api-key", ""));
            
            restTemplate.getForEntity(healthUrl, Map.class);
            return true;
        } catch (Exception e) {
            log.warn("OpenAI health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    private AIResponseDto buildResponseFromChatCompletions(Map<String, Object> response) {
        if (response == null) {
            return AIResponseDto.builder()
                .id(java.util.UUID.randomUUID().toString())
                .object("chat.completion")
                .created(Instant.now().getEpochSecond())
                .choices(List.of(AIResponseDto.Choice.builder().text("No response generated").build()))
                .build();
        }
        
        List<Map<String, Object>> choicesData = (List<Map<String, Object>>) response.get("choices");
        List<AIResponseDto.Choice> choices = new java.util.ArrayList<>();
        
        for (Map<String, Object> choiceData : choicesData) {
            Map<String, Object> message = (Map<String, Object>) choiceData.get("message");
            choices.add(AIResponseDto.Choice.builder()
                .index(((Number) choiceData.get("index")).intValue())
                .text((String) message.get("content"))
                .finishReason((String) choiceData.get("finish_reason"))
                .build());
        }
        
        Map<String, Object> usageData = (Map<String, Object>) response.get("usage");
        return AIResponseDto.builder()
            .id((String) response.get("id"))
            .object("chat.completion")
            .created(((Number) response.get("created")).longValue())
            .model((String) response.get("model"))
            .choices(choices)
            .usage(AIResponseDto.Usage.builder()
                .promptTokens(((Number) usageData.get("prompt_tokens")).intValue())
                .completionTokens(((Number) usageData.get("completion_tokens")).intValue())
                .totalTokens(((Number) usageData.get("total_tokens")).intValue())
                .build())
            .metadata(java.util.Collections.emptyMap())
            .build();
    }
}
