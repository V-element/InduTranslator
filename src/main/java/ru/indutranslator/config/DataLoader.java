package ru.indutranslator.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.indutranslator.domain.entity.enterprise.Role;
import ru.indutranslator.domain.entity.enterprise.User;
import ru.indutranslator.domain.repository.RoleRepository;
import ru.indutranslator.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Disabled due to LazyInitializationException issues
    // The admin user will be created manually via SQL script
    /*
    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            // Create default admin user if not exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });

                Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        return roleRepository.save(role);
                    });

                User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@example.com")
                    .firstName("Admin")
                    .lastName("User")
                    .active(true)
                    .build();

                // Set roles after saving to avoid lazy loading issues
                admin = userRepository.save(admin);
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                adminRoles.add(userRole);
                admin.setRoles(adminRoles);
                userRepository.save(admin);
                
                log.info("Created default admin user: admin/admin123");
            }

            // Create default regular user if not exists
            if (userRepository.findByUsername("user").isEmpty()) {
                Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        return roleRepository.save(role);
                    });

                User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .email("user@example.com")
                    .firstName("Regular")
                    .lastName("User")
                    .active(true)
                    .build();

                // Set roles after saving to avoid lazy loading issues
                user = userRepository.save(user);
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                user.setRoles(userRoles);
                userRepository.save(user);
                
                log.info("Created default user user: user/user123");
            }
        };
    }
    */
}
