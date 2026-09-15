package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "task_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id", nullable = false)
    private SourceTask sourceTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_version_id")
    private TaskVersion parentVersion;

    @Column(name = "author_type")
    private String authorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User authorUser;

    @Column(name = "version_number")
    private Integer versionNumber;

    private String title;

    private String content;

    private String context;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    private Instant createdAt;

    @PreUpdate
    protected void onUpdate() {
        createdAt = Instant.now();
    }
}
