package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.indutranslator.domain.dto.DocumentDto;
import ru.indutranslator.domain.entity.Document;
import ru.indutranslator.domain.entity.enterprise.User;
import ru.indutranslator.domain.mapper.DocumentMapper;
import ru.indutranslator.domain.repository.DocumentRepository;
import ru.indutranslator.domain.repository.UserRepository;
import ru.indutranslator.service.DocumentService;
import ru.indutranslator.service.StorageService;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final UserRepository userRepository;
    private final StorageService storageService;

    @Value("${minio.bucket-name:indutranslator-documents}")
    private String bucketName;

    @Override
    @Transactional
    public DocumentDto uploadDocument(DocumentDto documentDto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate embedding (placeholder for now)
        // embedding = embeddingService.generateEmbedding(documentDto.getContent());

        Document document = documentMapper.toEntity(documentDto);
        document.setUser(user);
        document.setCreatedAt(Instant.now());
        document.setUpdatedAt(Instant.now());
        // Embedding is handled as JSONB string in the entity
        document.setEmbedding("[]"); // Empty array placeholder

        // Upload to storage
        try {
            byte[] content = documentDto.getContent() != null ? documentDto.getContent() : new byte[0];
            String storagePath = storageService.uploadFile(
                new java.io.ByteArrayInputStream(content),
                documentDto.getFileName(),
                documentDto.getContentType(),
                bucketName
            );
            document.setStoragePath(storagePath);
            document.setBucketName(bucketName);
        } catch (Exception e) {
            log.error("Failed to upload document to storage: {}", e.getMessage());
            throw new RuntimeException("Failed to upload document", e);
        }

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDto(savedDocument);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentDto getDocument(Long id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        return documentMapper.toDto(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getUserDocuments(Long userId) {
        List<Document> documents = documentRepository.findByUserId(userId);
        return documents.stream().map(documentMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void deleteDocument(Long id, Long userId) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found"));

        // Check if user owns the document
        if (!document.getUser().getId().equals(userId)) {
            throw new RuntimeException("Document does not belong to user");
        }

        // Delete from storage
        try {
            if (document.getStoragePath() != null) {
                storageService.deleteFile(document.getStoragePath(), document.getBucketName());
            }
        } catch (Exception e) {
            log.warn("Failed to delete document from storage: {}", e.getMessage());
        }

        documentRepository.delete(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> searchSimilarDocuments(float[] embedding, int limit) {
        // For MVP, return recent documents since we don't have vector search
        // In production, this would use pgvector for semantic similarity
        List<Document> documents = documentRepository.findByUserIdOrderByCreatedAtDesc(0L);
        return documents.stream().limit(limit).map(documentMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void generateEmbedding(Long documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));

        // Generate embedding (placeholder - in production, call embedding service)
        try {
            // embedding = embeddingService.generateEmbedding(document.getContent());
            // document.setEmbedding(embedding);
            documentRepository.save(document);
        } catch (Exception e) {
            log.error("Failed to generate embedding: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getAllDocuments(int page, int size) {
        return documentRepository.findAll().stream()
            .map(documentMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByDepartment(Long departmentId) {
        return documentRepository.findByDepartmentId(departmentId).stream()
            .map(documentMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByTask(Long taskId) {
        return documentRepository.findByTaskId(taskId).stream()
            .map(documentMapper::toDto).toList();
    }
}

