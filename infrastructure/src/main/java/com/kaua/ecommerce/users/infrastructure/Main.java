package com.kaua.ecommerce.users.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootApplication
public class Main {

    @Autowired
    private static S3Client s3Client;

    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(Main.class, args);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            s3Client.close();
        }));
    }
}