package com.kaua.ecommerce.users.domain.exceptions;

import com.kaua.ecommerce.users.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<Error> errors;

    private DomainException(final List<Error> aErrors) {
        super("DomainException");
        this.errors = aErrors;
    }

    public static DomainException with(final Error aError) {
        return new DomainException(List.of(aError));
    }

    public static DomainException with(final List<Error> aErrors) {
        return new DomainException(aErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
