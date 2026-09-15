package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.AuditLogDto;
import ru.indutranslator.domain.entity.enterprise.AuditLog;
import ru.indutranslator.domain.mapper.AuditLogMapper;
import ru.indutranslator.domain.repository.AuditLogRepository;
import ru.indutranslator.service.AuditLogService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    @Transactional
    public AuditLogDto createLog(String action, String entityType, Long entityId, 
                                 Long actorUserId, Long actorDepartmentId, String ipAddress, String userAgent) {
        AuditLog log = AuditLog.builder()
            .action(action)
            .entityType(entityType)
            .entityId(entityId)
            .actorUserId(actorUserId)
            .actorDepartmentId(actorDepartmentId)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .build();
        AuditLog savedLog = auditLogRepository.save(log);
        return auditLogMapper.toDto(savedLog);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogDto getLogById(Long id) {
        AuditLog log = auditLogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Log not found: " + id));
        return auditLogMapper.toDto(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDto> getUserLogs(Long userId, int page, int size) {
        return auditLogRepository.findByActorUserId(userId).stream()
            .map(auditLogMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDto> getLogsByAction(String action, int page, int size) {
        return auditLogRepository.findByAction(action).stream()
            .map(auditLogMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDto> getLogsByEntity(String entityType, Long entityId, int page, int size) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
            .map(auditLogMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDto> getAllLogs(int page, int size) {
        return auditLogRepository.findAll().stream()
            .map(auditLogMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void deleteLog(Long id) {
        auditLogRepository.deleteById(id);
    }
}
