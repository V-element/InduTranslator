package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task_adaptations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAdaptation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id", nullable = false)
    private SourceTask sourceTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_version_id")
    private TaskVersion taskVersion;

    private String title;

    private String content;

    @Column(name = "ai_model")
    private String aiModel;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "context_data")
    private String contextData;

    @Column(name = "adaptation_type")
    private String adaptationType;

    private String reason;

    @Column(name = "is_fallback")
    private Boolean fallback = false;

    private String status;

    @Column(name = "original_content")
    private String originalContent;

    @Column(name = "adapted_content")
    private String adaptedContent;

    @ElementCollection
    @CollectionTable(name = "task_adaptation_citations", joinColumns = @JoinColumn(name = "adaptation_id"))
    @Column(name = "citation")
    private List<String> citations;

    @OneToMany(mappedBy = "taskAdaptation", fetch = FetchType.LAZY)
    private Set<Acknowledgement> acknowledgements = new HashSet<>();

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
