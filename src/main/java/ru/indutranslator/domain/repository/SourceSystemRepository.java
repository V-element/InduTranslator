package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.SourceSystem;

import java.util.Optional;

public interface SourceSystemRepository extends JpaRepository<SourceSystem, Long> {
    Optional<SourceSystem> findByCode(String code);
    Optional<SourceSystem> findByName(String name);
}
