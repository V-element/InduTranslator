package ru.indutranslator.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.indutranslator.domain.dto.EventDto;
import ru.indutranslator.domain.entity.enterprise.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto toDto(Event event);
    Event toEntity(EventDto eventDto);
}
