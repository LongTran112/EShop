package com.shopme.admin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper utility to get the correct file URL (S3 or local) based on configuration.
 * Use this in controllers/services to convert relative paths to full URLs when S3 is enabled.
 */
@Component
public class FileUploadHelper {

    @Autowired(required = false)
    private S3Service s3Service;

    /**
     * Converts a relative file path to the appropriate URL.
     * If S3 is enabled, returns the S3 URL. Otherwise, returns the relative path (for local storage).
     *
     * @param relativePath The relative path (e.g., "/product-images/1/image.png")
     * @return Full S3 URL if S3 is enabled, otherwise the relative path
     */
    public String getFileUrl(String relativePath) {
        if (s3Service != null && s3Service.isEnabled() && relativePath != null && relativePath.startsWith("/")) {
            // Convert relative path to S3 key (remove leading slash)
            String s3Key = relativePath.substring(1);
            String s3Url = s3Service.getFileUrl(s3Key);
            return s3Url != null ? s3Url : relativePath;
        }
        return relativePath;
    }

    /**
     * Checks if S3 storage is enabled
     */
    public boolean isS3Enabled() {
        return s3Service != null && s3Service.isEnabled();
    }
}

