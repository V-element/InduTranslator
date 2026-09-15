package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.indutranslator.domain.entity.enterprise.Regulation;

import java.util.List;

public interface RegulationRepository extends JpaRepository<Regulation, Long> {

    @Query("SELECT r FROM Regulation r JOIN r.departmentRegulations dr WHERE dr.department.id = :departmentId")
    List<Regulation> findByDepartmentId(@Param("departmentId") Long departmentId);

    List<Regulation> findByActiveTrue();

    List<Regulation> findByTaskRouteId(Long routeId);
}
