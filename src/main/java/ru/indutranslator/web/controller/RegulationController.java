package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.RegulationDto;
import ru.indutranslator.service.RegulationService;

import java.util.List;

@RestController
@RequestMapping("/api/regulations")
@RequiredArgsConstructor
@Tag(name = "Regulations", description = "Regulation management endpoints")
public class RegulationController {

    private final RegulationService regulationService;

    @PostMapping
    @Operation(summary = "Create new regulation")
    public ResponseEntity<RegulationDto> createRegulation(@RequestBody RegulationDto regulationDto) {
        RegulationDto createdRegulation = regulationService.createRegulation(regulationDto, 1L);
        return ResponseEntity.ok(createdRegulation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get regulation by ID")
    public ResponseEntity<RegulationDto> getRegulation(@PathVariable Long id) {
        return ResponseEntity.ok(regulationService.getRegulationById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update regulation")
    public ResponseEntity<RegulationDto> updateRegulation(
            @PathVariable Long id, @RequestBody RegulationDto regulationDto) {
        return ResponseEntity.ok(regulationService.updateRegulation(id, regulationDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete regulation")
    public ResponseEntity<Void> deleteRegulation(@PathVariable Long id) {
        regulationService.deleteRegulation(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all regulations")
    public ResponseEntity<List<RegulationDto>> getAllRegulations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(regulationService.getAllRegulations(page, size));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get regulations by department")
    public ResponseEntity<List<RegulationDto>> getRegulationsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(regulationService.getRegulationsByDepartment(departmentId));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active regulations")
    public ResponseEntity<List<RegulationDto>> getActiveRegulations() {
        return ResponseEntity.ok(regulationService.getActiveRegulations());
    }

    @GetMapping("/route/{routeId}")
    @Operation(summary = "Get regulations by task route")
    public ResponseEntity<List<RegulationDto>> getRegulationsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(regulationService.getRegulationsByTaskRoute(routeId));
    }

    @PostMapping("/{regulationId}/route/{routeId}/apply")
    @Operation(summary = "Apply regulation to route")
    public ResponseEntity<Void> applyRegulationToRoute(
            @PathVariable Long regulationId, @PathVariable Long routeId) {
        regulationService.applyRegulationToRoute(regulationId, routeId, 1L);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{regulationId}/route/{routeId}/remove")
    @Operation(summary = "Remove regulation from route")
    public ResponseEntity<Void> removeRegulationFromRoute(
            @PathVariable Long regulationId, @PathVariable Long routeId) {
        regulationService.removeRegulationFromRoute(regulationId, routeId, 1L);
        return ResponseEntity.ok().build();
    }
}
