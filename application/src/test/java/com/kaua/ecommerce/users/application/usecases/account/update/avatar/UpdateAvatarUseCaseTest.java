package com.kaua.ecommerce.users.application.usecases.account.update.avatar;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AvatarGateway;
import com.kaua.ecommerce.users.application.gateways.CacheGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.utils.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateAvatarUseCaseTest {

    @InjectMocks
    private DefaultUpdateAvatarUseCase useCase;

    @Mock
    private AvatarGateway avatarGateway;

    @Mock
    private AccountGateway accountGateway;

    @Spy
    private CacheGateway<Account> accountCacheGateway;

    @Test
    void givenAValidCommand_whenCallUpdate_shouldReturnAccountId() {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567890Ab",
                Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false)
        );
        final var aId = aAccount.getId().getValue();
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                "avatar.png"
        );
        final var aFinalAvatarUrl = "http://cloud-storage.com/bucket-name/" + aId + "-avatar.png";

        final var aAccountUpdatedToCallCache = aAccount.changeAvatarUrl(aFinalAvatarUrl);

        final var aCommand = UpdateAvatarCommand.with(aId, aResource);

        // when
        Mockito.when(accountGateway.findById(aId)).thenReturn(Optional.of(aAccount));
        Mockito.when(avatarGateway.save(aId, aResource))
                .thenReturn(aFinalAvatarUrl);
        Mockito.when(accountGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualAccount = Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(aId, actualAccount.id());

        Mockito.verify(accountGateway, Mockito.times(1)).findById(aId);
        Mockito.verify(avatarGateway, Mockito.times(1)).save(aId, aResource);
        Mockito.verify(accountGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(aId, cmd.getId().getValue()) &&
                        Objects.equals(aFinalAvatarUrl, cmd.getAvatarUrl()) &&
                        Objects.equals(aAccount.getFirstName(), cmd.getFirstName()) &&
                        Objects.equals(aAccount.getLastName(), cmd.getLastName()) &&
                        Objects.equals(aAccount.getEmail(), cmd.getEmail()) &&
                        Objects.equals(aAccount.getPassword(), cmd.getPassword()) &&
                        Objects.equals(aAccount.getMailStatus(), cmd.getMailStatus()) &&
                        Objects.equals(aAccount.getCreatedAt(), cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aAccount.getRole(), cmd.getRole())
        ));
        Mockito.verify(accountCacheGateway, Mockito.times(1))
                .save(aAccountUpdatedToCallCache);
    }

    @Test
    void givenAnInvalidCommandWithNullResource_whenCallUpdate_shouldReturnAccountIdAndNotUpdateAvatar() {
        // given
        final var aAccount = Account.newAccount(
                "teste",
                "testes",
                "teste@teste.com",
                "1234567890Ab",
                Role.newRole("Ceo", null, RoleTypes.EMPLOYEES, false)
        );
        final var aId = aAccount.getId().getValue();

        final var aCommand = UpdateAvatarCommand.with(aId, null);

        // when
        Mockito.when(accountGateway.findById(aId)).thenReturn(Optional.of(aAccount));
        Mockito.when(accountGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualAccount = Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(aId, actualAccount.id());

        Mockito.verify(accountGateway, Mockito.times(1)).findById(aId);
        Mockito.verify(avatarGateway, Mockito.times(0)).save(Mockito.any(), Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(aId, cmd.getId().getValue()) &&
                        Objects.equals(aAccount.getAvatarUrl(), cmd.getAvatarUrl()) &&
                        Objects.equals(aAccount.getFirstName(), cmd.getFirstName()) &&
                        Objects.equals(aAccount.getLastName(), cmd.getLastName()) &&
                        Objects.equals(aAccount.getEmail(), cmd.getEmail()) &&
                        Objects.equals(aAccount.getPassword(), cmd.getPassword()) &&
                        Objects.equals(aAccount.getMailStatus(), cmd.getMailStatus()) &&
                        Objects.equals(aAccount.getCreatedAt(), cmd.getCreatedAt()) &&
                        Objects.nonNull(cmd.getUpdatedAt()) &&
                        Objects.equals(aAccount.getRole(), cmd.getRole())
        ));
        Mockito.verify(accountCacheGateway, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void givenAnInvalidAccountId_whenCallUpdate_shouldThrowNotFoundException() {
        // given
        final var aId = "123";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/png",
                "avatar.png"
        );
        final var expectedErrorMessage = "Account with id 123 was not found";

        final var aCommand = UpdateAvatarCommand.with(aId, aResource);

        // when
        Mockito.when(accountGateway.findById(aId)).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(accountGateway, Mockito.times(1)).findById(aId);
        Mockito.verify(avatarGateway, Mockito.times(0)).save(Mockito.any(), Mockito.any());
        Mockito.verify(accountGateway, Mockito.times(0)).update(Mockito.any());
        Mockito.verify(accountCacheGateway, Mockito.times(0))
                .save(Mockito.any());
    }
}
