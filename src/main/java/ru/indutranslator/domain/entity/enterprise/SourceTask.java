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
@Table(name = "source_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_system_id", nullable = false)
    private SourceSystem sourceSystem;

    @Column(name = "external_id")
    private String externalId;

    private String title;

    private String description;

    private Integer priority = 0;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<SourceTaskContext> contexts = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<TaskRoute> routes = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<TaskVersion> versions = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<TaskAdaptation> adaptations = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<Clarification> clarifications = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<Acknowledgement> acknowledgements = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<ExecutionResult> executionResults = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "sourceTask", fetch = FetchType.LAZY)
    private Set<RoiMetric> roiMetrics = new HashSet<>();

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
