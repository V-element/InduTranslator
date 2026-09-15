package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.TaskVersionDto;
import ru.indutranslator.domain.entity.enterprise.TaskVersion;

@Mapper(componentModel = "spring")
public interface TaskVersionMapper {

    TaskVersionDto toDto(TaskVersion version);
    TaskVersion toEntity(TaskVersionDto versionDto);
}
