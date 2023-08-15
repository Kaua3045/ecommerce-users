package com.kaua.ecommerce.users.domain.accounts.code;

import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

public class AccountCodeTest {

    @Test
    public void givenAValidValues_whenCallNewAccountCode_thenShouldCreateANewAccountCode() {
        // Given
        final var code = generateCode();
        final var codeChallenge = generateCodeChallenge(100);
        final var accountID = AccountID.unique();

        // When
        final var accountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);

        // Then
        Assertions.assertNotNull(accountCode);
        Assertions.assertEquals(code, accountCode.getCode());
        Assertions.assertEquals(codeChallenge, accountCode.getCodeChallenge());
        Assertions.assertEquals(accountID, accountCode.getAccountID());
        Assertions.assertNotNull(accountCode.getCreatedAt());
        Assertions.assertNotNull(accountCode.getUpdatedAt());
    }

    @Test
    public void givenAInvalidCodeBlank_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final var code = "";
        final var codeChallenge = generateCodeChallenge(100);
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
        final var codeChallenge = generateCodeChallenge(100);
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
        final var codeChallenge = generateCodeChallenge(100);
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
    public void givenAInvalidCodeChallengeLengthMoreThan100_whenCallNewAccountCode_thenShouldThrowAnException() {
        // Given
        final var code = generateCode();
        final var codeChallenge = generateCodeChallenge(120);
        final var accountID = AccountID.unique();
        final var expectedErrorMessage = "'codeChallenge' must be less than 100 characters";

        // When
        final var aAccountCode = AccountCode.newAccountCode(code, codeChallenge, accountID);
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                aAccountCode.validate(new ThrowsValidationHandler()));

        // Then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    private String generateCode() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }

    private String generateCodeChallenge(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
