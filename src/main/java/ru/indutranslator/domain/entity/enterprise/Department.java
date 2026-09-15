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
@Table(name = "departments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    private String name;

    private String code;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_id")
    private User head;

    @Column(name = "is_active")
    private Boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "departments_competencies",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "competency_id")
    )
    private Set<Competency> competencies = new HashSet<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Department> children = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<DepartmentRegulation> departmentRegulations = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<CommunicationProfile> communicationProfiles = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<SourceTaskContext> contexts = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<TaskAdaptation> taskAdaptations = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Acknowledgement> acknowledgements = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Clarification> clarifications = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<ExecutionResult> executionResults = new HashSet<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
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
