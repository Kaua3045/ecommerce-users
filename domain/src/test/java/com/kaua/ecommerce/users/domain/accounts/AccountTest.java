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

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidFirstNameLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = """
                O empenho em analisar a execução dos pontos do programa causa impacto indireto na
                reavaliação dos modos de operação convencionais. Podemos já vislumbrar o modo pelo 
                qual a constante divulgação das informações talvez venha a ressaltar a relatividade 
                de alternativas às soluções ortodoxas. Assim mesmo, a contínua expansão de nossa 
                atividade assume importantes posições no estabelecimento das condições inegavelmente 
                apropriadas. No entanto, não podemos esquecer que a revolução dos costumes auxilia 
                a preparação e a composição das novas proposições. As experiências acumuladas 
                demonstram que o novo modelo estrutural aqui preconizado acarreta um processo 
                de reformulação e modernização do processo de comunicação como um todo.
                """;
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

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidLastNameLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = """
                O empenho em analisar a execução dos pontos do programa causa impacto indireto na
                reavaliação dos modos de operação convencionais. Podemos já vislumbrar o modo pelo 
                qual a constante divulgação das informações talvez venha a ressaltar a relatividade 
                de alternativas às soluções ortodoxas. Assim mesmo, a contínua expansão de nossa 
                atividade assume importantes posições no estabelecimento das condições inegavelmente 
                apropriadas. No entanto, não podemos esquecer que a revolução dos costumes auxilia 
                a preparação e a composição das novas proposições. As experiências acumuladas 
                demonstram que o novo modelo estrutural aqui preconizado acarreta um processo 
                de reformulação e modernização do processo de comunicação como um todo.
                """;
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

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccount
                .validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthMoreThan255_whenCallsNewAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = """
                O empenho em analisar a execução dos pontos do programa causa impacto indireto na
                reavaliação dos modos de operação convencionais. Podemos já vislumbrar o modo pelo
                qual a constante divulgação das informações talvez venha a ressaltar a relatividade
                de alternativas às soluções ortodoxas. Assim mesmo, a contínua expansão de nossa
                atividade assume importantes posições no estabelecimento das condições inegavelmente
                apropriadas. No entanto, não podemos esquecer que a revolução dos costumes auxilia
                a preparação e a composição das novas proposições. As experiências acumuladas
                demonstram que o novo modelo estrutural aqui preconizado acarreta um processo
                de reformulação e modernização do processo de comunicação como um todo.
                """;
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";

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

    @Test
    public void givenAValidValues_whenCallsUpdateAccount_thenAnAccountShouldBeUpdated() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678Ab";
        final var aMailStatus = AccountMailStatus.CONFIRMED;
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
                aMailStatus,
                aPassword,
                aAvatarUrl
        );

        //then
        Assertions.assertDoesNotThrow(() -> aAccountUpdated.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(aAccount.getId(), aAccountUpdated.getId());
        Assertions.assertEquals(aAccount.getFirstName(), aAccountUpdated.getFirstName());
        Assertions.assertEquals(aAccount.getLastName(), aAccountUpdated.getLastName());
        Assertions.assertEquals(aAccount.getEmail(), aAccountUpdated.getEmail());
        Assertions.assertEquals(aMailStatus, aAccountUpdated.getMailStatus());
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
        final var aMailStatus = AccountMailStatus.CONFIRMED;
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
                aMailStatus,
                aPassword,
                aAvatarUrl
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccountUpdated
                .validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthLessThan8_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345";
        final var aMailStatus = AccountMailStatus.CONFIRMED;
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
                aMailStatus,
                aPassword,
                aAvatarUrl
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccountUpdated
                .validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordLengthMoreThan255_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = """
                O empenho em analisar a execução dos pontos do programa causa impacto indireto na
                reavaliação dos modos de operação convencionais. Podemos já vislumbrar o modo pelo
                qual a constante divulgação das informações talvez venha a ressaltar a relatividade
                de alternativas às soluções ortodoxas. Assim mesmo, a contínua expansão de nossa
                atividade assume importantes posições no estabelecimento das condições inegavelmente
                apropriadas. No entanto, não podemos esquecer que a revolução dos costumes auxilia
                a preparação e a composição das novas proposições. As experiências acumuladas
                demonstram que o novo modelo estrutural aqui preconizado acarreta um processo
                de reformulação e modernização do processo de comunicação como um todo.
                """;
        final var aMailStatus = AccountMailStatus.CONFIRMED;
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
                aMailStatus,
                aPassword,
                aAvatarUrl
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccountUpdated
                .validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidPasswordButNotContainsLowerAndUpercaseLetter_whenCallsUpdateAccount_thenAnExceptionShouldBeThrown() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var aMailStatus = AccountMailStatus.CONFIRMED;
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
                aMailStatus,
                aPassword,
                aAvatarUrl
        );

        final var aException = Assertions.assertThrows(DomainException.class, () -> aAccountUpdated
                .validate(new ThrowsValidationHandler()));

        //then
        Assertions.assertEquals(expectedErrorMessage, aException.getErrors().get(0).message());
    }
}
