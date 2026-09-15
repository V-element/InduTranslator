package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "ai_generation_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIGenerationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id")
    private SourceTask sourceTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_adaptation_id")
    private TaskAdaptation taskAdaptation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulation_version_id")
    private RegulationVersion regulationVersion;

    private String prompt;

    @Column(name = "input_data")
    private String inputData;

    @Column(name = "output_data")
    private String outputData;

    @Column(name = "ai_model")
    private String aiModel;

    private String provider;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "latency_ms")
    private Integer latencyMs;

    private Boolean success = true;

    @Column(name = "error_message")
    private String errorMessage;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
