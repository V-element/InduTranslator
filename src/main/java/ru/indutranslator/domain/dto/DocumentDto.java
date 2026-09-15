package ru.indutranslator.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotBlank
    private String contentType;

    @NotNull
    private byte[] content;

    @NotBlank
    @Size(max = 255)
    private String fileName;

    private Long fileSize;

    @Size(max = 500)
    private String storagePath;

    @Size(max = 100)
    private String bucketName;

    @Size(max = 10000)
    private String embedding;

    private String metadata;

    private Long userId;

    private Instant createdAt;

    private Instant updatedAt;
}

