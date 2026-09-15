package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.Integration;

import java.util.List;

public interface IntegrationRepository extends JpaRepository<Integration, Long> {

    List<Integration> findByActiveTrue();
}
