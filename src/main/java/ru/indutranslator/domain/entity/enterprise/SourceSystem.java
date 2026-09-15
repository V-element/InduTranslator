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
@Table(name = "source_systems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    private String name;

    private String code;

    private String type;

    private String config;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "sourceSystem", fetch = FetchType.LAZY)
    private Set<SourceTask> sourceTasks = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    private Instant createdAt;

    private Instant updatedAt;
}
