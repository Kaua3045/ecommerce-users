package com.kaua.ecommerce.users.application.usecases.role.delete;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.application.usecases.role.delete.DefaultDeleteRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleCommand;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteRoleUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private DefaultDeleteRoleUseCase useCase;

    @Test
    void givenAValidCommandWithRoleId_whenCallDeleteById_shouldBeOk() {
        // given
        final var aRole = Role.newRole("ceo", null, RoleTypes.EMPLOYEES, true);
        final var aId = aRole.getId().getValue();

        final var aCommand = DeleteRoleCommand.with(aId);

        // when
        Mockito.doNothing().when(roleGateway).deleteById(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(roleGateway, Mockito.times(1))
                .deleteById(aId);
    }

    @Test
    void givenAnInvalidCommandWithRoleIdNotExists_whenCallDeleteById_shouldBeOk() {
        // given
        final var aId = "123";

        final var aCommand = DeleteRoleCommand.with(aId);

        // when
        Mockito.doNothing().when(roleGateway).deleteById(aId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        // then
        Mockito.verify(roleGateway, Mockito.times(1))
                .deleteById(aId);
    }
}
