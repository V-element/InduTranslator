package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.indutranslator.domain.dto.DocumentDto;
import ru.indutranslator.service.DocumentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document management endpoints")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestHeader("X-User-Id") Long userId) throws IOException {
        
        DocumentDto documentDto = DocumentDto.builder()
            .title(title)
            .description(description)
            .fileName(file.getOriginalFilename())
            .contentType(file.getContentType())
            .content(file.getBytes())
            .fileSize(file.getSize())
            .build();

        DocumentDto savedDocument = documentService.uploadDocument(documentDto, userId);
        return ResponseEntity.ok(savedDocument);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentDto>> getUserDocuments(@PathVariable Long userId) {
        return ResponseEntity.ok(documentService.getUserDocuments(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id, 
                                               @RequestHeader("X-User-Id") Long userId) {
        documentService.deleteDocument(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentDto>> searchSimilar(
            @RequestParam("embedding") String embedding,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        // Parse embedding string to float array
        float[] embeddingArray = parseEmbedding(embedding);
        return ResponseEntity.ok(documentService.searchSimilarDocuments(embeddingArray, limit));
    }

    private float[] parseEmbedding(String embedding) {
        // Simple parsing implementation - in production use proper JSON parsing
        String[] values = embedding.replaceAll("[\\[\\]]", "").split(",");
        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Float.parseFloat(values[i].trim());
        }
        return result;
    }
}

