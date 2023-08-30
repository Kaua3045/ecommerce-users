package com.kaua.ecommerce.users.infrastructure.config;

import com.kaua.ecommerce.users.infrastructure.configurations.properties.aws.AwsCloudProperties;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.aws.AwsS3StorageProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@ActiveProfiles("test-integration")
public class AwsConfigTest {

    @Autowired
    private AwsCloudProperties awsCloudProperties;

    @Autowired
    private AwsS3StorageProperties awsS3StorageProperties;

    @Autowired
    private AwsBasicCredentials awsBasicCredentials;

    @Autowired
    private S3Client s3Client;

    @Test
    void testInjections() {
        Assertions.assertNotNull(awsCloudProperties);
        Assertions.assertNotNull(awsS3StorageProperties);
        Assertions.assertNotNull(awsBasicCredentials);
        Assertions.assertNotNull(s3Client);

        Assertions.assertEquals("us-east-1", awsS3StorageProperties.getRegion());
        Assertions.assertEquals("testeBucket", awsS3StorageProperties.getBucketName());
        Assertions.assertEquals("123", awsCloudProperties.getAccessKey());
        Assertions.assertEquals("456", awsCloudProperties.getSecretKey());
    }
}
