package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.AuditLogDto;
import ru.indutranslator.domain.entity.enterprise.AuditLog;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogMapper INSTANCE = Mappers.getMapper(AuditLogMapper.class);

    AuditLogDto toDto(AuditLog log);
    AuditLog toEntity(AuditLogDto logDto);
}
