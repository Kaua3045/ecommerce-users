package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    // 1. Teste do caminho feliz (onde veio tudo certo) ADDED
    // 2. Teste passando uma propriedade inválida ADDED
    // 3. Teste criando uma conta com um email já existente ADDED
    // 4. Simulando um erro genérico vindo do gateway NOT ADDED

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private DefaultCreateAccountUseCase useCase;

    @Test
    public void givenAValidCommand_whenCallCreateAccount_thenShouldReturneAnAccountId() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        Mockito.when(accountGateway.existsByEmail(Mockito.any()))
                .thenReturn(false);
        Mockito.when(accountGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aOutput = useCase.execute(aCommand).getRight();

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertNotNull(aOutput.id());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(aEmail);
        Mockito.verify(accountGateway, Mockito.times(1))
                .create(Mockito.argThat(aAccount ->
                        Objects.equals(aFirstName, aAccount.getFirstName()) &&
                        Objects.equals(aLastName, aAccount.getLastName()) &&
                        Objects.equals(aEmail, aAccount.getEmail()) &&
                        Objects.equals(aPassword, aAccount.getPassword()) &&
                        Objects.nonNull(aAccount.getId()) &&
                        Objects.equals(AccountMailStatus.WAITING_CONFIRMATION, aAccount.getMailStatus()) &&
                        Objects.nonNull(aAccount.getCreatedAt()) &&
                        Objects.nonNull(aAccount.getUpdatedAt())
                ));
    }

    @Test
    public void givenAnInvalidFirstName_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidFirstNameLengthLessThan3_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "F ";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidFirstNameLengthMoreThan255_whenCallCreateAccount_thenShouldReturnAnError() {
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
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'firstName' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidLastName_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final String aLastName = null;
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidLastNameLengthLessThan3_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "S ";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidLastNameLengthMoreThan255_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
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
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'lastName' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidEmail_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'email' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnExistingEmail_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "1234567Ab";
        final var expectedErrorMessage = "'email' already exists";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        Mockito.when(accountGateway.existsByEmail(aEmail))
                .thenReturn(true);

        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(aEmail);
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidPassword_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "";
        final var expectedErrorMessage = "'password' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidPasswordLengthLessThan8_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123";
        final var expectedErrorMessage = "'password' must be between 8 and 255 characters";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidPasswordLengthMoreThan255_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
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
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }

    @Test
    public void givenAnInvalidPasswordWithoutUpperLetterAndLowerLetter_whenCallCreateAccount_thenShouldReturnAnError() {
        // given
        final var aFirstName = "Fulano";
        final var aLastName = "Silveira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "12345678";
        final var expectedErrorMessage = "'password' should contain at least one uppercase letter, one lowercase letter and one number";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAccountCommand.with(aFirstName, aLastName, aEmail, aPassword);

        // when
        final var aNotification = useCase.execute(aCommand).getLeft();

        // then
        Assertions.assertEquals(expectedErrorCount, aNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, aNotification.getErrors().get(0).message());

        Mockito.verify(accountGateway, Mockito.times(1))
                .existsByEmail(Mockito.any());
        Mockito.verify(accountGateway, Mockito.never())
                .create(Mockito.any());
    }
}
