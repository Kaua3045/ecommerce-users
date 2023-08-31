package com.kaua.ecommerce.users.infrastructure.utils;

import com.kaua.ecommerce.users.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageTypeNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

public class ResourceOfTest {

    @Test
    void givenAnInvalidMultipartFile_whenCallWith_thenReturnNull() {
        final MultipartFile multipartFile = null;

        final var aResult = ResourceOf.with(multipartFile);

        Assertions.assertNull(aResult);
    }

    @Test
    void givenAnInvalidMultipartFile_whenCallWith_thenThrowImageTypeNotValidException() {
        final var mockPart = Mockito.mock(MultipartFile.class);

        Mockito.when(mockPart.getContentType()).thenReturn("image/gif");

        Assertions.assertThrows(
                ImageTypeNotValidException.class,
                () -> ResourceOf.with(mockPart)
        );
    }

    @Test
    void givenAnInvalidMultipartFile_whenCallWith_thenThrowImageSizeNotValidException() {
        final var mockPart = Mockito.mock(MultipartFile.class);

        Mockito.when(mockPart.getContentType()).thenReturn("image/jpeg");
        Mockito.when(mockPart.getSize()).thenReturn(600 * 1024L + 1);

        Assertions.assertThrows(
                ImageSizeNotValidException.class,
                () -> ResourceOf.with(mockPart)
        );
    }
}
