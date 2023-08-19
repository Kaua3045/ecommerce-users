package com.kaua.ecommerce.users.domain.accounts.mail;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class AccountMailTest {

    @Test
    public void givenAValidValues_whenCallNewAccountMail_thenAnAccountShouldBeCreated() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        Assertions.assertDoesNotThrow(() -> aAccountMail.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(aAccountMail.getId());
        Assertions.assertEquals(aToken, aAccountMail.getToken());
        Assertions.assertEquals(aType, aAccountMail.getType());
        Assertions.assertEquals(aAccount, aAccountMail.getAccount());
        Assertions.assertEquals(aExpiresAt, aAccountMail.getExpiresAt());
        Assertions.assertNotNull(aAccountMail.getCreatedAt());
        Assertions.assertNotNull(aAccountMail.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidTokenNull_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final String aToken = null;
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'token' should not be null or blank";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidTokenBlank_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = "";
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'token' should not be null or blank";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidTokenLenghtMoreThan36_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = RandomStringUtils.generateValue(37);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'token' should not be greater than 36";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidTypeNull_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = RandomStringUtils.generateValue(36);
        final AccountMailType aType = null;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'type' should not be null";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidAccountNull_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final Account aAccount = null;
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'account' should not be null";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAValidAccountButAfterSetNull_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab"
        );
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'account' should not be null";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);
        aAccountMail.setAccount(null);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidExpirestAtNull_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final Instant aExpiresAt = null;
        final var expectedErrorMessage = "'expiresAt' should not be null";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidExpirestAtBeforeNow_whenCallNewAccountMail_thenAnExceptionShouldBeThrown() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().minus(10, ChronoUnit.MINUTES);
        final var expectedErrorMessage = "'expiresAt' should not be before now";

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountMailValidator = new AccountMailValidator(aAccountMail, aTestValidationHandler);

        aAccountMailValidator.validate();

        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAValidValues_whenCallHashCodeAndEqualsAndFrom_thenShouldBeTrue() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);
        final var aAccountMailIdFrom = AccountMailID.from("1234");

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        Assertions.assertTrue(aAccountMail.getId().equals(aAccountMail.getId()));
        Assertions.assertNotNull(aAccountMail.getId().hashCode());

        Assertions.assertNotNull(aAccountMailIdFrom);
        Assertions.assertInstanceOf(AccountMailID.class, aAccountMailIdFrom);
    }

    @Test
    public void givenAInvalidValues_whenCallHashCodeAndEqualsAndFrom_thenShouldBeFalse() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        Assertions.assertFalse(aAccountMail.getId().equals(AccountMailID.unique()));
        Assertions.assertFalse(aAccountMail.getId().equals(null));
        Assertions.assertFalse(aAccountMail.getId().equals(new Object()));
    }

    @Test
    public void givenValidValues_whenCalledWithInAccountMail_shouldReturnAnAccountMailObjectWithDataEqualToWhatWasPassed() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Test",
                "Testando",
                "teste@teste.com",
                "123456Ab");
        final var aExpiresAt = InstantUtils.now().plus(10, ChronoUnit.MINUTES);

        final var aAccountMail = AccountMail.newAccountMail(aToken, aType, aAccount, aExpiresAt);

        final var aAccountMailCloned = AccountMail.with(
                aAccountMail.getId().getValue(),
                aAccountMail.getToken(),
                aAccountMail.getType(),
                aAccountMail.getAccount(),
                aAccountMail.getExpiresAt(),
                aAccountMail.getCreatedAt(),
                aAccountMail.getUpdatedAt()
        );

        // then
        Assertions.assertEquals(aAccountMail.getId().getValue(), aAccountMailCloned.getId().getValue());
        Assertions.assertEquals(aAccountMail.getToken(), aAccountMailCloned.getToken());
        Assertions.assertEquals(aAccountMail.getType(), aAccountMailCloned.getType());
        Assertions.assertEquals(aAccountMail.getAccount(), aAccountMailCloned.getAccount());
        Assertions.assertEquals(aAccountMail.getExpiresAt(), aAccountMailCloned.getExpiresAt());
        Assertions.assertEquals(aAccountMail.getCreatedAt(), aAccountMailCloned.getCreatedAt());
        Assertions.assertEquals(aAccountMail.getUpdatedAt(), aAccountMailCloned.getUpdatedAt());
    }
}
