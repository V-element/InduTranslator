package ru.indutranslator.service;

import ru.indutranslator.domain.dto.DocumentDto;

import java.util.List;

public interface DocumentService {

    DocumentDto uploadDocument(DocumentDto documentDto, Long userId);
    
    DocumentDto getDocument(Long id);
    
    List<DocumentDto> getUserDocuments(Long userId);
    
    void deleteDocument(Long id, Long userId);
    
    List<DocumentDto> searchSimilarDocuments(float[] embedding, int limit);
    
    void generateEmbedding(Long documentId);

    List<DocumentDto> getAllDocuments(int page, int size);

    List<DocumentDto> getDocumentsByDepartment(Long departmentId);

    List<DocumentDto> getDocumentsByTask(Long taskId);
}
