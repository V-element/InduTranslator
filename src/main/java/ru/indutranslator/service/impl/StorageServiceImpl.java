package ru.indutranslator.service.impl;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.indutranslator.service.StorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name:indutranslator-documents}")
    private String defaultBucketName;

    @Value("${minio.endpoint:localhost}")
    private String endpoint;

    @Value("${minio.port:9000}")
    private int port;

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String contentType, String bucketName) {
        try {
            ensureBucketExists(bucketName);

            PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, -1, 10485760)
                .contentType(contentType)
                .build();

            minioClient.putObject(args);

            log.info("File uploaded successfully: {}", fileName);
            return bucketName + "/" + fileName;

        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public void deleteFile(String filePath, String bucketName) {
        try {
            String fileName = filePath.substring(filePath.indexOf("/") + 1);

            RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();

            minioClient.removeObject(args);

            log.info("File deleted successfully: {}", fileName);

        } catch (Exception e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public byte[] downloadFile(String filePath, String bucketName) {
        try {
            String fileName = filePath.substring(filePath.indexOf("/") + 1);

            GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();

            try (GetObjectResponse response = minioClient.getObject(args)) {
                return response.readAllBytes();
            }

        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to download file", e);
        }
    }

    @Override
    public String getPresignedUrl(String filePath, String bucketName, Integer expirationSeconds) {
        try {
            String fileName = filePath.substring(filePath.indexOf("/") + 1);

            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .expiry(expirationSeconds)
                .build();

            return minioClient.getPresignedObjectUrl(args);

        } catch (Exception e) {
            log.error("Error generating pre-signed URL: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate pre-signed URL", e);
        }
    }

    @Override
    public List<String> listFiles(String bucketName, String prefix) {
        List<String> files = new ArrayList<>();
        try {
            ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

            Iterable<Result<Item>> results = minioClient.listObjects(args);
            results.forEach(result -> {
                try {
                    files.add(result.get().objectName());
                } catch (Exception e) {
                    log.error("Error listing files: {}", e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("Error listing files: {}", e.getMessage(), e);
        }
        return files;
    }

    @Override
    public boolean fileExists(String filePath, String bucketName) {
        try {
            String fileName = filePath.substring(filePath.indexOf("/") + 1);

            StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();

            minioClient.statObject(args);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void ensureBucketExists(String bucketName) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Bucket created: {}", bucketName);
        }
    }
}
