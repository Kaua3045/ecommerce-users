package com.kaua.ecommerce.users.domain.exceptions;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.Validation;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThrowsValidationHandlerTest {

    @Test
    public void testAppendWithError() {
        Error error = new Error("Sample error");
        ThrowsValidationHandler handler = new ThrowsValidationHandler();

        Assertions.assertThrows(DomainException.class, () -> handler.append(error));
    }

    @Test
    public void testAppendWithValidationHandler() {
        ThrowsValidationHandler handler = new ThrowsValidationHandler();
        ValidationHandler anotherHandler = new ThrowsValidationHandler();

        Assertions.assertThrows(DomainException.class, () -> handler.append(anotherHandler));
    }

    @Test
    public void testValidateWithValidationSuccess() {
        Validation validation = () -> {};
        ThrowsValidationHandler handler = new ThrowsValidationHandler();

        Assertions.assertDoesNotThrow(() -> handler.validate(validation));
    }

    @Test
    public void testValidateWithValidationThrowsDomainException() {
        ThrowsValidationHandler handler = new ThrowsValidationHandler();

        Assertions.assertThrows(DomainException.class, () -> handler.validate(null));
    }

    @Test
    public void testGetErrors() {
        ThrowsValidationHandler handler = new ThrowsValidationHandler();

        Assertions.assertEquals(0, handler.getErrors().size());
    }
}
