package ru.indutranslator.service;

import ru.indutranslator.domain.dto.AuditLogDto;

import java.util.List;

public interface AuditLogService {

    AuditLogDto createLog(String action, String entityType, Long entityId, 
                          Long actorUserId, Long actorDepartmentId, String ipAddress, String userAgent);
    
    AuditLogDto getLogById(Long id);

    List<AuditLogDto> getUserLogs(Long userId, int page, int size);

    List<AuditLogDto> getLogsByAction(String action, int page, int size);

    List<AuditLogDto> getLogsByEntity(String entityType, Long entityId, int page, int size);

    List<AuditLogDto> getAllLogs(int page, int size);

    void deleteLog(Long id);
}
