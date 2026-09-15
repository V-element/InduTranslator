package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.SourceTaskDto;
import ru.indutranslator.domain.entity.enterprise.SourceTask;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface SourceTaskMapper {

    SourceTaskDto toDto(SourceTask task);
    SourceTask toEntity(SourceTaskDto taskDto);
}
