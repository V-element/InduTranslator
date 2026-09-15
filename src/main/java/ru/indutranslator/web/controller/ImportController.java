package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.ImportResultDto;
import ru.indutranslator.service.ImportService;

import java.util.List;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Tag(name = "Import", description = "Data import endpoints")
public class ImportController {

    private final ImportService importService;

    @PostMapping("/task")
    @Operation(summary = "Import task")
    public ResponseEntity<ImportResultDto> importTask(
            @RequestParam String sourceSystem,
            @RequestParam String externalId) {
        return ResponseEntity.ok(importService.importTask(sourceSystem, externalId, 1L));
    }

    @PostMapping("/document")
    @Operation(summary = "Import document")
    public ResponseEntity<ImportResultDto> importDocument(
            @RequestParam String sourceSystem,
            @RequestParam String externalId) {
        return ResponseEntity.ok(importService.importDocument(sourceSystem, externalId, 1L));
    }

    @PostMapping("/batch")
    @Operation(summary = "Import batch")
    public ResponseEntity<ImportResultDto> importBatch(
            @RequestParam String sourceSystem,
            @RequestParam String filePath) {
        return ResponseEntity.ok(importService.importBatch(sourceSystem, filePath, 1L));
    }

    @GetMapping
    @Operation(summary = "Get import history")
    public ResponseEntity<List<ImportResultDto>> getImportHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(importService.getImportHistory(page, size));
    }

    @GetMapping("/failed")
    @Operation(summary = "Get failed imports")
    public ResponseEntity<List<ImportResultDto>> getFailedImports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(importService.getFailedImports(page, size));
    }

    @PostMapping("/{id}/retry")
    @Operation(summary = "Retry import")
    public ResponseEntity<Void> retryImport(@PathVariable Long id) {
        importService.retryImport(id, 1L);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete import")
    public ResponseEntity<Void> deleteImport(@PathVariable Long id) {
        importService.deleteImport(id, 1L);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Get import status")
    public ResponseEntity<ImportResultDto> getImportStatus(@PathVariable Long id) {
        return ResponseEntity.ok(importService.getImportStatus(id));
    }
}
