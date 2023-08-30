package com.kaua.ecommerce.users.infrastructure.services.impl;

import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.services.StorageService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Objects;
import java.util.Optional;

public class AwsS3StorageService implements StorageService {

    private final String bucketName;
    private final S3Client s3Client;

    public AwsS3StorageService(final String bucketName, final S3Client s3Client) {
        this.bucketName = Objects.requireNonNull(bucketName);
        this.s3Client = Objects.requireNonNull(s3Client);
    }


    @Override
    public String uploadFile(String key, Resource resource) {
        try {
            final var KEY_WITH_PREFIX = key + "-" + resource.fileName();

            final var avatarAlreadyExistsRequest = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(key)
                    .build();

            final var avatarsContainsPrefix = s3Client.listObjects(avatarAlreadyExistsRequest);

            if (!(avatarsContainsPrefix.contents().isEmpty()) && avatarsContainsPrefix.prefix().equalsIgnoreCase(key)) {
                avatarsContainsPrefix.contents().forEach(image -> deleteFile(image.key()));
            }

            final var request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(KEY_WITH_PREFIX)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(resource.contentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(
                    resource.inputStream(),
                    resource.inputStream().available()
            ));

            return getFileUrl(KEY_WITH_PREFIX).orElse(null);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            s3Client.close();
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            final var request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            s3Client.close();
        }
    }

    @Override
    public Optional<String> getFileUrl(String key) {
        try {
            final var request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return Optional.ofNullable(s3Client.utilities().getUrl(request).toString());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            s3Client.close();
        }
    }

    @Override
    public void deleteFileByPrefix(String prefix) {
        try {
            final var request = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            final var avatars = s3Client.listObjects(request);

            if (!avatars.contents().isEmpty()) {
                avatars.contents().forEach(image -> deleteFile(image.key()));
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            s3Client.close();
        }
    }
}
