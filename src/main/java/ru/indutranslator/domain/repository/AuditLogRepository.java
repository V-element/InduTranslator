package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.AuditLog;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByActorUserId(Long userId);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
