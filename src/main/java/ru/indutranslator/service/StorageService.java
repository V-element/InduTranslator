package ru.indutranslator.service;

import ru.indutranslator.domain.dto.StorageDto;

import java.io.InputStream;
import java.util.List;

public interface StorageService {

    String uploadFile(InputStream inputStream, String fileName, String contentType, String bucketName);
    
    void deleteFile(String filePath, String bucketName);
    
    byte[] downloadFile(String filePath, String bucketName);
    
    String getPresignedUrl(String filePath, String bucketName, Integer expirationSeconds);
    
    List<String> listFiles(String bucketName, String prefix);
    
    boolean fileExists(String filePath, String bucketName);
}
