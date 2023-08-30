package com.kaua.ecommerce.users.infrastructure.service.local;

import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class InMemoryStorageServiceTest {

    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    void cleanUp() {
        this.target.clear();
    }

    @Test
    void givenValidResource_whenCallUploadFile_thenShouldUploadFile() {
        // given
        final var aFileName = "avatar";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                aFileName
        );
        final var aKey = IdUtils.generate();
        final var expectedUrl = "http://localhost:8080/files/" + aKey + "-" + aFileName + ".png";

        // when
        final var aResult = this.target.uploadFile(aKey, aResource);

        // then
        Assertions.assertEquals(expectedUrl, aResult);
    }

    @Test
    void givenValidKey_whenCallDeleteFile_thenShouldDeleteFile() {
        // given
        final var aFileName = "avatar";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                aFileName
        );
        final var aKey = IdUtils.generate();
        this.target.uploadFile(aKey, aResource);

        // when
        this.target.deleteFile(aKey + "-avatar");

        // then
        Assertions.assertTrue(this.target.getFileUrl(aKey + "-avatar").isEmpty());
    }

    @Test
    void givenValidKey_whenCallGetFileUrl_thenShouldReturnFileUrl() {
        // given
        final var aFileName = "avatar";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                aFileName
        );
        final var aKey = IdUtils.generate();
        final var expectedUrl = "http://localhost:8080/files/" + aKey + "-" + aFileName + ".png";
        this.target.uploadFile(aKey, aResource);

        // when
        final var aUrl = this.target.getFileUrl(aKey + "-avatar").get();
        // then
        Assertions.assertEquals(expectedUrl, aUrl);
    }

    @Test
    void givenAnInvalid_whenCallGetFileUrl_thenShouldReturnEmpty() {
        // given
        final var aKey = IdUtils.generate();

        // when
        final var aUrl = this.target.getFileUrl(aKey + "-avatar");
        // then
        Assertions.assertTrue(aUrl.isEmpty());
    }

    @Test
    void givenValidPrefix_whenCallDeleteFileByPrefix_thenShouldDeleteFile() {
        // given
        final var aFileName = "avatar";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                aFileName
        );
        final var aKey = IdUtils.generate();
        this.target.uploadFile(aKey, aResource);

        // when
        this.target.deleteFileByPrefix(aKey + "-avatar");

        // then
        Assertions.assertTrue(this.target.getFileUrl(aKey + "-avatar").isEmpty());
    }
}
