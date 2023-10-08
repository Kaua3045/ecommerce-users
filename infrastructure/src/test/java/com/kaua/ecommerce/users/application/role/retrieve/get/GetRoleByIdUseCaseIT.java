package com.kaua.ecommerce.users.application.role.retrieve.get;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@IntegrationTest
public class GetRoleByIdUseCaseIT {

    @Autowired
    private GetRoleByIdUseCase getRoleByIdUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidId_whenCallGetRoleById_shouldReturnRole() {
        final var aPermisson = Permission.newPermission("create-user", null);
        final var aRolePermission = RolePermission.newRolePermission(aPermisson.getId(), aPermisson.getName());

        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aType = RoleTypes.EMPLOYEES;
        final var aIsDefault = false;

        final var aRole = Role.newRole(aName, aDescription, aType, aIsDefault);
        aRole.addPermissions(Set.of(aRolePermission));

        final var aId = aRole.getId().getValue();

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermisson));
        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        final var actualRole = this.getRoleByIdUseCase.execute(GetRoleByIdCommand.with(aId));

        Assertions.assertEquals(aId, actualRole.id());
        Assertions.assertEquals(aName, actualRole.name());
        Assertions.assertEquals(aDescription, actualRole.description());
        Assertions.assertEquals(aType.name(), actualRole.roleType());
        Assertions.assertEquals(aIsDefault, actualRole.isDefault());
        Assertions.assertEquals(aRole.getCreatedAt().toString(), actualRole.createdAt());
        Assertions.assertEquals(aRole.getUpdatedAt().toString(), actualRole.updatedAt());
        Assertions.assertEquals(1, actualRole.permissions().size());
    }

    @Test
    void givenAnInvalidId_whenCallGetRoleById_shouldThrowNotFoundException() {
        final var expectedErrorMessage = "Role with id 123 was not found";
        final var aId = RoleID.from("123").getValue();

        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> this.getRoleByIdUseCase.execute(GetRoleByIdCommand.with(aId)));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
