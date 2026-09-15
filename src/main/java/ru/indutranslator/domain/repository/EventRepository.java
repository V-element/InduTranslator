package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findBySourceTaskId(Long taskId);

    List<Event> findByEventType(String eventType);

    List<Event> findTop10ByOrderByCreatedAtDesc();
}
