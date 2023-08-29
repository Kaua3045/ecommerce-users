package com.kaua.ecommerce.users.domain.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ResourceTest {

    @Test
    void givenAValidParams_whenCallWithResource_shouldInstantiate() {
        // given
        final var aInputStream = InputStream.nullInputStream();
        final var aContentType = "image/png";
        final var aFileName = "image.png";

        // when
        final var actualResource = Resource.with(aInputStream, aContentType, aFileName);

        // then
        Assertions.assertNotNull(actualResource);
        Assertions.assertEquals(aInputStream, actualResource.inputStream());
        Assertions.assertEquals(aContentType, actualResource.contentType());
        Assertions.assertEquals(aFileName, actualResource.fileName());
    }

    @Test
    void givenAnInvalidNullInputStream_whenCallWithResource_shouldThrowException() {
        // given
        final InputStream aInputStream = null;
        final var aContentType = "image/png";
        final var aFileName = "image.png";

        final var expectedErrorMessage = "'inputStream' must not be null";

        // when
        final var actualException = Assertions.assertThrows(
                NullPointerException.class,
                () -> Resource.with(aInputStream, aContentType, aFileName)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAnInvalidNullContentType_whenCallWithResource_shouldThrowException() {
        // given
        final var aInputStream = InputStream.nullInputStream();
        final String aContentType = null;
        final var aFileName = "image.png";

        final var expectedErrorMessage = "'contentType' must not be null";

        // when
        final var actualException = Assertions.assertThrows(
                NullPointerException.class,
                () -> Resource.with(aInputStream, aContentType, aFileName)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAnInvalidNullFileName_whenCallWithResource_shouldThrowException() {
        // given
        final var aInputStream = InputStream.nullInputStream();
        final var aContentType = "image/png";
        final String aFileName = null;

        final var expectedErrorMessage = "'fileName' must not be null";

        // when
        final var actualException = Assertions.assertThrows(
                NullPointerException.class,
                () -> Resource.with(aInputStream, aContentType, aFileName)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
