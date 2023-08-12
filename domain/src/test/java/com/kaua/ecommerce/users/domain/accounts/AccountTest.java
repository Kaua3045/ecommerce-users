package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void givenAValidValues_whenCallsNewAccount_thenAnAccountShouldBeCreated() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        // then
        Assertions.assertNotNull(aAccount.getId());
        Assertions.assertEquals(aFirstName, aAccount.getFirstName());
        Assertions.assertEquals(aLastName, aAccount.getLastName());
        Assertions.assertEquals(aEmail, aAccount.getEmail());
        Assertions.assertEquals(aPassword, aAccount.getPassword());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, aAccount.getMailStatus());
        Assertions.assertNull(aAccount.getAvatarUrl());
        Assertions.assertNotNull(aAccount.getCreatedAt());
        Assertions.assertNotNull(aAccount.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidFirstName_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var expectedErrorMessage = "'firstName' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidLastName_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final String aLastName = null;
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var expectedErrorMessage = "'lastName' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmail_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "";
        final var aPassword = "123456Ab";
        final var expectedErrorMessage = "'email' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPassword_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var expectedErrorMessage = "'password' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLength_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234";
        final var expectedErrorMessage = "'password' should be at least 8 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordButNotContainsLowerAndUpercaseLetter_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }
}
