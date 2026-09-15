package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.TaskVersion;

import java.util.List;
import java.util.Optional;

public interface TaskVersionRepository extends JpaRepository<TaskVersion, Long> {

    List<TaskVersion> findBySourceTaskId(Long taskId);

    Optional<TaskVersion> findBySourceTaskIdAndVersionNumber(Long taskId, Integer versionNumber);
}
