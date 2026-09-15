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
public class UploadResponse {

    private Long id;

    private String fileName;

    private String filePath;

    private String contentType;

    private Long fileSize;

    private String message;

    private Instant createdAt;
}
