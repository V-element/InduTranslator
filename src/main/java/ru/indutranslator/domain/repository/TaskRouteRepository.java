package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.TaskRoute;

import java.util.List;

public interface TaskRouteRepository extends JpaRepository<TaskRoute, Long> {

    List<TaskRoute> findBySourceTaskId(Long taskId);

    List<TaskRoute> findByStatus(String status);
}
