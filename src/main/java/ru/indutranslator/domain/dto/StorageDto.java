package ru.indutranslator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageDto {

    private String filePath;

    private String fileName;

    private String contentType;

    private Long fileSize;

    private String bucketName;

    private String presignedUrl;

    private Instant createdAt;
}
