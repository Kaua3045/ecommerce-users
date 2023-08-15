package com.kaua.ecommerce.users.domain.exceptions;

import com.kaua.ecommerce.users.domain.accounts.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest {

    @Test
    public void givenAValidAggregate_whenCallNotFoundExceptionWith_ThenReturnNotFoundException() {
        // given
        final var aggregate = Account.class;
        final var aId = "123";
        final var expectedErrorMessage = "Account with id 123 was not found";

        // when
        final var notFoundException = NotFoundException.with(aggregate, aId);
        // then
        Assertions.assertEquals(expectedErrorMessage, notFoundException.getMessage());
    }
}
