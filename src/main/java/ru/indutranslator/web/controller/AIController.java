package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.AIConfigurationDto;
import ru.indutranslator.domain.dto.AIRequestDto;
import ru.indutranslator.domain.dto.AIResponseDto;
import ru.indutranslator.ai.service.AIService;
import ru.indutranslator.ai.provider.AIProvider;
import ru.indutranslator.ai.provider.AIProviderFactory;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "AI/LLM operations and configuration")
public class AIController {

    private final AIService aiService;
    private final AIProviderFactory providerFactory;

    @GetMapping("/config")
    public ResponseEntity<AIConfigurationDto> getActiveConfiguration() {
        return ResponseEntity.ok(aiService.getActiveConfiguration());
    }

    @PutMapping("/config")
    public ResponseEntity<AIConfigurationDto> updateConfiguration(@RequestBody AIConfigurationDto configDto) {
        return ResponseEntity.ok(aiService.updateConfiguration(configDto));
    }

    @PatchMapping("/config/{id}/activate")
    public ResponseEntity<AIConfigurationDto> activateConfiguration(@PathVariable Long id) {
        return ResponseEntity.ok(aiService.setActiveConfiguration(id));
    }

    @PostMapping("/generate")
    public ResponseEntity<AIResponseDto> generate(@RequestBody AIRequestDto requestDto) {
        return ResponseEntity.ok(aiService.generate(requestDto));
    }

    @PostMapping("/generate/text")
    public ResponseEntity<String> generateText(@RequestBody(required = true) String prompt) {
        return ResponseEntity.ok(aiService.generateText(prompt));
    }

    @PostMapping("/chat")
    public ResponseEntity<AIResponseDto> chat(@RequestBody AIRequestDto requestDto) {
        try {
            AIResponseDto response = aiService.generateWithProvider(requestDto, "gigachat");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.warn("Chat GigaChat fallback: {}", e.getMessage());
            String errorMessage = e.getMessage();
            if (errorMessage == null) errorMessage = "";
            boolean needsApiKey = errorMessage.isEmpty() || 
                errorMessage.contains("No active AI configuration") || 
                errorMessage.contains("Bearer") || 
                errorMessage.contains("token") || 
                errorMessage.contains("key") ||
                errorMessage.contains("API key") ||
                errorMessage.contains("configuration") ||
                errorMessage.toLowerCase().contains("недоступен");
            
            String fallback = needsApiKey
                ? "Не могу ответить: требуется API-ключ GigaChat. Настройте ключ в настройках (⚙️). " +
                  "Функция AI-чата работает через внешний API Sber GigaChat."
                : "Не могу ответить: " + errorMessage + ". Попробуйте позже.";
            
            AIResponseDto errorResponse = AIResponseDto.builder()
                .id(java.util.UUID.randomUUID().toString())
                .object("chat.completion")
                .created(System.currentTimeMillis() / 1000)
                .model("gigachat-local")
                .choices(List.of(AIResponseDto.Choice.builder()
                    .text(fallback)
                    .finishReason("stop")
                    .build()))
                .build();
            return ResponseEntity.status(200).body(errorResponse);
        }
    }

    @GetMapping("/providers")
    public ResponseEntity<List<String>> getProviders() {
        return ResponseEntity.ok(aiService.getAvailableProviders());
    }

    @PostMapping("/reset-circuit-breaker")
    public ResponseEntity<String> resetCircuitBreaker() {
        aiService.resetCircuitBreaker("gigachat");
        return ResponseEntity.ok("Circuit breaker reset");
    }
}

