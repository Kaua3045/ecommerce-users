package com.kaua.ecommerce.users.infrastructure.exceptions;

import com.kaua.ecommerce.users.domain.exceptions.NoStackTraceException;

public class SecretManagerException extends NoStackTraceException {

    public SecretManagerException(String message) {
        super(message);
    }
}
