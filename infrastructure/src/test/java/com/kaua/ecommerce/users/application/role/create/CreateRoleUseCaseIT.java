package com.kaua.ecommerce.users.application.role.create;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateRoleUseCaseIT {

    @Autowired
    private CreateRoleUseCase createRoleUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @SpyBean
    private RoleGateway roleGateway;

    @Test
    void givenAValidCommandWithDescription_whenCallCreateRole_shouldReturnRoleId() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        Assertions.assertEquals(0, roleRepository.count());

        final var aCommand = new CreateRoleCommand(aName, aDescription, aRoleType.name(), aIsDefault);

        final var actualResult = this.createRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertNotNull(actualRole.getCreatedAt());
        Assertions.assertNotNull(actualRole.getUpdatedAt());
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallCreateRole_shouldReturnRoleId() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        Assertions.assertEquals(0, roleRepository.count());

        final var aCommand = new CreateRoleCommand(aName, aDescription, aRoleType.name(), aIsDefault);

        final var actualResult = this.createRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertNotNull(actualRole.getCreatedAt());
        Assertions.assertNotNull(actualRole.getUpdatedAt());
    }

    @Test
    void givenAnInvalidName_whenCallCreateRole_thenShouldReturnDomainException() {
        final String aName = null;
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = "employees";
        final var aIsDefault = false;
        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, roleRepository.count());

        final var aCommand = new CreateRoleCommand(aName, aDescription, aRoleType, aIsDefault);

        final var notification = this.createRoleUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Assertions.assertEquals(0, roleRepository.count());

        Mockito.verify(roleGateway, Mockito.times(0)).create(Mockito.any());
    }
}
