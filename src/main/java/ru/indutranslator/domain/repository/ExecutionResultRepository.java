package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.ExecutionResult;

import java.util.List;

public interface ExecutionResultRepository extends JpaRepository<ExecutionResult, Long> {

    List<ExecutionResult> findBySourceTaskId(Long taskId);

    List<ExecutionResult> findByRouteStepId(Long stepId);

    List<ExecutionResult> findByStatus(String status);
}
