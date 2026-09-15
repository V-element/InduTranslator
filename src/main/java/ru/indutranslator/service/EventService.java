package ru.indutranslator.service;

import ru.indutranslator.domain.dto.EventDto;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto);

    EventDto getEventById(Long id);

    void deleteEvent(Long id);

    List<EventDto> getEventsByTask(Long taskId);

    List<EventDto> getEventsByType(String eventType);

    List<EventDto> getRecentEvents(int limit);
}
