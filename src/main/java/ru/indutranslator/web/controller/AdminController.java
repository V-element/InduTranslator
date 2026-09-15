package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.RoleDto;
import ru.indutranslator.domain.entity.enterprise.Enterprise;
import ru.indutranslator.domain.entity.enterprise.SourceSystem;
import ru.indutranslator.domain.repository.EnterpriseRepository;
import ru.indutranslator.domain.repository.SourceSystemRepository;
import ru.indutranslator.service.AdminService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin management endpoints")
public class AdminController {

    private final AdminService adminService;
    private final EnterpriseRepository enterpriseRepository;
    private final SourceSystemRepository sourceSystemRepository;

    // Role management
    @PostMapping("/roles")
    @Operation(summary = "Create role (Admin only)")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(adminService.createRole(roleDto, 1L));
    }

    @PutMapping("/roles/{id}")
    @Operation(summary = "Update role (Admin only)")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(adminService.updateRole(id, roleDto, 1L));
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Delete role (Admin only)")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        adminService.deleteRole(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    @Operation(summary = "Get all roles (Admin only)")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(adminService.getAllRoles(0, 100));
    }

    // User management - moved to AdminUserController to avoid conflicts

    // ROI configuration
    @PostMapping("/roi/coefficient")
    @Operation(summary = "Update ROI coefficient (Admin only)")
    public ResponseEntity<Void> updateROICoefficient(@RequestBody Map<String, Double> request) {
        String metricName = request.keySet().iterator().next();
        adminService.updateROICoefficient(metricName, request.get(metricName), 1L);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roi/coefficient/{metricName}")
    @Operation(summary = "Get ROI coefficient (Admin only)")
    public ResponseEntity<Double> getROICoefficient(@PathVariable String metricName) {
        return ResponseEntity.ok(adminService.getROICoefficient(metricName));
    }

    // AI configuration
    @PostMapping("/ai/provider")
    @Operation(summary = "Update AI provider config (Admin only)")
    public ResponseEntity<Void> updateAIProviderConfig(@RequestBody Map<String, String> request) {
        adminService.updateAIProviderConfig(
            request.get("provider"),
            request.get("apiKey"),
            request.get("baseUrl"),
            1L
        );
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/ai/config/{id}/activate")
    @Operation(summary = "Activate AI provider (Admin only)")
    public ResponseEntity<Void> activateAIProvider(@PathVariable Long configId) {
        adminService.activateAIProvider(configId, 1L);
        return ResponseEntity.ok().build();
    }

    // System configuration
    @PostMapping("/config")
    @Operation(summary = "Update system config (Admin only)")
    public ResponseEntity<Void> updateSystemConfig(@RequestBody Map<String, String> request) {
        adminService.updateSystemConfig(request.keySet().iterator().next(), request.values().iterator().next(), 1L);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/config/{key}")
    @Operation(summary = "Get system config (Admin only)")
    public ResponseEntity<String> getSystemConfig(@PathVariable String key) {
        return ResponseEntity.ok(adminService.getSystemConfig(key));
    }

    @PostMapping("/initialize")
    @Operation(summary = "Initialize default enterprise and source system (Admin only)")
    public ResponseEntity<Map<String, Object>> initializeDefaults() {
        // Create default enterprise if not exists
        Enterprise enterprise = enterpriseRepository.findById(1L)
            .orElseGet(() -> {
                Enterprise newEnterprise = new Enterprise();
                newEnterprise.setName("Default Enterprise");
                newEnterprise.setCode("DEFAULT");
                newEnterprise.setDescription("Default enterprise for the system");
                return enterpriseRepository.save(newEnterprise);
            });

        // Create default source system if not exists
        SourceSystem sourceSystem = sourceSystemRepository.findById(1L)
            .orElseGet(() -> {
                SourceSystem newSourceSystem = new SourceSystem();
                newSourceSystem.setEnterprise(enterprise);
                newSourceSystem.setName("Default Source System");
                newSourceSystem.setCode("DEFAULT");
                newSourceSystem.setType("INTERNAL");
                newSourceSystem.setActive(true);
                return sourceSystemRepository.save(newSourceSystem);
            });

        return ResponseEntity.ok(Map.of(
            "message", "Initialization completed",
            "enterpriseId", enterprise.getId(),
            "sourceSystemId", sourceSystem.getId()
        ));
    }
}
