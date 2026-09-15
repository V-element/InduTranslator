package ru.indutranslator.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "ai_configurations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    private String apiKey;

    @Column(name = "base_url")
    private String baseUrl;

    private String model;

    private Integer maxTokens;

    private Double temperature;

    @Column(name = "top_p")
    private Double topP;

    @Column(name = "is_active")
    private boolean active = false;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
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

