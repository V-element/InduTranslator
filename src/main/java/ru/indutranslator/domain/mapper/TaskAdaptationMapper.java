package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.TaskAdaptationDto;
import ru.indutranslator.domain.entity.enterprise.TaskAdaptation;

@Mapper(componentModel = "spring")
public interface TaskAdaptationMapper {

    TaskAdaptationDto toDto(TaskAdaptation adaptation);
    TaskAdaptation toEntity(TaskAdaptationDto adaptationDto);
}
