package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.ExecutionResultDto;
import ru.indutranslator.domain.entity.enterprise.ExecutionResult;

@Mapper(componentModel = "spring")
public interface ExecutionResultMapper {

    ExecutionResultDto toDto(ExecutionResult result);
    ExecutionResult toEntity(ExecutionResultDto resultDto);
}
