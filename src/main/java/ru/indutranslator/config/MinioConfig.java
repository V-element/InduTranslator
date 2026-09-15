package ru.indutranslator.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint:localhost}")
    private String endpoint;

    @Value("${minio.port:9000}")
    private int port;

    @Value("${minio.access-key:minioadmin}")
    private String accessKey;

    @Value("${minio.secret-key:minioadmin123}")
    private String secretKey;

    @Value("${minio.use-ssl:false}")
    private boolean useSSL;

    @Bean
    public MinioClient minioClient() {
        String minioUrl = "http" + (useSSL ? "s" : "") + "://" + endpoint + ":" + port;
        log.info("Initializing MinIO client with URL: {}", minioUrl);

        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .build();
    }
}

