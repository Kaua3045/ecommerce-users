package com.kaua.ecommerce.users.infrastructure.exceptions;

import com.kaua.ecommerce.users.domain.exceptions.DomainException;

import java.util.Collections;

public class ImageSizeNotValidException extends DomainException {

    public ImageSizeNotValidException() {
        super("Maximum image size is 600kb", Collections.emptyList());
    }
}
