package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "route_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_route_id", nullable = false)
    private TaskRoute taskRoute;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "sla_hours")
    private Integer slaHours;

    private String status;

    @Column(name = "order_number")
    private Integer orderNumber;

    private String dependencies;

    @OneToMany(mappedBy = "routeStep", fetch = FetchType.LAZY)
    private Set<Acknowledgement> acknowledgements = new HashSet<>();

    @OneToMany(mappedBy = "routeStep", fetch = FetchType.LAZY)
    private Set<ExecutionResult> executionResults = new HashSet<>();

    @OneToMany(mappedBy = "routeStep", fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
