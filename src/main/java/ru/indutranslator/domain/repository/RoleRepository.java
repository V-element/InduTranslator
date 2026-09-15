package ru.indutranslator.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indutranslator.domain.entity.enterprise.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
