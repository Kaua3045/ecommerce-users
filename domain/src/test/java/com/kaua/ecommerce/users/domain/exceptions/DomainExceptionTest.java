package com.kaua.ecommerce.users.domain.exceptions;

import com.kaua.ecommerce.users.domain.validation.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DomainExceptionTest {

    @Test
    void givenAValidListOfError_whenCallDomainExceptionWith_ThenReturnDomainException() {
        // given
        var errors = List.of(new Error("Common Error"));
        // when
        var domainException = DomainException.with(errors);
        // then
        Assertions.assertEquals(errors, domainException.getErrors());
    }
}
