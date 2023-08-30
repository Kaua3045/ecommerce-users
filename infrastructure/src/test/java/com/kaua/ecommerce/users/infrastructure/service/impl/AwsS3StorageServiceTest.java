package com.kaua.ecommerce.users.infrastructure.service.impl;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.services.impl.AwsS3StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@IntegrationTest
public class AwsS3StorageServiceTest {

    private AwsS3StorageService target;

    private S3Client s3Client;
    private S3Utilities s3Utilities;

    private String bucketName = "test";

    @BeforeEach
    void setUp() {
        this.s3Client = Mockito.mock(S3Client.class);
        this.s3Utilities = Mockito.mock(S3Utilities.class);
        Mockito.when(s3Client.utilities()).thenReturn(s3Utilities);

        this.target = new AwsS3StorageService(this.bucketName, s3Client);
    }

    @Test
    void givenAValidResource_whenCallUploadFile_thenShouldUploadFile() {
        // given
        final var key = "test";
        final var resource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                "test.png"
        );

        // when
        Mockito.doReturn(buildListObjectsResponse())
                        .when(s3Client)
                                .listObjects(Mockito.any(ListObjectsRequest.class));
        Mockito.doReturn(buildPutObjectResponse())
                .when(s3Client)
                        .putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));

        this.target.uploadFile(key, resource);

        // then
        final var captor = ArgumentCaptor.forClass(PutObjectRequest.class);

        Mockito.verify(s3Client, Mockito.times(1)).putObject(captor.capture(), Mockito.any(RequestBody.class));
    }

    @Test
    void givenAValidResourceButS3Throws_whenCallUploadFile_thenShouldThrowRuntimeException() {
        // given
        final var key = "test";
        final var resource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                "test.png"
        );

        // when
        Mockito.doReturn(buildListObjectsResponse())
                .when(s3Client)
                .listObjects(Mockito.any(ListObjectsRequest.class));
        Mockito.doThrow(S3Exception.class)
                .when(s3Client)
                .putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));

        Assertions.assertThrows(RuntimeException.class, () -> this.target.uploadFile(key, resource));

        // then
        final var captor = ArgumentCaptor.forClass(PutObjectRequest.class);

        Mockito.verify(s3Client, Mockito.times(1)).putObject(captor.capture(), Mockito.any(RequestBody.class));
    }

    @Test
    void givenAValidResourceAndNotExistsImageWithKey_whenCallUploadFile_thenShouldUploadFile() {
        // given
        final var key = "test";
        final var resource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                "test.png"
        );

        // when
        Mockito.doReturn(ListObjectsResponse.builder().build())
                .when(s3Client)
                .listObjects(Mockito.any(ListObjectsRequest.class));
        Mockito.doReturn(buildPutObjectResponse())
                .when(s3Client)
                .putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));

        this.target.uploadFile(key, resource);

        // then
        final var captor = ArgumentCaptor.forClass(PutObjectRequest.class);

        Mockito.verify(s3Client, Mockito.times(1)).putObject(captor.capture(), Mockito.any(RequestBody.class));
    }

    @Test
    void givenAValidResourceAndPrefixIsNotMatchingWithKey_whenCallUploadFile_thenShouldUploadFile() {
        // given
        final var key = "test";
        final var resource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                "test.png"
        );

        // when
        Mockito.doReturn(ListObjectsResponse.builder()
                        .prefix("not-match")
                        .contents(List.of(S3Object.builder()
                                .key("not-match")
                                .build()))
                        .build())
                .when(s3Client)
                .listObjects(Mockito.any(ListObjectsRequest.class));
        Mockito.doReturn(buildPutObjectResponse())
                .when(s3Client)
                .putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));

        this.target.uploadFile(key, resource);

        // then
        final var captor = ArgumentCaptor.forClass(PutObjectRequest.class);

        Mockito.verify(s3Client, Mockito.times(1)).putObject(captor.capture(), Mockito.any(RequestBody.class));
    }

    @Test
    void givenAValidPrefix_whenCallGetFileUrl_thenShouldReturnFileUrl() throws MalformedURLException {
        // given
        final var key = "test";
        final var expectedUrl = "http://localhost:8080/files/test-teste.png";

        // when
        Mockito.when(s3Utilities.getUrl(Mockito.any(GetUrlRequest.class)))
                .thenReturn(buildUrl());

        final var aResult = this.target.getFileUrl(key);

        // then
        Assertions.assertTrue(aResult.isPresent());
        Assertions.assertEquals(expectedUrl, aResult.get());

        Mockito.verify(s3Client.utilities(), Mockito.times(1)).getUrl(Mockito.any(GetUrlRequest.class));
    }

    @Test
    void givenAValidPrefixButS3Throws_whenCallGetFileUrl_thenShouldThrowRuntimeException() {
        // given
        final var key = "test";
        final var expectedErrorMessage = "software.amazon.awssdk.services.s3.model.S3Exception";

        // when
        Mockito.when(s3Utilities.getUrl(Mockito.any(GetUrlRequest.class)))
                .thenThrow(S3Exception.class);

        final var aResult = Assertions.assertThrows(RuntimeException.class,
                () -> this.target.getFileUrl(key));

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getMessage());

        Mockito.verify(s3Client.utilities(), Mockito.times(1)).getUrl(Mockito.any(GetUrlRequest.class));
    }

    @Test
    void givenAValidPrefix_whenCallDeleteFile_thenShouldDeleteFile() {
        // given
        final var key = "test";

        // when
        Mockito.when(s3Client.deleteObject(Mockito.any(DeleteObjectRequest.class)))
                .thenReturn(DeleteObjectResponse.builder().build());

        Assertions.assertDoesNotThrow(() -> this.target.deleteFile(key));

        // then
        Mockito.verify(s3Client, Mockito.times(1)).deleteObject(Mockito.any(DeleteObjectRequest.class));
    }

    @Test
    void givenAValidPrefixButS3Throws_whenCallDeleteFile_thenShouldThrowRuntimeException() {
        // given
        final var key = "test";
        final var expectedErrorMessage = "software.amazon.awssdk.services.s3.model.S3Exception";

        // when
        Mockito.when(s3Client.deleteObject(Mockito.any(DeleteObjectRequest.class)))
                .thenThrow(S3Exception.class);

        final var aResult = Assertions.assertThrows(RuntimeException.class,
                () -> this.target.deleteFile(key));

        // then
        Assertions.assertEquals(expectedErrorMessage, aResult.getMessage());

        Mockito.verify(s3Client, Mockito.times(1)).deleteObject(Mockito.any(DeleteObjectRequest.class));
    }

    private PutObjectResponse buildPutObjectResponse() {
        return PutObjectResponse.builder()
                .eTag("test")
                .build();
    }

    private ListObjectsResponse buildListObjectsResponse() {
        return ListObjectsResponse.builder()
                .contents(List.of(S3Object.builder()
                        .key("test-test")
                        .build()))
                .prefix("test")
                .build();
    }

    private URL buildUrl() throws MalformedURLException {
        return new URL("http://localhost:8080/files/test-teste.png");
    }
}
