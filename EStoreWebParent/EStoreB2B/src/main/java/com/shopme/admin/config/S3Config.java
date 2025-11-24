package com.shopme.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {

    @Autowired
    private S3ConfigProperties s3ConfigProperties;

    @Bean
    public S3Client s3Client() {
        if (!s3ConfigProperties.isEnabled()) {
            return null;
        }

        var builder = S3Client.builder()
                .region(Region.of(s3ConfigProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                s3ConfigProperties.getAccessKey(),
                                s3ConfigProperties.getSecretKey()
                        )
                ));

        // Support for S3-compatible services (like MinIO, DigitalOcean Spaces)
        if (s3ConfigProperties.getEndpointUrl() != null && !s3ConfigProperties.getEndpointUrl().isEmpty()) {
            builder.endpointOverride(URI.create(s3ConfigProperties.getEndpointUrl()));
        }

        return builder.build();
    }
}

