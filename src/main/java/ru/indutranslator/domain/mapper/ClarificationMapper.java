package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.ClarificationDto;
import ru.indutranslator.domain.entity.enterprise.Clarification;

@Mapper(componentModel = "spring")
public interface ClarificationMapper {

    ClarificationDto toDto(Clarification clarification);
    Clarification toEntity(ClarificationDto clarificationDto);
}
