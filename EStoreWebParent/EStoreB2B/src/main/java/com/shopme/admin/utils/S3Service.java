package com.shopme.admin.utils;

import com.shopme.admin.config.S3ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);

    @Autowired(required = false)
    private S3Client s3Client;

    @Autowired
    private S3ConfigProperties s3ConfigProperties;

    public void uploadFile(String key, MultipartFile multipartFile) throws IOException {
        if (!s3ConfigProperties.isEnabled() || s3Client == null) {
            throw new IllegalStateException("S3 is not enabled or S3Client is not configured");
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(key)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));
            LOGGER.info("File uploaded to S3: {}", key);
        } catch (S3Exception e) {
            LOGGER.error("Error uploading file to S3: {}", key, e);
            throw new IOException("Could not upload file to S3: " + key, e);
        }
    }

    public void deleteFile(String key) {
        if (!s3ConfigProperties.isEnabled() || s3Client == null) {
            return;
        }

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            LOGGER.info("File deleted from S3: {}", key);
        } catch (S3Exception e) {
            LOGGER.error("Error deleting file from S3: {}", key, e);
        }
    }

    public void deleteDirectory(String prefix) {
        if (!s3ConfigProperties.isEnabled() || s3Client == null) {
            return;
        }

        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(s3ConfigProperties.getBucketName())
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);
            List<S3Object> objects = listResponse.contents();

            if (!objects.isEmpty()) {
                List<ObjectIdentifier> objectIdentifiers = objects.stream()
                        .map(S3Object::key)
                        .map(key -> ObjectIdentifier.builder().key(key).build())
                        .collect(Collectors.toList());

                DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                        .bucket(s3ConfigProperties.getBucketName())
                        .delete(Delete.builder().objects(objectIdentifiers).build())
                        .build();

                s3Client.deleteObjects(deleteRequest);
                LOGGER.info("Deleted {} objects from S3 with prefix: {}", objects.size(), prefix);
            }
        } catch (S3Exception e) {
            LOGGER.error("Error deleting directory from S3: {}", prefix, e);
        }
    }

    public String getFileUrl(String key) {
        if (!s3ConfigProperties.isEnabled()) {
            return null;
        }

        // If using a custom endpoint (like MinIO or DigitalOcean Spaces)
        if (s3ConfigProperties.getEndpointUrl() != null && !s3ConfigProperties.getEndpointUrl().isEmpty()) {
            String endpoint = s3ConfigProperties.getEndpointUrl();
            if (endpoint.endsWith("/")) {
                endpoint = endpoint.substring(0, endpoint.length() - 1);
            }
            return endpoint + "/" + s3ConfigProperties.getBucketName() + "/" + key;
        }

        // Standard AWS S3 URL format
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3ConfigProperties.getBucketName(),
                s3ConfigProperties.getRegion(),
                key);
    }

    public boolean isEnabled() {
        return s3ConfigProperties.isEnabled() && s3Client != null;
    }
}

