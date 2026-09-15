package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.ExecutionResultDto;
import ru.indutranslator.service.ExecutionService;

import java.util.List;

@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
@Tag(name = "Executions", description = "Execution and steps management endpoints")
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping
    @Operation(summary = "Create new execution result")
    public ResponseEntity<ExecutionResultDto> createResult(@RequestBody ExecutionResultDto resultDto) {
        ExecutionResultDto createdResult = executionService.createResult(resultDto, 1L);
        return ResponseEntity.ok(createdResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get execution result by ID")
    public ResponseEntity<ExecutionResultDto> getResult(@PathVariable Long id) {
        return ResponseEntity.ok(executionService.getResultById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update execution result")
    public ResponseEntity<ExecutionResultDto> updateResult(
            @PathVariable Long id, @RequestBody ExecutionResultDto resultDto) {
        return ResponseEntity.ok(executionService.updateResult(id, resultDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete execution result")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        executionService.deleteResult(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get results by task")
    public ResponseEntity<List<ExecutionResultDto>> getResultsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(executionService.getResultsByTask(taskId));
    }

    @GetMapping("/step/{stepId}")
    @Operation(summary = "Get results by step")
    public ResponseEntity<List<ExecutionResultDto>> getResultsByStep(@PathVariable Long stepId) {
        return ResponseEntity.ok(executionService.getResultsByStep(stepId));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending results")
    public ResponseEntity<List<ExecutionResultDto>> getPendingResults() {
        return ResponseEntity.ok(executionService.getPendingResults());
    }

    @PostMapping("/{resultId}/complete")
    @Operation(summary = "Complete step")
    public ResponseEntity<Void> completeStep(@PathVariable Long resultId) {
        executionService.completeStep(resultId, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{resultId}/acknowledge")
    @Operation(summary = "Acknowledge result")
    public ResponseEntity<Void> acknowledgeResult(@PathVariable Long resultId) {
        executionService.acknowledgeResult(resultId, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{resultId}/comment")
    @Operation(summary = "Add comment")
    public ResponseEntity<Void> addComment(
            @PathVariable Long resultId, @RequestBody String comment) {
        executionService.addComment(resultId, comment, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{resultId}/attach/{documentId}")
    @Operation(summary = "Attach file")
    public ResponseEntity<Void> attachFile(@PathVariable Long resultId, @PathVariable Long documentId) {
        executionService.attachFile(resultId, documentId, 1L);
        return ResponseEntity.ok().build();
    }
}
