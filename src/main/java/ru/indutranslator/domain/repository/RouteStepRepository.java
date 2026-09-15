package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.RouteStep;

import java.util.List;

public interface RouteStepRepository extends JpaRepository<RouteStep, Long> {

    List<RouteStep> findByTaskRouteId(Long routeId);

    List<RouteStep> findByDepartmentId(Long departmentId);

    List<RouteStep> findByUserIdAndStatus(Long userId, String status);
}
