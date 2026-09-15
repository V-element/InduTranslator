package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.RoiMetric;

import java.util.List;

public interface RoiMetricRepository extends JpaRepository<RoiMetric, Long> {

    List<RoiMetric> findBySourceTaskId(Long taskId);
}
