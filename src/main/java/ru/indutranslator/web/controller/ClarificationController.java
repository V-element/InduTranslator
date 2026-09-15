package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.ClarificationDto;
import ru.indutranslator.service.ClarificationService;

import java.util.List;

@RestController
@RequestMapping("/api/clarifications")
@RequiredArgsConstructor
@Tag(name = "Clarifications", description = "Clarification management endpoints")
public class ClarificationController {

    private final ClarificationService clarificationService;

    @PostMapping
    @Operation(summary = "Create new clarification")
    public ResponseEntity<ClarificationDto> createClarification(@RequestBody ClarificationDto clarificationDto) {
        ClarificationDto createdClarification = clarificationService.createClarification(clarificationDto, 1L);
        return ResponseEntity.ok(createdClarification);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get clarification by ID")
    public ResponseEntity<ClarificationDto> getClarification(@PathVariable Long id) {
        return ResponseEntity.ok(clarificationService.getClarificationById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update clarification")
    public ResponseEntity<ClarificationDto> updateClarification(
            @PathVariable Long id, @RequestBody ClarificationDto clarificationDto) {
        return ResponseEntity.ok(clarificationService.updateClarification(id, clarificationDto, 1L));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete clarification")
    public ResponseEntity<Void> deleteClarification(@PathVariable Long id) {
        clarificationService.deleteClarification(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get clarifications by task")
    public ResponseEntity<List<ClarificationDto>> getClarificationsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(clarificationService.getClarificationsByTask(taskId));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending clarifications")
    public ResponseEntity<List<ClarificationDto>> getPendingClarifications() {
        return ResponseEntity.ok(clarificationService.getPendingClarifications());
    }

    @PostMapping("/{id}/answer")
    @Operation(summary = "Answer clarification")
    public ResponseEntity<Void> answerClarification(
            @PathVariable Long id, @RequestBody String answer) {
        clarificationService.answerClarification(id, answer, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "Close clarification")
    public ResponseEntity<Void> closeClarification(@PathVariable Long id) {
        clarificationService.closeClarification(id, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reopen")
    @Operation(summary = "Reopen clarification")
    public ResponseEntity<Void> reopenClarification(@PathVariable Long id) {
        clarificationService.reopenClarification(id, 1L);
        return ResponseEntity.ok().build();
    }
}
