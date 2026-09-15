package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.indutranslator.domain.entity.Document;
import ru.indutranslator.domain.entity.enterprise.User;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByFileName(String fileName);
    
    List<Document> findByUserId(Long userId);
    
    List<Document> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Document> findByTitleContainingOrDescriptionContaining(String title, String description);
    
    List<Document> findByDepartmentId(@Param("departmentId") Long departmentId);
    
    @Query("SELECT d FROM Document d JOIN d.sourceTask st WHERE st.id = :taskId")
    List<Document> findByTaskId(@Param("taskId") Long taskId);
}

