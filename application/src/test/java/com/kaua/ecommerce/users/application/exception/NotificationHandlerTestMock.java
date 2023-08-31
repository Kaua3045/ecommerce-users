package com.kaua.ecommerce.users.application.exception;

import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.Validation;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NotificationHandlerTestMock {

    @Test
    void givenValidate_whenCallValidate_thenShouldReturnDomainException() {
        final var expectedErrorMessage = "Simulated error";

        final var validation = Mockito.mock(Validation.class);
        Mockito.doThrow(DomainException.with(new Error(expectedErrorMessage)))
                .when(validation)
                .validate();

        final var handler = NotificationHandler.create();

        handler.validate(validation);

        Assertions.assertTrue(handler.hasError());
        Assertions.assertEquals(1, handler.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, handler.getErrors().get(0).message());
    }
}
