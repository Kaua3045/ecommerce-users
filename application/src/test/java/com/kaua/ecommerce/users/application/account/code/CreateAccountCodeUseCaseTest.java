package com.kaua.ecommerce.users.application.account.code;

import com.kaua.ecommerce.users.application.gateways.AccountCodeGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateAccountCodeUseCaseTest {

    @Mock
    private AccountCodeGateway accountCodeGateway;

    @InjectMocks
    private DefaultCreateAccountCodeUseCase useCase;

    @Test
    public void givenAValidCommand_whenCallCreateCode_thenShouldCreateCode() {
        // given
        final var aCode = RandomStringUtils.generateValue(35);
        final var aCodeChallenge = RandomStringUtils.generateValue(100);
        final var aAccountID = UUID.randomUUID().toString();

        final var command = CreateAccountCodeCommand.with(
                aCode,
                aCodeChallenge,
                aAccountID
        );

        // when
        Mockito.when(accountCodeGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertEquals(aCode, aOutput.code());
        Assertions.assertEquals(aCodeChallenge, aOutput.codeChallenge());

        Mockito.verify(accountCodeGateway, Mockito.times(1))
                .create(Mockito.argThat(cmd ->
                        Objects.equals(aCode, cmd.getCode()) &&
                Objects.equals(aCodeChallenge, cmd.getCodeChallenge()) &&
                Objects.equals(aAccountID, cmd.getAccountID().getValue())));
    }

    @Test
    public void givenAnInvalidCodeBlank_whenCallCreateAccountCode_thenShouldReturnAnError() {
        // given
        final var aCode = "";
        final var aCodeChallenge = RandomStringUtils.generateValue(100);
        final var aAccountID = UUID.randomUUID().toString();
        final var expectedErrorMessage = "'code' should not be null or blank";
        final var expectedErrorCount = 1;

        final var command = CreateAccountCodeCommand.with(
                aCode,
                aCodeChallenge,
                aAccountID
        );

        // when
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());

        Mockito.verify(accountCodeGateway, Mockito.times(0))
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidCodeNull_whenCallCreateAccountCode_thenShouldReturnAnError() {
        // given
        final String aCode = null;
        final var aCodeChallenge = RandomStringUtils.generateValue(100);
        final var aAccountID = UUID.randomUUID().toString();
        final var expectedErrorMessage = "'code' should not be null or blank";
        final var expectedErrorCount = 1;

        final var command = CreateAccountCodeCommand.with(
                aCode,
                aCodeChallenge,
                aAccountID
        );

        // when
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());

        Mockito.verify(accountCodeGateway, Mockito.times(0))
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidCodeChallengeBlank_whenCallCreateAccountCode_thenShouldReturnAnError() {
        // given
        final var aCode = RandomStringUtils.generateValue(36);
        final var aCodeChallenge = "";
        final var aAccountID = UUID.randomUUID().toString();
        final var expectedErrorMessage = "'codeChallenge' should not be null or blank";
        final var expectedErrorCount = 1;

        final var command = CreateAccountCodeCommand.with(
                aCode,
                aCodeChallenge,
                aAccountID
        );

        // when
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());

        Mockito.verify(accountCodeGateway, Mockito.times(0))
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidCodeChallengeNull_whenCallCreateAccountCode_thenShouldReturnAnError() {
        // given
        final var aCode = RandomStringUtils.generateValue(36);
        final String aCodeChallenge = null;
        final var aAccountID = UUID.randomUUID().toString();
        final var expectedErrorMessage = "'codeChallenge' should not be null or blank";
        final var expectedErrorCount = 1;

        final var command = CreateAccountCodeCommand.with(
                aCode,
                aCodeChallenge,
                aAccountID
        );

        // when
        final var aException = Assertions.assertThrows(DomainException.class, () ->
                useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, aException.getErrors().size());

        Mockito.verify(accountCodeGateway, Mockito.times(0))
                .create(Mockito.any());
    }
}
