package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.Clarification;

import java.util.List;

public interface ClarificationRepository extends JpaRepository<Clarification, Long> {

    List<Clarification> findBySourceTaskId(Long taskId);

    List<Clarification> findByStatus(String status);
}
