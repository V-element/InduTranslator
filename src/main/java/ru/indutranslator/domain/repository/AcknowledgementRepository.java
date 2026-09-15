package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.indutranslator.domain.entity.enterprise.Acknowledgement;
import ru.indutranslator.domain.entity.enterprise.ExecutionResult;

import java.util.List;

public interface AcknowledgementRepository extends JpaRepository<Acknowledgement, Long> {

    @Query("SELECT a FROM Acknowledgement a JOIN a.executionResult er WHERE er.id = :resultId")
    List<Acknowledgement> findByExecutionResultId(@Param("resultId") Long resultId);

    List<Acknowledgement> findByUserIdAndStatus(Long userId, String status);
}
