package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    void givenAValidValues_whenCallsNewAccount_thenAnAccountShouldBeCreated() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
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
        Assertions.assertEquals(aRole, aAccount.getRole());
        Assertions.assertNotNull(aAccount.getCreatedAt());
        Assertions.assertNotNull(aAccount.getUpdatedAt());

        Assertions.assertEquals(aEvent, aAccount.getDomainEvents().get(0));
        Assertions.assertEquals(expectedEventCount, aAccount.getDomainEvents().size());

        Assertions.assertEquals(aAccount.getId(), aAccount.getId());
        Assertions.assertNotEquals(AccountID.unique(), aAccount.getId());
        Assertions.assertNotNull(aAccount.getId());
        Assertions.assertFalse(aAccount.getId().equals(null));
        Assertions.assertFalse(aAccount.getId().equals(new Object()));
        Assertions.assertNotEquals(1201212, aAccount.getId().hashCode());
    }

    @Test
    void givenAnInvalidFirstNameBlank_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'firstName' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidFirstNameNull_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final String aFirstName = null;
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'firstName' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidFirstNameLengthLessThan3_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Ka ";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidFirstNameLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = RandomStringUtils.generateValue(256);
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidLastNameBlank_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'lastName' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidLastNameNull_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final String aLastName = null;
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'lastName' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidLastNameLengthLessThan3_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pe ";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidLastNameLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = RandomStringUtils.generateValue(256);
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmailBlank_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "";
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'email' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmailNull_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final String aEmail = null;
        final var aPassword = "123456Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'email' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordNull_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordBlank_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordLengthLessThan8_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = RandomStringUtils.generateValue(256);
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordButNotContainsLowerAndUpercaseLetter_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        // then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAValidPassword_whenCallsChangePassword_thenAnAccountShouldBeChangePassword() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*",
                aRole
        );

        final var aAccountUpdatedAt = aAccount.getUpdatedAt();

        final var aAccountUpdated = aAccount.changePassword(aPassword);

        //then
        Assertions.assertDoesNotThrow(() -> aAccountUpdated.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(aAccount.getId().getValue(), aAccountUpdated.getId().getValue());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountUpdated.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountUpdated.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountUpdated.getEmail());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, aAccountUpdated.getMailStatus());
        Assertions.assertEquals(aPassword, aAccountUpdated.getPassword());
        Assertions.assertNull(aAccountUpdated.getAvatarUrl());
        Assertions.assertEquals(aRole, aAccountUpdated.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountUpdated.getCreatedAt());
        Assertions.assertTrue(aAccountUpdated.getUpdatedAt().isAfter(aAccountUpdatedAt));
    }

    @Test
    void givenAValidAvatarUrl_whenCallsChangeAvatarUrl_thenAnAccountShouldBeChangeAvatarUrl() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aAvatarUrl = "http://localhost:8080/files/avatar.png";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aAccountUpdatedAt = aAccount.getUpdatedAt();

        final var aAccountUpdated = aAccount.changeAvatarUrl(aAvatarUrl);

        //then
        Assertions.assertDoesNotThrow(() -> aAccountUpdated.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(aAccount.getId().getValue(), aAccountUpdated.getId().getValue());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountUpdated.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountUpdated.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountUpdated.getEmail());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, aAccountUpdated.getMailStatus());
        Assertions.assertEquals(aPassword, aAccountUpdated.getPassword());
        Assertions.assertEquals(aAvatarUrl, aAccountUpdated.getAvatarUrl());
        Assertions.assertEquals(aRole, aAccountUpdated.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountUpdated.getCreatedAt());
        Assertions.assertTrue(aAccountUpdated.getUpdatedAt().isAfter(aAccountUpdatedAt));
    }

    @Test
    void givenAValidRole_whenCallsChangeRole_thenAnAccountShouldBeChangeRole() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                Role.newRole("User", "common user", RoleTypes.COMMON, true)
        );

        final var aAccountUpdatedAt = aAccount.getUpdatedAt();

        final var aAccountUpdated = aAccount.changeRole(aRole);

        //then
        Assertions.assertDoesNotThrow(() -> aAccountUpdated.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(aAccount.getId().getValue(), aAccountUpdated.getId().getValue());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountUpdated.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountUpdated.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountUpdated.getEmail());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, aAccountUpdated.getMailStatus());
        Assertions.assertEquals(aAccount.getPassword(), aAccountUpdated.getPassword());
        Assertions.assertNull(aAccountUpdated.getAvatarUrl());
        Assertions.assertEquals(aRole, aAccountUpdated.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountUpdated.getCreatedAt());
        Assertions.assertTrue(aAccountUpdated.getUpdatedAt().isAfter(aAccountUpdatedAt));
    }

    @Test
    void givenAnInvalidPassword_whenCallsChangePassword_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final String aPassword = null;
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' should not be null or blank";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*",
                aRole
        );

        final var aAccountUpdated = aAccount.changePassword(aPassword);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordLengthLessThan8_whenCallsChangePassword_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*",
                aRole
        );

        final var aAccountUpdated = aAccount.changePassword(aPassword);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordLengthMoreThan255_whenCallsChangePassword_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = RandomStringUtils.generateValue(256);
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*",
                aRole
        );

        final var aAccountUpdated = aAccount.changePassword(aPassword);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidPasswordButNotContainsLowerAndUpercaseLetter_whenCallsChangePassword_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                "87654321Ab*",
                aRole
        );

        final var aAccountUpdated = aAccount.changePassword(aPassword);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidRole_whenCallsChangeRole_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "87654321Ab*";
        final Role aRole = null;
        final var expectedErrorMessage = "'role' should not be null";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false)
        );

        final var aAccountUpdated = aAccount.changeRole(aRole);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccountUpdated, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenValidValues_whenCalledWithInAccount_shouldReturnAnAccountObjectWithDataEqualToWhatWasPassed() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Teste";
        final var aEmail = "test@teste.com";
        final var aPassword = "1234567Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
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
                aAccount.getRole(),
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
        Assertions.assertEquals(aAccount.getRole(), aAccountCloned.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountCloned.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), aAccountCloned.getUpdatedAt());

        Assertions.assertEquals(aEvent, aAccountCloned.getDomainEvents().get(0));
        Assertions.assertEquals(expectedEventCount, aAccountCloned.getDomainEvents().size());
    }

    @Test
    void givenAValidAccountWatingConfirmation_whenCallsConfirm_thenAnAccountShouldBeConfirmed() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
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
        Assertions.assertEquals(aRole, aAccountUpdated.getRole());
        Assertions.assertEquals(aAccount.getCreatedAt(), aAccountUpdated.getCreatedAt());
        Assertions.assertTrue(aAccountUpdated.getUpdatedAt().isAfter(aAccountUpdatedAt));
    }

    @Test
    void givenAValidId_whenCallNewAccountDeletedEvent_shouldReturnAnAccountDeletedEvent() {
        // given
        final var aId = AccountID.unique();

        // when
        final var aAccountDeletedEvent = new AccountDeletedEvent(aId.getValue());

        // then
        Assertions.assertEquals(aId.getValue(), aAccountDeletedEvent.id());
        Assertions.assertNotNull(aAccountDeletedEvent.occurredOn());
    }

    @Test
    void givenAnInvalidRoleId_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var expectedErrorMessage = "'role' should not be null";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                null
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmailFormatWithoutAtSign_whenCallNewAccount_shouldReturnAnDomainException() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "email_sem_arroba.com";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'email' must be a valid email address";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmailFormatWithoutDotInFinal_whenCallNewAccount_shouldReturnAnDomainException() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "email_com_arroba@aaaa";
        final var aPassword = "12345678Ab";
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var expectedErrorMessage = "'email' must be a valid email address";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword,
                aRole
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountValidator = new AccountValidator(aAccount, aTestValidationHandler);

        aAccountValidator.validate();

        //then
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }
}
