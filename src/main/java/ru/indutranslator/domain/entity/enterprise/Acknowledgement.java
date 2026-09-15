package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "acknowledgements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Acknowledgement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_step_id")
    private RouteStep routeStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_adaptation_id")
    private TaskAdaptation taskAdaptation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id")
    private SourceTask sourceTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_result_id")
    private ExecutionResult executionResult;

    private String status;

    private String comments;

    @Column(name = "acknowledged_at")
    private Instant acknowledgedAt;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
