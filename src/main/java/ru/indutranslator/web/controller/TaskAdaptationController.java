package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.TaskAdaptationDto;
import ru.indutranslator.service.TaskAdaptationService;

import java.util.List;

@RestController
@RequestMapping("/api/adaptations")
@RequiredArgsConstructor
@Tag(name = "Adaptations", description = "Task adaptation management endpoints")
public class TaskAdaptationController {

    private final TaskAdaptationService taskAdaptationService;

    @PostMapping
    @Operation(summary = "Create new adaptation")
    public ResponseEntity<TaskAdaptationDto> createAdaptation(@RequestBody TaskAdaptationDto adaptationDto) {
        TaskAdaptationDto createdAdaptation = taskAdaptationService.createAdaptation(adaptationDto, 1L);
        return ResponseEntity.ok(createdAdaptation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get adaptation by ID")
    public ResponseEntity<TaskAdaptationDto> getAdaptation(@PathVariable Long id) {
        return ResponseEntity.ok(taskAdaptationService.getAdaptationById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update adaptation")
    public ResponseEntity<TaskAdaptationDto> updateAdaptation(
            @PathVariable Long id, @RequestBody TaskAdaptationDto adaptationDto) {
        return ResponseEntity.ok(taskAdaptationService.updateAdaptation(id, adaptationDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete adaptation")
    public ResponseEntity<Void> deleteAdaptation(@PathVariable Long id) {
        taskAdaptationService.deleteAdaptation(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get adaptations by task")
    public ResponseEntity<List<TaskAdaptationDto>> getAdaptationsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskAdaptationService.getAdaptationsByTask(taskId));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get adaptations by department")
    public ResponseEntity<List<TaskAdaptationDto>> getAdaptationsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(taskAdaptationService.getAdaptationsByDepartment(departmentId));
    }

    @GetMapping("/role/{roleId}")
    @Operation(summary = "Get adaptations by role")
    public ResponseEntity<List<TaskAdaptationDto>> getAdaptationsByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(taskAdaptationService.getAdaptationsByRole(roleId));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending adaptations")
    public ResponseEntity<List<TaskAdaptationDto>> getPendingAdaptations() {
        return ResponseEntity.ok(taskAdaptationService.getPendingAdaptations());
    }

    @PostMapping("/{id}/regenerate")
    @Operation(summary = "Regenerate adaptation")
    public ResponseEntity<Void> regenerateAdaptation(@PathVariable Long id) {
        taskAdaptationService.regenerateAdaptation(id, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve adaptation")
    public ResponseEntity<Void> approveAdaptation(@PathVariable Long id) {
        taskAdaptationService.approveAdaptation(id, 1L);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/fallback")
    @Operation(summary = "Set fallback flag")
    public ResponseEntity<Void> setFallback(@PathVariable Long id, @RequestBody Boolean isFallback) {
        taskAdaptationService.setFallbackFlag(id, isFallback, 1L);
        return ResponseEntity.ok().build();
    }
}
