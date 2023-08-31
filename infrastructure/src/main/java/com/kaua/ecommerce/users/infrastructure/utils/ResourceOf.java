package com.kaua.ecommerce.users.infrastructure.utils;

import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageTypeNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public final class ResourceOf {

    private ResourceOf() {}

    public static Resource with(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        isValidImage(part);

        try {
            final var inputStream = part.getInputStream();
            return Resource.with(
                    inputStream,
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void isValidImage(final MultipartFile part) {
        final var imageTypes = List.of(
                "image/jpeg",
                "image/jpg",
                "image/png"
        );

        final var validImageType = imageTypes.contains(part.getContentType());
        final var IMAGE_SIZE = 600;
        final var validImageSize = part.getSize() > IMAGE_SIZE * 1024;

        if (!validImageType) {
            throw new ImageTypeNotValidException();
        }

        if (validImageSize) {
            throw new ImageSizeNotValidException();
        }
    }
}
