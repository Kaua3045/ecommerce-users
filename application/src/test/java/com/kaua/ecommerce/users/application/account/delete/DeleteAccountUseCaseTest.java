package com.kaua.ecommerce.users.application.account.delete;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountUseCaseTest {

    // 1. Teste do caminho feliz (onde veio tudo certo) ADDED
    // 2. Teste passando uma propriedade inválida ADDED
    // 3. Teste criando uma conta com um email já existente ADDED
    // 4. Simulando um erro genérico vindo do gateway NOT ADDED

    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private DefaultDeleteAccountUseCase useCase;

    @Test
    void givenAValidCommandWithAccountId_whenCallDeleteById_shouldBeOk() {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567890Ab"
        );
        final var aId = aAccount.getId().getValue();

        final var aCommand = DeleteAccountCommand.with(aId);

        // when
        Mockito.doNothing().when(accountGateway).deleteById(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(accountGateway, Mockito.times(1))
                .deleteById(aId);
    }

    @Test
    void givenAnInvalidCommandWithAccountIdNotExists_whenCallDeleteById_shouldBeOk() {
        // given
        final var aId = "123";

        final var aCommand = DeleteAccountCommand.with(aId);

        // when
        Mockito.doNothing().when(accountGateway).deleteById(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(accountGateway, Mockito.times(1))
                .deleteById(aId);
    }
}
