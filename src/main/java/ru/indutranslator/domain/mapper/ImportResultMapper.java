package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.ImportResultDto;
import ru.indutranslator.domain.entity.enterprise.AuditLog;

@Mapper(componentModel = "spring")
public interface ImportResultMapper {

    ImportResultMapper INSTANCE = Mappers.getMapper(ImportResultMapper.class);

    ImportResultDto toDto(AuditLog log);
    AuditLog toEntity(ImportResultDto resultDto);
}
