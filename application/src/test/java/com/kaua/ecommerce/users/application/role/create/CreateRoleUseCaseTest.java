package com.kaua.ecommerce.users.application.role.create;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
public class CreateRoleUseCaseTest {

    @Mock
    private RoleGateway roleGateway;

    @InjectMocks
    private DefaultCreateRoleUseCase useCase;

    @Test
    void givenAValidCommand_whenCallCreateRole_shouldReturnRoleId() {
        // given
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = "employees";

        final var aCommnad = CreateRoleCommand.with(aName, aDescription, aRoleType);

        // when
        Mockito.when(roleGateway.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var aResult = useCase.execute(aCommnad).getRight();

        // then
        Assertions.assertNotNull(aResult);
        Assertions.assertNotNull(aResult.id());

        Mockito.verify(roleGateway, Mockito.times(1)).existsByName(Mockito.anyString());
        Mockito.verify(roleGateway, Mockito.times(1)).create(argThat(cmd ->
                Objects.equals(aName, cmd.getName()) &&
                Objects.equals(aDescription, cmd.getDescription()) &&
                Objects.equals(aRoleType, cmd.getRoleType().name().toLowerCase()) &&
                Objects.nonNull(cmd.getId()) &&
                Objects.nonNull(cmd.getCreatedAt()) &&
                Objects.nonNull(cmd.getUpdatedAt())
        ));
    }
}
