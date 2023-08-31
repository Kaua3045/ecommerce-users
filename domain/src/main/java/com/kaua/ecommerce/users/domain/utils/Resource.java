package com.kaua.ecommerce.users.domain.utils;

import java.io.InputStream;
import java.util.Objects;

public class Resource {

    private final InputStream inputStream;
    private final String contentType;
    private final String fileName;

    private Resource(final InputStream inputStream, final String contentType, final String fileName) {
        this.inputStream = Objects.requireNonNull(inputStream, "'inputStream' must not be null");
        this.contentType = Objects.requireNonNull(contentType, "'contentType' must not be null");
        this.fileName = Objects.requireNonNull(fileName, "'fileName' must not be null");
    }

    public static Resource with(
            final InputStream inputStream,
            final String contentType,
            final String fileName
    ) {
        return new Resource(inputStream, contentType, fileName);
    }

    public InputStream inputStream() {
        return inputStream;
    }

    public String contentType() {
        return contentType;
    }

    public String fileName() {
        return fileName;
    }
}
