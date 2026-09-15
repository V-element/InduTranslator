package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.TaskAdaptation;

import java.util.List;

public interface TaskAdaptationRepository extends JpaRepository<TaskAdaptation, Long> {

    List<TaskAdaptation> findBySourceTaskId(Long taskId);

    List<TaskAdaptation> findByDepartmentId(Long departmentId);

    List<TaskAdaptation> findByRoleId(Long roleId);

    List<TaskAdaptation> findByStatus(String status);
}
