package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.ImportResultDto;
import ru.indutranslator.domain.entity.enterprise.AuditLog;
import ru.indutranslator.domain.mapper.ImportResultMapper;
import ru.indutranslator.domain.repository.AuditLogRepository;
import ru.indutranslator.service.ImportService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final AuditLogRepository auditLogRepository;
    private final ImportResultMapper importResultMapper;

    @Override
    @Transactional
    public ImportResultDto importTask(String sourceSystem, String externalId, Long userId) {
        AuditLog log = AuditLog.builder()
            .action("IMPORT_TASK")
            .entityType("SourceTask")
            .entityId(null)
            .actorUserId(userId)
            .ipAddress("127.0.0.1")
            .userAgent("ImportService")
            .build();
        AuditLog savedLog = auditLogRepository.save(log);
        return importResultMapper.toDto(savedLog);
    }

    @Override
    @Transactional
    public ImportResultDto importDocument(String sourceSystem, String externalId, Long userId) {
        AuditLog log = AuditLog.builder()
            .action("IMPORT_DOCUMENT")
            .entityType("Document")
            .entityId(null)
            .actorUserId(userId)
            .ipAddress("127.0.0.1")
            .userAgent("ImportService")
            .build();
        AuditLog savedLog = auditLogRepository.save(log);
        return importResultMapper.toDto(savedLog);
    }

    @Override
    @Transactional
    public ImportResultDto importBatch(String sourceSystem, String filePath, Long userId) {
        AuditLog log = AuditLog.builder()
            .action("IMPORT_BATCH")
            .entityType("Batch")
            .entityId(null)
            .actorUserId(userId)
            .ipAddress("127.0.0.1")
            .userAgent("ImportService")
            .build();
        AuditLog savedLog = auditLogRepository.save(log);
        return importResultMapper.toDto(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImportResultDto> getImportHistory(int page, int size) {
        return auditLogRepository.findAll().stream()
            .map(importResultMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImportResultDto> getFailedImports(int page, int size) {
        return auditLogRepository.findAll().stream()
            .map(importResultMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void retryImport(Long id, Long userId) {
        log.info("Retrying import: {}", id);
    }

    @Override
    @Transactional
    public void deleteImport(Long id, Long userId) {
        auditLogRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ImportResultDto getImportStatus(Long id) {
        AuditLog log = auditLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Import not found: " + id));
        return importResultMapper.toDto(log);
    }
}
