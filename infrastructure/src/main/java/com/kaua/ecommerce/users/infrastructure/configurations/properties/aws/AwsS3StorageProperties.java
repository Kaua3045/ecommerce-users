package com.kaua.ecommerce.users.infrastructure.configurations.properties.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class AwsS3StorageProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(AwsS3StorageProperties.class);

    private String bucketName;
    private String region;

    public AwsS3StorageProperties() {}

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toString());
    }

    @Override
    public String toString() {
        return "AwsS3StorageProperties{" +
                "bucketName='" + bucketName + '\'' +
                ", region='" + region + '\'' +
                '}';
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
