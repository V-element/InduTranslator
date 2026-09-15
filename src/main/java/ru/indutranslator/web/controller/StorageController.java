package ru.indutranslator.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {

    private final ru.indutranslator.service.StorageService storageService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Storage service is operational");
    }

    @GetMapping("/buckets")
    public ResponseEntity<java.util.List<String>> listBuckets() {
        // This would list MinIO buckets
        return ResponseEntity.ok(java.util.List.of("indutranslator-documents"));
    }
}

