package com.kaua.ecommerce.users.infrastructure.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SendEventExceptionTest {

    @Test
    public void givenAValidErrorMessage_whenSendEventExceptionIsCalled_thenASendEventExceptionShouldBeThrown() {
        // Arrange
        final var aErrorMessage = "Error sending event to QUEUE";

        // Act
        final var aSendEventException = new SendEventException(aErrorMessage);

        // Assert
        Assertions.assertEquals(aErrorMessage, aSendEventException.getMessage());
    }
}
