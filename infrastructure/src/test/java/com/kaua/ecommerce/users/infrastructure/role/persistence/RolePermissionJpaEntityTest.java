package com.kaua.ecommerce.users.infrastructure.role.persistence;

import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RolePermissionJpaEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RolePermissionJpaEntityTest {

    @Test
    void givenAValidValues_whenCallGet_shouldDoesNotReturnFalse() {
        final var aRole = RoleJpaEntity.toEntity(Role.newRole("test", null, RoleTypes.COMMON, true));
        final var aRolePermission = RolePermission.newRolePermission(PermissionID.from("1"), "test");
        final var aEntity = RolePermissionJpaEntity.from(aRole, aRolePermission);

        Assertions.assertEquals(aRole, aEntity.getRole());
        Assertions.assertEquals(aRolePermission.getPermissionName(), aEntity.getPermissionName());
        Assertions.assertEquals(aRolePermission.getPermissionID().getValue(), aEntity.getId().getPermissionId());
    }
}
