package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.RouteStepDto;
import ru.indutranslator.domain.entity.enterprise.RouteStep;

@Mapper(componentModel = "spring")
public interface RouteStepMapper {

    RouteStepDto toDto(RouteStep step);
    RouteStep toEntity(RouteStepDto stepDto);
}
