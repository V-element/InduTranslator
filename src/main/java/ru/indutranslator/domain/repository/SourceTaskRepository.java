package ru.indutranslator.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.indutranslator.domain.entity.enterprise.SourceTask;

import java.util.List;

public interface SourceTaskRepository extends JpaRepository<SourceTask, Long> {

    List<SourceTask> findByStatus(String status);

    Page<SourceTask> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);

    int countByStatus(String status);

    @Query("SELECT DISTINCT t.status FROM SourceTask t WHERE t.status IS NOT NULL")
    List<String> findDistinctStatuses();
}
