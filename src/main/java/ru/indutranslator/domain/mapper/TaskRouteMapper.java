package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.TaskRouteDto;
import ru.indutranslator.domain.entity.enterprise.TaskRoute;

@Mapper(componentModel = "spring", uses = RouteStepMapper.class)
public interface TaskRouteMapper {

    TaskRouteDto toDto(TaskRoute route);
    TaskRoute toEntity(TaskRouteDto routeDto);
}
