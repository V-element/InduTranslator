package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.AIConfigurationDto;
import ru.indutranslator.service.AIConfigurationService;

import java.util.List;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
@Tag(name = "AI Configuration", description = "AI configuration endpoints")
public class AIConfigurationController {

    private final AIConfigurationService aiConfigurationService;

    @PostMapping
    @Operation(summary = "Create configuration")
    public ResponseEntity<AIConfigurationDto> createConfiguration(@RequestBody AIConfigurationDto configDto) {
        AIConfigurationDto createdConfig = aiConfigurationService.createConfiguration(configDto, 1L);
        return ResponseEntity.ok(createdConfig);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get configuration by ID")
    public ResponseEntity<AIConfigurationDto> getConfiguration(@PathVariable Long id) {
        return ResponseEntity.ok(aiConfigurationService.getConfigurationById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update configuration")
    public ResponseEntity<AIConfigurationDto> updateConfiguration(
            @PathVariable Long id, @RequestBody AIConfigurationDto configDto) {
        return ResponseEntity.ok(aiConfigurationService.updateConfiguration(id, configDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete configuration")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable Long id) {
        aiConfigurationService.deleteConfiguration(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all configurations")
    public ResponseEntity<List<AIConfigurationDto>> getAllConfigurations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(aiConfigurationService.getAllConfigurations(page, size));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active configuration")
    public ResponseEntity<AIConfigurationDto> getActiveConfiguration() {
        return ResponseEntity.ok(aiConfigurationService.getActiveConfiguration());
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate configuration")
    public ResponseEntity<AIConfigurationDto> activateConfiguration(@PathVariable Long id) {
        return ResponseEntity.ok(aiConfigurationService.setActiveConfiguration(id, 1L));
    }

    @GetMapping("/provider/{provider}")
    @Operation(summary = "Get provider configurations")
    public ResponseEntity<List<AIConfigurationDto>> getProviderConfigurations(@PathVariable String provider) {
        return ResponseEntity.ok(aiConfigurationService.getProviderConfigurations(provider));
    }

    @PostMapping("/{id}/test")
    @Operation(summary = "Test connection")
    public ResponseEntity<Void> testConnection(@PathVariable Long id) {
        aiConfigurationService.testConnection(id, 1L);
        return ResponseEntity.ok().build();
    }
}
