package com.kaua.ecommerce.users.application.role.update;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class UpdateRoleUseCaseIT {

    @Autowired
    private UpdateRoleUseCase updateRoleUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @SpyBean
    private RoleGateway roleGateway;

    @Test
    void givenAValidCommandWithDescription_whenCallUpdateRole_shouldReturnRoleId() {
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, false);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = true;
        final var aId = aRole.getId().getValue();

        final var aCommand = new UpdateRoleCommand(aId, aName, aDescription, aRoleType.name(), aIsDefault);

        Assertions.assertEquals(1, roleRepository.count());

        final var actualResult = this.updateRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(aRole.getUpdatedAt().isBefore(actualRole.getUpdatedAt()));
    }

    @Test
    void givenAValidCommandWithNullDescription_whenCallUpdateRole_shouldReturnRoleId() {
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, false);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aId = aRole.getId().getValue();

        final var aCommand = new UpdateRoleCommand(aId, aName, aDescription, aRoleType.name(), aIsDefault);

        Assertions.assertEquals(1, roleRepository.count());

        final var actualResult = this.updateRoleUseCase.execute(aCommand).getRight();

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        Assertions.assertEquals(1, roleRepository.count());

        final var actualRole = roleRepository.findById(actualResult.id()).get();

        Assertions.assertEquals(aName, actualRole.getName());
        Assertions.assertEquals(aDescription, actualRole.getDescription());
        Assertions.assertEquals(aRoleType, actualRole.getRoleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertTrue(aRole.getUpdatedAt().isBefore(actualRole.getUpdatedAt()));
    }

    @Test
    void givenAnInvalidName_whenCallUpdateRole_thenShouldReturnDomainException() {
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, false);

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var aName = "";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aId = aRole.getId().getValue();

        final var expectedErrorMessage = "'name' should not be null or blank";
        final var expectedErrorCount = 1;

        final var aCommand = new UpdateRoleCommand(aId, aName, aDescription, aRoleType.name(), aIsDefault);

        Assertions.assertEquals(1, roleRepository.count());

        final var notification = this.updateRoleUseCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(roleGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenACommandWithInvalidID_whenCallUpdateRole_shouldThrowNotFoundException() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;
        final var aId = RoleID.from("123").getValue();

        final var expectedErrorMessage = "Role with id 123 was not found";

        final var aCommand = new UpdateRoleCommand(aId, aName, aDescription, aRoleType.name(), aIsDefault);

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> this.updateRoleUseCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
