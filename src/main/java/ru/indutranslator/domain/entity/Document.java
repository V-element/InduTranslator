package ru.indutranslator.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import ru.indutranslator.domain.entity.enterprise.User;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String contentType;

    @Column(length = 10485760, columnDefinition = "bytea") // 10MB max
    private byte[] content;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "bucket_name")
    private String bucketName;

    // Vector embeddings stored as JSONB for flexibility
    @Column(name = "embedding", columnDefinition = "jsonb")
    private String embedding;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id")
    private ru.indutranslator.domain.entity.enterprise.SourceTask sourceTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private ru.indutranslator.domain.entity.enterprise.Department department;
}

