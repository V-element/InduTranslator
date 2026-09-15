package ru.indutranslator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.indutranslator.domain.dto.EventDto;
import ru.indutranslator.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Event management endpoints")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Create event")
    public ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.createEvent(eventDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get events by task")
    public ResponseEntity<List<EventDto>> getEventsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(eventService.getEventsByTask(taskId));
    }

    @GetMapping("/type/{eventType}")
    @Operation(summary = "Get events by type")
    public ResponseEntity<List<EventDto>> getEventsByType(@PathVariable String eventType) {
        return ResponseEntity.ok(eventService.getEventsByType(eventType));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent events")
    public ResponseEntity<List<EventDto>> getRecentEvents(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(eventService.getRecentEvents(limit));
    }
}
