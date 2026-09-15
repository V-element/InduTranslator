package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "regulation_versions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegulationVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulation_id", nullable = false)
    private Regulation regulation;

    @Column(name = "version_number")
    private String versionNumber;

    private String title;

    private String content;

    private String diff;

    private Boolean approved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_user_id")
    private User approvedBy;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    private Instant createdAt;
}
