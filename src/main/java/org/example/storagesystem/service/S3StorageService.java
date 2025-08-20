package org.example.storagesystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3StorageService {
    String uploadFile(String folder, MultipartFile file);

    void deleteFile(String key);
}
