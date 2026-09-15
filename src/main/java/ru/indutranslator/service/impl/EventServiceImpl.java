package ru.indutranslator.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.indutranslator.domain.dto.EventDto;
import ru.indutranslator.domain.entity.enterprise.Event;
import ru.indutranslator.domain.mapper.EventMapper;
import ru.indutranslator.domain.repository.EventRepository;
import ru.indutranslator.service.EventService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found: " + id));
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getEventsByTask(Long taskId) {
        return eventRepository.findBySourceTaskId(taskId).stream()
            .map(eventMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getEventsByType(String eventType) {
        return eventRepository.findByEventType(eventType).stream()
            .map(eventMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getRecentEvents(int limit) {
        return eventRepository.findTop10ByOrderByCreatedAtDesc().stream()
            .limit(limit)
            .map(eventMapper::toDto).toList();
    }
}
