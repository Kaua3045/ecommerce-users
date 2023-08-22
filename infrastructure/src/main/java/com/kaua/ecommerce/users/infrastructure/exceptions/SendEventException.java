package com.kaua.ecommerce.users.infrastructure.exceptions;

import com.kaua.ecommerce.users.domain.exceptions.NoStackTraceException;

public class SendEventException extends NoStackTraceException {

    public SendEventException(String message) {
        super(message);
    }
}
