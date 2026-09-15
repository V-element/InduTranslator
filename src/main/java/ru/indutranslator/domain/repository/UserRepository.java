package ru.indutranslator.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.indutranslator.domain.entity.enterprise.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :filter, '%'))")
    Page<User> findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("filter") String filter, Pageable pageable);
    
    Page<User> findAllByActive(Boolean active, Pageable pageable);
}

