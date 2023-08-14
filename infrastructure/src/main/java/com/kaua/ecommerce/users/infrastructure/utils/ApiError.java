package com.kaua.ecommerce.users.infrastructure.utils;

import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.validation.Error;

import java.util.List;

public record ApiError(String message, List<Error> errors) {

    public static ApiError from(final DomainException exception) {
        return new ApiError("DomainException", exception.getErrors());
    }
}
