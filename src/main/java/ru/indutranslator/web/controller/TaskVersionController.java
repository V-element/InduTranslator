package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.TaskVersionDto;
import ru.indutranslator.service.TaskVersionService;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
@RequiredArgsConstructor
@Tag(name = "Versions", description = "Task version management endpoints")
public class TaskVersionController {

    private final TaskVersionService taskVersionService;

    @PostMapping
    @Operation(summary = "Create version")
    public ResponseEntity<TaskVersionDto> createVersion(@RequestBody TaskVersionDto versionDto) {
        TaskVersionDto createdVersion = taskVersionService.createVersion(versionDto, 1L);
        return ResponseEntity.ok(createdVersion);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get version by ID")
    public ResponseEntity<TaskVersionDto> getVersion(@PathVariable Long id) {
        return ResponseEntity.ok(taskVersionService.getVersionById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete version")
    public ResponseEntity<Void> deleteVersion(@PathVariable Long id) {
        taskVersionService.deleteVersion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get versions by task")
    public ResponseEntity<List<TaskVersionDto>> getVersionsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskVersionService.getVersionsByTask(taskId));
    }

    @GetMapping
    @Operation(summary = "Get all versions")
    public ResponseEntity<List<TaskVersionDto>> getAllVersions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskVersionService.getAllVersions(page, size));
    }

    @GetMapping("/task/{taskId}/version/{versionNumber}")
    @Operation(summary = "Get version by number")
    public ResponseEntity<TaskVersionDto> getVersionByNumber(
            @PathVariable Long taskId, @PathVariable Integer versionNumber) {
        return ResponseEntity.ok(taskVersionService.getVersionByNumber(taskId, versionNumber));
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare versions")
    public ResponseEntity<Void> compareVersions(
            @RequestParam Long version1Id, @RequestParam Long version2Id) {
        taskVersionService.compareVersions(version1Id, version2Id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore version")
    public ResponseEntity<Void> restoreVersion(@PathVariable Long id) {
        taskVersionService.restoreVersion(id, 1L);
        return ResponseEntity.ok().build();
    }
}
