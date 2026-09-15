package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.AcknowledgementDto;
import ru.indutranslator.service.AcknowledgementService;

import java.util.List;

@RestController
@RequestMapping("/api/acknowledgements")
@RequiredArgsConstructor
@Tag(name = "Acknowledgements", description = "Acknowledgement management endpoints")
public class AcknowledgementController {

    private final AcknowledgementService acknowledgementService;

    @PostMapping
    @Operation(summary = "Create acknowledgement")
    public ResponseEntity<AcknowledgementDto> createAcknowledgement(@RequestBody AcknowledgementDto acknowledgementDto) {
        AcknowledgementDto createdAcknowledgement = acknowledgementService.createAcknowledgement(acknowledgementDto, 1L);
        return ResponseEntity.ok(createdAcknowledgement);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get acknowledgement by ID")
    public ResponseEntity<AcknowledgementDto> getAcknowledgement(@PathVariable Long id) {
        return ResponseEntity.ok(acknowledgementService.getAcknowledgementById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete acknowledgement")
    public ResponseEntity<Void> deleteAcknowledgement(@PathVariable Long id) {
        acknowledgementService.deleteAcknowledgement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/result/{resultId}")
    @Operation(summary = "Get acknowledgements by result")
    public ResponseEntity<List<AcknowledgementDto>> getAcknowledgementsByResult(@PathVariable Long resultId) {
        return ResponseEntity.ok(acknowledgementService.getAcknowledgementsByResult(resultId));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending acknowledgements")
    public ResponseEntity<List<AcknowledgementDto>> getPendingAcknowledgements(@RequestParam Long userId) {
        return ResponseEntity.ok(acknowledgementService.getPendingAcknowledgements(userId));
    }

    @PostMapping("/{resultId}/acknowledge")
    @Operation(summary = "Acknowledge result")
    public ResponseEntity<Void> acknowledge(
            @PathVariable Long resultId, @RequestBody String comment) {
        acknowledgementService.acknowledge(resultId, comment, 1L);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject acknowledgement")
    public ResponseEntity<Void> rejectAcknowledgement(@PathVariable Long id, @RequestBody String reason) {
        acknowledgementService.rejectAcknowledgement(id, reason, 1L);
        return ResponseEntity.ok().build();
    }
}
