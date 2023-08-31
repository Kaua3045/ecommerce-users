package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.infrastructure.configurations.properties.aws.AwsS3StorageProperties;
import com.kaua.ecommerce.users.infrastructure.services.StorageService;
import com.kaua.ecommerce.users.infrastructure.services.impl.AwsS3StorageService;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class StorageConfig {

    @Bean(name = "storageService")
    @Profile({"development", "production"})
    public StorageService awsS3Storage(
            final AwsS3StorageProperties awsS3StorageProperties,
            final S3Client s3Client
    ) {
        return new AwsS3StorageService(
                awsS3StorageProperties.getBucketName(),
                s3Client
        );
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
