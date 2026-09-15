package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "roi_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoiMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id")
    private SourceTask sourceTask;

    private String period;

    @Column(name = "metric_type")
    private String metricType;

    @Column(name = "metric_value")
    private Double metricValue;

    @Column(name = "baseline_value")
    private Double baselineValue;

    @Column(name = "current_value")
    private Double currentValue;

    @Column(name = "improvement_value")
    private Double improvementValue;

    @Column(name = "improvement_percentage")
    private Double improvementPercentage;

    private String currency;

    @Column(name = "calculation_date")
    private Instant calculationDate;

    private String description;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
