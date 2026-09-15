package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDto {

    private Long id;
    private String action;
    private String entityType;
    private Long entityId;
    private Long actorUserId;
    private Long actorDepartmentId;
    private String ipAddress;
    private String userAgent;
    private Instant createdAt;
}

