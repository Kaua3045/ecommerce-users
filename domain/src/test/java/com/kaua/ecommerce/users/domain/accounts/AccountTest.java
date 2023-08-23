package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
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

        final var aEvent = new AccountCreatedEvent(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail()
        );
        final var expectedEventCount = 1;

        aAccount.registerEvent(aEvent);

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

        Assertions.assertEquals(aEvent, aAccount.getDomainEvents().get(0));
        Assertions.assertEquals(expectedEventCount, aAccount.getDomainEvents().size());

        Assertions.assertTrue(aAccount.getId().equals(aAccount.getId()));
        Assertions.assertFalse(aAccount.getId().equals(AccountID.unique()));
        Assertions.assertFalse(aAccount.getId().equals(null));
        Assertions.assertNotNull(aAccount.getId().hashCode());
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

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidFirstNameLengthLessThan3_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Ka ";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidFirstNameLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = RandomStringUtils.generateValue(256);
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
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

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidLastNameLengthLessThan3_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pe ";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidLastNameLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = RandomStringUtils.generateValue(256);
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
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

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
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

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthLessThan8_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = RandomStringUtils.generateValue(256);
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
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

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAValidValues_whenCallsUpdateAccount_thenAnAccountShouldBeUpdated() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aAvatarUrl = "http://teste.com/avatar.png";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*"
        );

        final var aAccountUpdatedAt = aAccount.getUpdatedAt();

        final var aAccountUpdated = aAccount.update(
                aPassword,
                aAvatarUrl
        );

        //then
        Assertions.assertDoesNotThrow(() -> aAccountUpdated.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(aAccount.getId().getValue(), aAccountUpdated.getId().getValue());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountUpdated.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountUpdated.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountUpdated.getEmail());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, aAccountUpdated.getMailStatus());
        Assertions.assertEquals(aPassword, aAccountUpdated.getPassword());
        Assertions.assertEquals(aAvatarUrl, aAccountUpdated.getAvatarUrl());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountUpdated.getCreatedAt());
        Assertions.assertTrue(aAccountUpdated.getUpdatedAt().isAfter(aAccountUpdatedAt));
    }

    @Test
    public void givenAnInvalidPassword_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var aAvatarUrl = "http://teste.com/avatar.png";
        final var expectedErrorMessage = "'password' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*"
        );

        final var aAccountUpdated = aAccount.update(
                aPassword,
                aAvatarUrl
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthLessThan8_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345";
        final var aAvatarUrl = "http://teste.com/avatar.png";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*"
        );

        final var aAccountUpdated = aAccount.update(
                aPassword,
                aAvatarUrl
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthMoreThan255_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = RandomStringUtils.generateValue(256);
        final var aAvatarUrl = "http://teste.com/avatar.png";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*"
        );

        final var aAccountUpdated = aAccount.update(
                aPassword,
                aAvatarUrl
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordButNotContainsLowerAndUpercaseLetter_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var aAvatarUrl = "http://teste.com/avatar.png";
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*"
        );

        final var aAccountUpdated = aAccount.update(
                aPassword,
                aAvatarUrl
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenValidValues_whenCalledWithInAccount_shouldReturnAnAccountObjectWithDataEqualToWhatWasPassed() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Teste";
        final var aEmail = "test@teste.com";
        final var aPassword = "1234567Ab";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aEvent = new AccountCreatedEvent(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail()
        );
        final var expectedEventCount = 1;

        aAccount.registerEvent(aEvent);

        final var aAccountCloned = Account.with(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail(),
                aAccount.getMailStatus(),
                aAccount.getPassword(),
                aAccount.getAvatarUrl(),
                aAccount.getCreatedAt(),
                aAccount.getUpdatedAt(),
                aAccount.getDomainEvents()
        );

        // then
        Assertions.assertEquals(aAccount.getId().getValue(), aAccountCloned.getId().getValue());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountCloned.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountCloned.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountCloned.getEmail());
        Assertions.assertEquals(aAccount.getMailStatus(), aAccountCloned.getMailStatus());
        Assertions.assertEquals(aAccount.getPassword(), aAccountCloned.getPassword());
        Assertions.assertEquals(aAccount.getAvatarUrl(), aAccountCloned.getAvatarUrl());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountCloned.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), aAccountCloned.getUpdatedAt());

        Assertions.assertEquals(aEvent, aAccountCloned.getDomainEvents().get(0));
        Assertions.assertEquals(expectedEventCount, aAccountCloned.getDomainEvents().size());
    }

    @Test
    public void givenAValidAccountWatingConfirmation_whenCallsConfirm_thenAnAccountShouldBeConfirmed() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        final var aAccountUpdatedAt = aAccount.getUpdatedAt();
        final var aAccountUpdated = aAccount.confirm();

        //then
        Assertions.assertDoesNotThrow(() -> aAccountUpdated.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(aAccount.getId().getValue(), aAccountUpdated.getId().getValue());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountUpdated.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountUpdated.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountUpdated.getEmail());
        Assertions.assertEquals(AccountMailStatus.CONFIRMED, aAccountUpdated.getMailStatus());
        Assertions.assertEquals(aPassword, aAccountUpdated.getPassword());
        Assertions.assertNull(aAccountUpdated.getAvatarUrl());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountUpdated.getCreatedAt());
        Assertions.assertTrue(aAccountUpdated.getUpdatedAt().isAfter(aAccountUpdatedAt));
    }
}
