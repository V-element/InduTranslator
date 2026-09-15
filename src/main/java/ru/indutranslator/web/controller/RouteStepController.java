package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.RouteStepDto;
import ru.indutranslator.service.RouteStepService;

import java.util.List;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
@Tag(name = "Steps", description = "Route step management endpoints")
public class RouteStepController {

    private final RouteStepService routeStepService;

    @PostMapping
    @Operation(summary = "Create step")
    public ResponseEntity<RouteStepDto> createStep(@RequestBody RouteStepDto stepDto) {
        RouteStepDto createdStep = routeStepService.createStep(stepDto, 1L);
        return ResponseEntity.ok(createdStep);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get step by ID")
    public ResponseEntity<RouteStepDto> getStep(@PathVariable Long id) {
        return ResponseEntity.ok(routeStepService.getStepById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update step")
    public ResponseEntity<RouteStepDto> updateStep(@PathVariable Long id, @RequestBody RouteStepDto stepDto) {
        return ResponseEntity.ok(routeStepService.updateStep(id, stepDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete step")
    public ResponseEntity<Void> deleteStep(@PathVariable Long id) {
        routeStepService.deleteStep(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/route/{routeId}")
    @Operation(summary = "Get steps by route")
    public ResponseEntity<List<RouteStepDto>> getStepsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeStepService.getStepsByRoute(routeId));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get steps by department")
    public ResponseEntity<List<RouteStepDto>> getStepsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(routeStepService.getStepsByDepartment(departmentId));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending steps")
    public ResponseEntity<List<RouteStepDto>> getPendingSteps(@RequestParam Long userId) {
        return ResponseEntity.ok(routeStepService.getPendingSteps(userId));
    }

    @PostMapping("/{stepId}/start")
    @Operation(summary = "Start step")
    public ResponseEntity<Void> startStep(@PathVariable Long stepId) {
        routeStepService.startStep(stepId, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{stepId}/complete")
    @Operation(summary = "Complete step")
    public ResponseEntity<Void> completeStep(@PathVariable Long stepId) {
        routeStepService.completeStep(stepId, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{stepId}/skip")
    @Operation(summary = "Skip step")
    public ResponseEntity<Void> skipStep(@PathVariable Long stepId) {
        routeStepService.skipStep(stepId, 1L);
        return ResponseEntity.ok().build();
    }
}
