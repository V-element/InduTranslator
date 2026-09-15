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
@Table(name = "regulations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Regulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    private String title;

    private String code;

    private String description;

    private String category;

    private String version;

    @Column(name = "content_type")
    private String contentType;

    private String content;

    @Column(name = "effective_from")
    private Instant effectiveFrom;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "regulation", fetch = FetchType.LAZY)
    private Set<DepartmentRegulation> departmentRegulations = new HashSet<>();

    @OneToMany(mappedBy = "regulation", fetch = FetchType.LAZY)
    private Set<RegulationVersion> versions = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_route_id")
    private TaskRoute taskRoute;

    private Instant createdAt;

    private Instant updatedAt;
}
