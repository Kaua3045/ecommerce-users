package com.kaua.ecommerce.users.domain.accounts.code;

import com.kaua.ecommerce.users.domain.TestValidationHandler;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class AccountCodeTest {

    @Test
    public void givenAValidValues_whenCallNewAccountCode_thenShouldCreateANewAccountCode() {
        // Given
        final var code = generateCode();
        final var codeChallenge = RandomStringUtils.generateValue(100);
        final var accountID = AccountID.unique();

        // When
        final var accountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);

        // Then
        Assertions.assertDoesNotThrow(() -> accountCode.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(accountCode);
        Assertions.assertEquals(code, accountCode.getCode());
        Assertions.assertEquals(codeChallenge, accountCode.getCodeChallenge());
        Assertions.assertEquals(accountID, accountCode.getAccountID());
        Assertions.assertNotNull(accountCode.getCreatedAt());
        Assertions.assertNotNull(accountCode.getUpdatedAt());

        Assertions.assertTrue(accountCode.getId().equals(accountCode.getId()));
        Assertions.assertFalse(accountCode.getId().equals(AccountID.unique()));
        Assertions.assertFalse(accountCode.getId().equals(null));
        Assertions.assertFalse(accountCode.getId().equals(new Object()));
        Assertions.assertNotNull(accountCode.getId().hashCode());
    }

    @Test
    public void givenAInvalidCodeBlank_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final var code = "";
        final var codeChallenge = RandomStringUtils.generateValue(100);
        final var accountID = AccountID.unique();
        final var expectedErrorMessage = "'code' should not be null or blank";

        // When
        final var aAccountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                aAccountCode.validate(new ThrowsValidationHandler()));

        // Then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidCodeNull_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final String code = null;
        final var codeChallenge = RandomStringUtils.generateValue(100);
        final var accountID = AccountID.unique();
        final var expectedErrorMessage = "'code' should not be null or blank";

        // When
        final var aAccountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                aAccountCode.validate(new ThrowsValidationHandler()));

        // Then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidCodeLengthMoreThan36_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final var code = generateCode()+"1234567890123456789012345678901234567890";
        final var codeChallenge = RandomStringUtils.generateValue(100);
        final var accountID = AccountID.unique();
        final var expectedErrorMessage = "'code' must be less than 36 characters";

        // When
        final var aAccountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                aAccountCode.validate(new ThrowsValidationHandler()));

        // Then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidCodeChallengeBlank_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final var code = generateCode();
        final var codeChallenge = "";
        final var accountID = AccountID.unique();
        final var expectedErrorMessage = "'codeChallenge' should not be null or blank";

        // When
        final var aAccountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                aAccountCode.validate(new ThrowsValidationHandler()));

        // Then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidCodeChallengeNull_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final var code = generateCode();
        final String codeChallenge = null;
        final var accountID = AccountID.unique();
        final var expectedErrorMessage = "'codeChallenge' should not be null or blank";

        // When
        final var aAccountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                aAccountCode.validate(new ThrowsValidationHandler()));

        // Then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenValidValues_whenCalledWithInAccountCode_shouldReturnAnAccountCodeObjectWithDataEqualToWhatWasPassed() {
        // given
        final var aCode = "123456";
        final var aCodeChallenge = "12313241242412";
        final var aAccountId = AccountID.unique();

        // when
        final var aAccountCode = AccountCode.newAccountCode(
                aCode,
                aCodeChallenge,
                aAccountId
        );

        final var aAccountCodeCloned = AccountCode.with(
                aAccountCode.getId().getValue(),
                aAccountCode.getCode(),
                aAccountCode.getCodeChallenge(),
                aAccountCode.getAccountID(),
                aAccountCode.getCreatedAt(),
                aAccountCode.getUpdatedAt()
        );

        // then
        Assertions.assertEquals(aAccountCode.getId().getValue(), aAccountCodeCloned.getId().getValue());
        Assertions.assertEquals(aAccountCode.getCode(), aAccountCodeCloned.getCode());
        Assertions.assertEquals(aAccountCode.getCodeChallenge(), aAccountCodeCloned.getCodeChallenge());
        Assertions.assertEquals(aAccountCode.getAccountID().getValue(), aAccountCodeCloned.getAccountID().getValue());
        Assertions.assertEquals(aAccountCode.getCreatedAt(), aAccountCodeCloned.getCreatedAt());
        Assertions.assertEquals(aAccountCode.getUpdatedAt(), aAccountCodeCloned.getUpdatedAt());
    }

    @Test
    public void givenTwoAccountCodeId_whenCallEquals_shouldReturnTrue() {
        final var aAccountCodeId1 = AccountCodeID.from("1234");
        final var aAccountCodeId2 = AccountCodeID.from("1234");

        Assertions.assertTrue(aAccountCodeId1.equals(aAccountCodeId2));
    }

    @Test
    public void givenAnInvalidCodeNull_whenCallValidate_shouldReturnAnError() {
        // given
        final var expectedErrorMessage = "'code' should not be null or blank";
        final var aAccountCode = AccountCode.newAccountCode(
                null,
                RandomStringUtils.generateValue(50),
                AccountID.unique()
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountCodeValidator = new AccountCodeValidator(aAccountCode, aTestValidationHandler);

        // when
        aAccountCodeValidator.validate();

        // then
        Assertions.assertTrue(aTestValidationHandler.hasError());
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCodeLenghtMoreThan36_whenCallValidate_shouldReturnAnError() {
        // given
        final var expectedErrorMessage = "'code' must be less than 36 characters";
        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(40),
                RandomStringUtils.generateValue(50),
                AccountID.unique()
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountCodeValidator = new AccountCodeValidator(aAccountCode, aTestValidationHandler);

        // when
        aAccountCodeValidator.validate();

        // then
        Assertions.assertTrue(aTestValidationHandler.hasError());
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCodeChallengeNull_whenCallValidate_shouldReturnAnError() {
        // given
        final var expectedErrorMessage = "'codeChallenge' should not be null or blank";
        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                null,
                AccountID.unique()
        );

        final var aTestValidationHandler = new TestValidationHandler();
        final var aAccountCodeValidator = new AccountCodeValidator(aAccountCode, aTestValidationHandler);

        // when
        aAccountCodeValidator.validate();

        // then
        Assertions.assertTrue(aTestValidationHandler.hasError());
        Assertions.assertEquals(expectedErrorMessage, aTestValidationHandler.getErrors().get(0).message());
    }

    private String generateCode() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
