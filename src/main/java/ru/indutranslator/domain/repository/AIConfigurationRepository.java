package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.AIConfiguration;

import java.util.List;
import java.util.Optional;

public interface AIConfigurationRepository extends JpaRepository<AIConfiguration, Long> {
    Optional<AIConfiguration> findByActiveTrue();
    
    Optional<AIConfiguration> findByProvider(String provider);
    
    boolean existsByActiveTrue();
    
    List<AIConfiguration> findAll();
}

