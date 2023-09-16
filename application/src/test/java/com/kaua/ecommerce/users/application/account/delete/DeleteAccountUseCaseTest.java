package com.kaua.ecommerce.users.application.account.delete;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.AvatarGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountUseCaseTest {

    // 1. Teste do caminho feliz (onde veio tudo certo) ADDED
    // 2. Teste passando uma propriedade inválida ADDED
    // 3. Teste criando uma conta com um email já existente ADDED
    // 4. Simulando um erro genérico vindo do gateway NOT ADDED

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AccountMailGateway accountMailGateway;

    @Mock
    private AvatarGateway avatarGateway;

    @InjectMocks
    private DefaultDeleteAccountUseCase useCase;

    @Test
    void givenAValidCommandWithAccountId_whenCallDeleteById_shouldBeOk() {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567890Ab",
                RoleID.unique()
        );
        final var aId = aAccount.getId().getValue();

        final var aCommand = DeleteAccountCommand.with(aId);

        // when
        Mockito.when(accountMailGateway.findAllByAccountId(aId)).thenReturn(List.of(
                AccountMail.newAccountMail(
                        RandomStringUtils.generateValue(36),
                        AccountMailType.ACCOUNT_CONFIRMATION,
                        aAccount,
                        InstantUtils.now().plus(10, ChronoUnit.MINUTES)
                )
        ));
        Mockito.doNothing().when(accountGateway).deleteById(aId);
        Mockito.doNothing().when(avatarGateway).delete(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(accountGateway, Mockito.times(1))
                .deleteById(aId);
        Mockito.verify(avatarGateway, Mockito.times(1))
                .delete(aId);
    }

    @Test
    void givenAnInvalidCommandWithAccountIdNotExists_whenCallDeleteById_shouldBeOk() {
        // given
        final var aId = "123";

        final var aCommand = DeleteAccountCommand.with(aId);

        // when
        Mockito.when(accountMailGateway.findAllByAccountId(aId)).thenReturn(List.of());
        Mockito.doNothing().when(accountGateway).deleteById(aId);
        Mockito.doNothing().when(avatarGateway).delete(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(accountGateway, Mockito.times(1))
                .deleteById(aId);
        Mockito.verify(avatarGateway, Mockito.times(1))
                .delete(aId);
    }
}
