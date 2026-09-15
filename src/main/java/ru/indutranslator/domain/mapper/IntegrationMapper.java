package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.IntegrationDto;
import ru.indutranslator.domain.entity.enterprise.Integration;

@Mapper(componentModel = "spring")
public interface IntegrationMapper {

    IntegrationDto toDto(Integration integration);
    Integration toEntity(IntegrationDto integrationDto);
}
