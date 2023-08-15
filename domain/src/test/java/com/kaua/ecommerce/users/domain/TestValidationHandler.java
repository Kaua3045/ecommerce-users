package com.kaua.ecommerce.users.domain;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.Validation;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class TestValidationHandler implements ValidationHandler {

    private final List<Error> errors;

    public TestValidationHandler() {
        this.errors = new ArrayList<>();
    }

    @Override
    public ValidationHandler append(Error aError) {
        errors.add(aError);
        return this;
    }

    @Override
    public ValidationHandler append(ValidationHandler aHandler) {
        errors.addAll(aHandler.getErrors());
        return this;
    }

    @Override
    public ValidationHandler validate(Validation aValidation) {
        aValidation.validate();
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return errors;
    }
}
