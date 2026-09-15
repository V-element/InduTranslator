package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.IntegrationDto;
import ru.indutranslator.service.IntegrationService;
import ru.indutranslator.utils.JwtTokenProvider;

import java.util.List;

@RestController
@RequestMapping("/api/integrations")
@RequiredArgsConstructor
@Tag(name = "Integrations", description = "Integration management endpoints")
public class IntegrationController {

    private final IntegrationService integrationService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    @Operation(summary = "Create new integration")
    public ResponseEntity<IntegrationDto> createIntegration(@RequestBody IntegrationDto integrationDto, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromAuthHeader(authHeader);
        IntegrationDto createdIntegration = integrationService.createIntegration(integrationDto, username);
        return ResponseEntity.ok(createdIntegration);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get integration by ID")
    public ResponseEntity<IntegrationDto> getIntegration(@PathVariable Long id) {
        return ResponseEntity.ok(integrationService.getIntegrationById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update integration")
    public ResponseEntity<IntegrationDto> updateIntegration(
            @PathVariable Long id, @RequestBody IntegrationDto integrationDto, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromAuthHeader(authHeader);
        return ResponseEntity.ok(integrationService.updateIntegration(id, integrationDto, username));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete integration")
    public ResponseEntity<Void> deleteIntegration(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromAuthHeader(authHeader);
        integrationService.deleteIntegration(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all integrations")
    public ResponseEntity<List<IntegrationDto>> getAllIntegrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(integrationService.getAllIntegrations(page, size));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active integrations")
    public ResponseEntity<List<IntegrationDto>> getActiveIntegrations() {
        return ResponseEntity.ok(integrationService.getActiveIntegrations());
    }

    @PostMapping("/{id}/test")
    @Operation(summary = "Test connection")
    public ResponseEntity<Void> testConnection(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromAuthHeader(authHeader);
        integrationService.testConnection(id, username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sync")
    @Operation(summary = "Sync data")
    public ResponseEntity<Void> syncData(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromAuthHeader(authHeader);
        integrationService.syncData(id, username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/mapping")
    @Operation(summary = "Update mapping")
    public ResponseEntity<Void> updateMapping(@PathVariable Long id, @RequestBody String mapping, @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromAuthHeader(authHeader);
        integrationService.updateMapping(id, mapping, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/last-sync")
    @Operation(summary = "Get last sync status")
    public ResponseEntity<IntegrationDto> getLastSyncStatus(@PathVariable Long id) {
        return ResponseEntity.ok(integrationService.getLastSyncStatus(id));
    }

    private String extractUsernameFromAuthHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            return jwtTokenProvider.extractUsername(jwt);
        }
        throw new IllegalStateException("Invalid or missing Authorization header");
    }
}
