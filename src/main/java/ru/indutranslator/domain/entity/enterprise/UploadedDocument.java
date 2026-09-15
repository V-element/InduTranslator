package ru.indutranslator.domain.entity.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "uploaded_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_task_id")
    private SourceTask sourceTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulation_id")
    private Regulation regulation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulation_version_id")
    private RegulationVersion regulationVersion;

    private String title;

    private String description;

    @Column(name = "content_type")
    private String contentType;

    private byte[] content;

    @Column(name = "file_name")
    private String fileName;

    private Long fileSize;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "bucket_name")
    private String bucketName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_user_id")
    private User uploadedBy;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
