package ru.indutranslator.ai.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;

import java.time.Instant;
import java.util.*;

/**
 * GigaChat provider implementation.
 * Supports SberBank GigaChat API with OAuth2 authentication
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GigaChatProvider implements AIProvider {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Override
    public AIResponseDto generate(AIRequestDto request) {
        String model = request.getModel() != null ? request.getModel() : "GigaChat-Pro";
        String prompt = request.getPrompt();
        Integer maxTokens = request.getMaxTokens() != null ? request.getMaxTokens() : 2048;
        Double temperature = request.getTemperature() != null ? request.getTemperature() : 0.7;
        Double topP = request.getTopP() != null ? request.getTopP() : 1.0;
        
        try {
            // Get API key from request
            String apiKey = request.getApiKey();
            if (apiKey == null || apiKey.isEmpty()) {
                throw new RuntimeException("API key not provided");
            }
            
            // Remove "Bearer " prefix if present
            String cleanApiKey = apiKey.replaceFirst("^Bearer\\s+", "");
            
            // Step 1: Get OAuth token from GigaChat
            log.info("Calling GigaChat API with apiKey={}", cleanApiKey.substring(0, Math.min(20, cleanApiKey.length())) + "...");
            String token = getOAuthToken(cleanApiKey);
            
            // Step 2: Build chat completion request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", temperature);
            requestBody.put("top_p", topP);
            requestBody.put("stream", false);
            
            // Create headers with OAuth token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("RqUID", UUID.randomUUID().toString());
            headers.set("Authorization", "Bearer " + token);
            
            // Create request
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // GigaChat API
            String apiUrl = "https://gigachat.devices.sberbank.ru/api/v1/chat/completions";
            
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);
            
            return buildResponseFromChatCompletions(response.getBody());
            
        } catch (Exception e) {
            log.error("GigaChat generation failed: {}", e.getMessage(), e);
            throw new RuntimeException("GigaChat generation failed: " + e.getMessage());
        }
    }
    
    /**
     * Get OAuth token from GigaChat using API key (password credentials)
     */
    private String getOAuthToken(String apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            // GigaChat OAuth endpoint
            String tokenUrl = "https://gigachat.devices.sberbank.ru/api/v1/oauth";
            
            // API key is base64(client_id:client_secret), decode it
            String decodedKey;
            try {
                decodedKey = new String(java.util.Base64.getDecoder().decode(apiKey), java.nio.charset.StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
                decodedKey = apiKey;
            }
            
            String[] parts = decodedKey.split(":", 2);
            String clientId = parts.length > 0 ? parts[0] : "";
            String clientSecret = parts.length > 1 ? parts[1] : "";
            
            // Build form data
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("scope", "GIGACHAT_API_PERS");
            form.add("grant_type", "client_credentials");
            
            // Add Basic Auth header
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedCredentials);
            
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);
            
            log.info("Requesting GigaChat OAuth token... clientId={}, encodedAuth={}", 
                clientId, encodedCredentials.substring(0, Math.min(20, encodedCredentials.length())) + "...");
            
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);
            
            log.info("OAuth response status={}, body={}", response.getStatusCode(), response.getBody());
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode json = objectMapper.readTree(response.getBody());
                String accessToken = json.path("access_token").asText();
                if (accessToken != null && !accessToken.isEmpty()) {
                    log.info("Successfully obtained GigaChat OAuth token");
                    return accessToken;
                }
            }
            
            // If token endpoint fails, try using the API key directly
            log.warn("Token endpoint failed, using API key directly. Response: {}", response.getBody());
            return apiKey;
            
        } catch (Exception e) {
            log.warn("Failed to get OAuth token, using API key directly: {}", e.getMessage());
            return apiKey;
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
        return "gigachat";
    }
    
    @Override
    public boolean isHealthy() {
        try {
            String healthUrl = "https://gigachat.devices.sberbank.ru/api/v1/models";
            
            HttpHeaders headers = new HttpHeaders();
            String apiKey = System.getProperty("ai.gigachat.api-key", "");
            headers.set("Authorization", "Bearer " + apiKey);
            
            restTemplate.getForEntity(healthUrl, String.class);
            return true;
        } catch (Exception e) {
            log.warn("GigaChat health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    private AIResponseDto buildResponseFromChatCompletions(Map<String, Object> response) {
        if (response == null) {
            return AIResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .object("chat.completion")
                .created(Instant.now().getEpochSecond())
                .choices(List.of(AIResponseDto.Choice.builder().text("No response generated").build()))
                .build();
        }
        
        List<Map<String, Object>> choicesData = (List<Map<String, Object>>) response.get("choices");
        List<AIResponseDto.Choice> choices = new ArrayList<>();
        
        for (Map<String, Object> choiceData : choicesData) {
            Map<String, Object> message = (Map<String, Object>) choiceData.get("message");
            if (message != null && message.containsKey("content")) {
                choices.add(AIResponseDto.Choice.builder()
                    .index(choiceData.containsKey("index") ? ((Number) choiceData.get("index")).intValue() : 0)
                    .text((String) message.get("content"))
                    .finishReason((String) choiceData.get("finish_reason"))
                    .build());
            }
        }
        
        if (choices.isEmpty()) {
            choices.add(AIResponseDto.Choice.builder()
                .text("No content in response")
                .finishReason("stop")
                .build());
        }
        
        return AIResponseDto.builder()
            .id(response.containsKey("id") ? (String) response.get("id") : UUID.randomUUID().toString())
            .object("chat.completion")
            .created(response.containsKey("created") ? ((Number) response.get("created")).longValue() : Instant.now().getEpochSecond())
            .model(response.containsKey("model") ? (String) response.get("model") : "gigachat")
            .choices(choices)
            .metadata(Collections.emptyMap())
            .build();
    }
}
