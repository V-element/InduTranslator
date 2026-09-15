package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "integrations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Integration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    private String name;

    private String code;

    private String configEncrypted;

    @Column(name = "is_active")
    private Boolean active = true;

    private String description;

    private String type;

    @Column(name = "api_url")
    private String apiUrl;

    @Column(name = "api_key")
    private String apiKey;

    private String config;

    @Column(name = "last_sync_id")
    private Long lastSyncId;

    @Column(name = "last_sync_at")
    private Instant lastSyncAt;

    @Column(name = "last_status")
    private String lastStatus;

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
