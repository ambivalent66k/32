package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storageObject.S3Properties;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.service.S3StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements S3StorageService {
    private final S3Client s3Client;
    private final S3Properties properties;

    @Override
    public String uploadFile(String folder, MultipartFile photo) {
        if (photo == null || photo.isEmpty()) return null;

        String key = folder + "/" + UUID.randomUUID() + "-" + photo.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(properties.getBucketName())
                            .key(key)
                            .contentType(photo.getContentType())
                            .build(),
                    RequestBody.fromInputStream(photo.getInputStream(), photo.getSize())

            );
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.PHOTO_NOT_UPLOADED);
        }
        return key;
    }

    @Override
    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(properties.getBucketName())
                            .key(key)
                            .build()
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PHOTO_NOT_DELETED);
        }
    }
}
