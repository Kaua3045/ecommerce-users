package com.kaua.ecommerce.users.application.account.update.role;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountRoleUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private DefaultUpdateAccountRoleUseCase useCase;

    @Test
    void givenAValidCommand_whenCallUpdateRole_thenShouldReturneAccountId() {
        // given
        final var aRole = Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false);
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab",
                Role.newRole("User", "Common user", RoleTypes.COMMON, true)
        );

        final var aCommand = UpdateAccountRoleCommand.with(aAccount.getId().getValue(), aRole.getId().getValue());

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));
        Mockito.when(roleGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aRole));
        Mockito.when(accountGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(aOutput);
        Assertions.assertNotNull(aOutput.id());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(aAccount.getId().getValue());
        Mockito.verify(roleGateway, Mockito.times(1))
                .findById(aRole.getId().getValue());
        Mockito.verify(accountGateway, Mockito.times(1))
                .update(Mockito.argThat(account ->
                        Objects.nonNull(account.getId()) &&
                                Objects.equals(AccountMailStatus.WAITING_CONFIRMATION, account.getMailStatus()) &&
                                Objects.equals(aAccount.getFirstName(), account.getFirstName()) &&
                                Objects.equals(aAccount.getLastName(), account.getLastName()) &&
                                Objects.equals(aAccount.getEmail(), account.getEmail()) &&
                                Objects.equals(aAccount.getPassword(), account.getPassword()) &&
                                Objects.isNull(account.getAvatarUrl()) &&
                                Objects.equals(aAccount.getCreatedAt(), account.getCreatedAt()) &&
                                Objects.nonNull(account.getUpdatedAt()) &&
                                Objects.equals(aRole, account.getRole())
                ));
    }

    @Test
    void givenAnInvalidCommandAccountId_whenCallUpdateRole_thenShouldThrowsNotFoundException() {
        // given
        final var aRole = Role.newRole("User", "Common user", RoleTypes.COMMON, true);
        final var expectedErrorMessage = "Account with id 123 was not found";

        final var aCommand = UpdateAccountRoleCommand.with("123", aRole.getId().getValue());

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(roleGateway, Mockito.times(0))
                .findById(Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(0))
                .update(Mockito.any());
    }

    @Test
    void givenAnInvalidCommandRoleId_whenCallUpdateRole_thenShouldThrowsNotFoundException() {
        // given
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silveira",
                "teste@teste.com",
                "1234567Ab",
                Role.newRole("User", "Common user", RoleTypes.COMMON, true)
        );
        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aCommand = UpdateAccountRoleCommand.with(aAccount.getId().getValue(), "123");

        // when
        Mockito.when(accountGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aAccount));
        Mockito.when(roleGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var aOutput = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, aOutput.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1))
                .findById(aAccount.getId().getValue());
        Mockito.verify(roleGateway, Mockito.times(1))
                .findById(Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(0))
                .update(Mockito.any());
    }
}
